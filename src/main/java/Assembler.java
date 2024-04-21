package cube;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class Assembler {
    public static class ModrmBasedInstruction {
        public enum Width {
            _8_BITS,
            _16_BITS,
            _32_BITS,
            _64_BITS_REG_32_BITS_MEM,
            _64_BITS,
        }

        public static ModrmBasedInstruction fromRegisterAndImmediate(String register, int immediate) {
            ModrmBasedInstruction res = new ModrmBasedInstruction();
            return res;
        }

        public static ModrmBasedInstruction fromRegisterDisplacementAndImmediate(String register, int displacement, int immediate) {
            ModrmBasedInstruction res = new ModrmBasedInstruction();
            return res;
        }

        ArrayList<Byte> encode() {
            ArrayList<Byte> res = new ArrayList<Byte>();

            // Operand-size override prefix
            if (operand_size_override) {
                res.add((byte)0x66);
            }

            // REX prefix
            int rex = (1 << 6) + (rex_w ? (1 << 3) : 0) + (rex_r ? ( 1 << 2) : 0) + (rex_x ? (1 << 1) : 0) + (rex_b ? 1 : 0);
            if (rex != (1 << 6)) {
                res.add((byte)rex);
            }

            // OpCode
            if (opcode > 0xFF) {
                res.add((byte)(opcode >> 8));
            }
            res.add((byte)(opcode & 0xFF));

            // ModR/M byte
            int modrm = (modrm_mod << 6) | (modrm_reg << 3) | (modrm_rm);
            res.add((byte)modrm);

            // Immediate
            res.addAll(immediate);

            return res;
        }

        public void setOpcode(int _opcode) {
            opcode = _opcode;
        }

        public void setMod(int mod) {
            modrm_mod = mod;
        }

        public void setRegister(int registerIndex) {
            rex_r = registerIndex > 7;
            modrm_reg = registerIndex & 0x7;
        }

        public void setRegister(String register) {
            register = stringRemovePrefix(register, "%");
            if (is16BitRegister(register)) {
                operand_size_override = true;
            }
            if (register.startsWith("r")) {
                rex_w = true;
            }
            setRegister(getRegisterIndex(register));
        }

        public void setOpcodeExtension(int opcodeExtension) {
            modrm_reg = opcodeExtension;
        }

        public void setSecondRegister(String register) {
            register = stringRemovePrefix(register, "%");
            if (is16BitRegister(register)) {
                operand_size_override = true;
            }
            rex_w = register.startsWith("r");

            int registerIndex = getRegisterIndex(register);
            rex_b = registerIndex > 7;

            modrm_rm = registerIndex & 0x7;
        }

        void setImmediate(int _immediate, int numberOfBytes) {
            for (int i = 0; i < numberOfBytes; ++i) {
                immediate.add((byte)((_immediate >> (8 * i)) & 0xFF));
            }
        }

        void setImmediate8Bit(int _immediate) {
            setImmediate(_immediate, 1);
        }

        void setImmediate32Bit(int _immediate) {
            setImmediate(_immediate, 4);
        }

        boolean rex_w = false;
        boolean rex_r = false;
        boolean rex_x = false;
        boolean rex_b = false;

        boolean operand_size_override = false;

        int opcode;

        int modrm_mod;
        int modrm_reg;
        int modrm_rm;

        ArrayList<Byte> immediate = new ArrayList<Byte>();
    }

    public static class AssemblerException extends Exception {
        public AssemblerException(String description) {
            super(description);
        }
    }

    public static class UnknownAssemblerCommandException extends AssemblerException {
        public UnknownAssemblerCommandException(String line) {
            super("Can't parse line: " + line);
        }
    }

    public Assembler(String sourceCode) throws UnknownAssemblerCommandException {
        String lines[] = sourceCode.split("\\R");

        for (String line : lines) {
            line = line.replaceAll(",", " ");
            line = line.replaceAll("^\\s+", "");
            line = line.replaceAll("\\s+$", "");
            line = line.replaceAll("\\s+", " ");

            if (line.length() == 0) {
                continue;
            }

            if (line.charAt(0) == '.') {
                throw new UnknownAssemblerCommandException(line);
            }

            String commandParts[] = line.split(" ");

            throw new UnknownAssemblerCommandException(line);
        }
    }

    public static ArrayList<Byte> encodeCommand(String line) throws UnknownAssemblerCommandException {
        line = line.replaceAll(",", " ");
        line = line.replaceAll("^\\s+", "");
        line = line.replaceAll("\\s+$", "");
        line = line.replaceAll("\\s+", " ");
        String commandParts[] = line.split(" ");
        ArrayList<String> params = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(commandParts, 1, commandParts.length)));
        return encodeCommand(commandParts[0], params);
    }

    public static ArrayList<Byte> encodeCommand(String mnemonic, ArrayList<String> params) throws UnknownAssemblerCommandException {
        ArrayList<Byte> res = new ArrayList<Byte>();

        if (mnemonic.equals("nop")) {
            res.add((byte) 0x90);
            return res;
        }

        if (mnemonic.equals("ret")) {
            res.add((byte) 0xc3);
            return res;
        }

        if (mnemonic.equals("syscall")) {
            res.add((byte) 0x0f);
            res.add((byte) 0x05);
            return res;
        }

        if (mnemonic.equals("movq")) {
        }

        if (arithmeticOperationsCodes.containsKey(mnemonic)) {
            int operationIndex = arithmeticOperationsCodes.get(mnemonic);
            if (params.get(0).charAt(0) == '$') {
                int immediate = Integer.decode(params.get(0).substring(1, params.get(0).length()));
                if ((-128 <= immediate) && (immediate <= 127)) {
                    ModrmBasedInstruction instr = new ModrmBasedInstruction();
                    instr.setOpcode(0x83);
                    instr.setMod(3);
                    instr.setOpcodeExtension(operationIndex);
                    instr.setSecondRegister(params.get(1));
                    instr.setImmediate8Bit(immediate);
                    return instr.encode();
                }
            }

            if (looksLikeRegister(params.get(0)) && looksLikeRegister(params.get(1))) {
                ModrmBasedInstruction instr = new ModrmBasedInstruction();
                instr.setOpcode(operationIndex * 8 + 1);
                instr.setMod(3);
                instr.setRegister(params.get(0));
                instr.setSecondRegister(params.get(1));
                return instr.encode();
            }
        }

        if (rotationAndShiftIndexes.containsKey(mnemonic)) {
            if (params.get(0).charAt(0) == '$') {
                ModrmBasedInstruction instr = new ModrmBasedInstruction();
                instr.setOpcode(0xc1);
                instr.setMod(3);
                instr.setOpcodeExtension(rotationAndShiftIndexes.get(mnemonic));
                instr.setSecondRegister(params.get(1));
                instr.setImmediate8Bit((byte) (int) Integer.decode(params.get(0).substring(1, params.get(0).length())));
                return instr.encode();
            }
        }

        if (mnemonic.equals("mul")) {
            if (params.size() == 1) {
                ModrmBasedInstruction instr = new ModrmBasedInstruction();
                instr.setOpcode(0xf7);
                instr.setMod(3);
                instr.setOpcodeExtension(4);
                instr.setSecondRegister(params.get(0));
                return instr.encode();
            }
        }

        if (mnemonic.equals("imul")) {
            if (params.get(0).startsWith("$") && params.get(1).startsWith("%") && params.get(2).startsWith("%")) {
                ModrmBasedInstruction instr = new ModrmBasedInstruction();
                int immediate = Integer.decode(params.get(0).substring(1, params.get(0).length()));
                instr.setOpcode(0x69);
                instr.setMod(3);
                instr.setRegister(params.get(1));
                instr.setSecondRegister(params.get(2));
                instr.setImmediate32Bit(immediate);
                return instr.encode();
            }
        }

        if (mnemonic.equals("div")) {
            if (params.size() == 1) {
                ModrmBasedInstruction instr = new ModrmBasedInstruction();
                instr.setOpcode(0xf7);
                instr.setMod(3);
                instr.setOpcodeExtension(6);
                instr.setSecondRegister(params.get(0));
                return instr.encode();
            }
        }

        if (mnemonic.equals("idiv")) {
            if (params.size() == 1) {
                ModrmBasedInstruction instr = new ModrmBasedInstruction();
                instr.setOpcode(0xf7);
                instr.setMod(3);
                instr.setOpcodeExtension(7);
                instr.setSecondRegister(params.get(0));
                return instr.encode();
            }
        }

        if (mnemonic.equals("not")) {
            if (params.size() == 1) {
                ModrmBasedInstruction instr = new ModrmBasedInstruction();
                instr.setOpcode(0xf7);
                instr.setMod(3);
                instr.setOpcodeExtension(2);
                instr.setSecondRegister(params.get(0));
                return instr.encode();
            }
        }

        throw new UnknownAssemblerCommandException("");//mnemonic + " " + String.join(" ", params));
    }

    static HashMap<String, Integer> registerIndexes = new HashMap<String, Integer>() {{
        put("ax", 0);
        put("cx", 1);
        put("dx", 2);
        put("bx", 3);
        put("sp", 4);
        put("bp", 5);
        put("si", 6);
        put("di", 7);
        put("8", 8);
        put("9", 9);
        put("10", 10);
        put("11", 11);
        put("12", 12);
        put("13", 13);
        put("14", 14);
        put("15", 15);
    }};

    static boolean is16BitRegister(String register) {
        register = stringRemovePrefix(register, "%");
        return registerIndexes.containsKey(register);
    }

    static HashMap<String, Integer> rotationAndShiftIndexes = new HashMap<String, Integer>() {{
        put("rol", 0);
        put("ror", 1);
        put("rcl", 2);
        put("rcr", 3);
        put("shl", 4);
        put("shr", 5);
        put("sal", 6);
        put("sar", 7);
    }};

    static String stringRemovePrefix(String str, String prefix) {
        if (str.startsWith(prefix)) {
            return str.substring(prefix.length(), str.length());
        }
        return str;
    }

    static int getRegisterIndex(String register) {
        register = stringRemovePrefix(register, "%");
        register = stringRemovePrefix(register, "r");
        register = stringRemovePrefix(register, "e");
        return registerIndexes.get(register);
    }

    static boolean looksLikeRegister(String register) {
        register = stringRemovePrefix(register, "%");
        register = stringRemovePrefix(register, "r");
        register = stringRemovePrefix(register, "e");
        return registerIndexes.containsKey(register);
    }

    static HashMap<String, Integer> arithmeticOperationsCodes = new HashMap<String, Integer>() {{
        put("add", 0);
        put("or", 1);
        put("adc", 2);
        put("sbb", 3);
        put("and", 4);
        put("sub", 5);
        put("xor", 6);
        put("cmp", 7);
    }};
}