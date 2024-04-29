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
            modrm_mod = 3;
            register = stringRemovePrefix(register, "%");
            if (is16BitRegister(register)) {
                operand_size_override = true;
            }
            rex_w = register.startsWith("r");

            int registerIndex = getRegisterIndex(register);
            rex_b = registerIndex > 7;

            modrm_rm = registerIndex & 0x7;
        }

        public void setIndirectOperand(String str) {
            if (str.charAt(0) == '%') {
                setSecondRegister(str);
                return;
            }
            int openingBracketPos = str.indexOf('(');
            String offsetStr = str.substring(0, openingBracketPos);
            String register = str.substring(openingBracketPos + 1, str.length() - 1);
            if (register.equals("%rip")) {
                modrm_mod = 0;
                modrm_rm = 5;
                setImmediate32Bit(parseInterger(offsetStr));
                return;
            }
        }

        int parseInterger(String str) {
            boolean isNegative = str.charAt(0) == '-';
            str = stringRemovePrefix(str, "-");
            str = stringRemovePrefix(str, "$");
            int absValue = Integer.decode(str);
            return isNegative ? -absValue : absValue;
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

    public static ArrayList<Byte> encodeCommand(String line, int currentInstructionAddress) throws UnknownAssemblerCommandException {
        line = line.replaceAll(",", " ");
        line = line.replaceAll("^\\s+", "");
        line = line.replaceAll("\\s+$", "");
        line = line.replaceAll("\\s+", " ");
        String commandParts[] = line.split(" ");
        ArrayList<String> params = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(commandParts, 1, commandParts.length)));
        return encodeCommand(commandParts[0], params, currentInstructionAddress);
    }

    public static ArrayList<Byte> encodeCommand(String mnemonic, ArrayList<String> params) throws UnknownAssemblerCommandException {
        ArrayList<Byte> res = new ArrayList<Byte>();

        HashMap<String, Integer> zeroParamsCommands = new HashMap<String, Integer>() {{
            put("nop", 0x90);
            put("ret", 0xc3);
            put("clc", 0xf8);
            put("stc", 0xf9);
            put("cli", 0xfa);
            put("sti", 0xfb);
            put("cld", 0xfc);
            put("std", 0xfd);
            put("syscall", 0x0f05);
        }};

        if (params.size() == 0) {
            int opcode = zeroParamsCommands.get(mnemonic);
            if (opcode > 0xFF) {
                res.add((byte)(opcode >> 8));
            }
            res.add((byte)(opcode & 0xFF));
            return res;
        }

        if (mnemonic.equals("mov")) {
            if ((params.get(0).charAt(0) == '$') && (params.get(1).charAt(0) == '%')){
                int registerIndex = getRegisterIndex(params.get(1));
                if (registerIndex > 7) {
                    res.add((byte)0x41);
                }
                res.add((byte)(0xb8 + (registerIndex & 0x7)));
                res.addAll(encode32BitsImmediate(params.get(0)));
                return res;
            }
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

        if (mnemonic.equals("push")) {
            if (params.get(0).charAt(0) == '$') {
                res.add((byte)0x68);
                res.addAll(encode32BitsImmediate(params.get(0)));
                return res;
            }

            if (params.get(0).charAt(0) == '%') {
                int registerIndex = getRegisterIndex(params.get(0));
                if (registerIndex > 7) {
                    res.add((byte)0x41);
                }
                res.add((byte)(0x50 + (registerIndex & 0x7)));
                return res;
            }
        }

        if (mnemonic.equals("pop")) {
            if (params.get(0).charAt(0) == '%') {
                int registerIndex = getRegisterIndex(params.get(0));
                if (registerIndex > 7) {
                    res.add((byte)0x41);
                }
                res.add((byte)(0x58 + (registerIndex & 0x7)));
                return res;
            }
        }

        if (mnemonic.equals("xchg")) {
            if (getRegisterIndex(params.get(1)) == 0) {
            }
            else {
                ModrmBasedInstruction instr = new ModrmBasedInstruction();
                instr.setOpcode(0x87);
                instr.setMod(3);
                instr.setRegister(params.get(0));
                instr.setSecondRegister(params.get(1));
                return instr.encode();
            }
        }

        if (mnemonic.equals("test")) {
            if ((params.get(0).charAt(0) == '%') && (params.get(0).charAt(0) == '%')) {
                ModrmBasedInstruction instr = new ModrmBasedInstruction();
                instr.setOpcode(0x85);
                instr.setMod(3);
                instr.setRegister(params.get(0));
                instr.setSecondRegister(params.get(1));
                return instr.encode();
            }
        }

        if (mnemonic.equals("lea")) {
            ModrmBasedInstruction instr = new ModrmBasedInstruction();
            instr.setOpcode(0x8d);
            instr.setRegister(params.get(1));
            instr.setIndirectOperand(params.get(0));
            return instr.encode();
        }

        throw new UnknownAssemblerCommandException("");//mnemonic + " " + String.join(" ", params));
    }

    public static ArrayList<Byte> encodeCommand(String mnemonic, ArrayList<String> params, int currentInstructionAddress) throws UnknownAssemblerCommandException {
        ArrayList<Byte> res = new ArrayList<Byte>();

        if (instructionsWithRelativeAddressesOpcodes.containsKey(mnemonic)) {
            int offset = Integer.decode("0x" + params.get(0)) - currentInstructionAddress;
            if ((-128 <= (offset - 2)) && ((offset - 2) <= 127)) {
                res.addAll(instructionsWithRelativeAddressesOpcodes.get(mnemonic).get(0));
                res.add((byte)(offset - 2));
                return res;
            }
            ArrayList<Byte> opcode = instructionsWithRelativeAddressesOpcodes.get(mnemonic).get(1);
            offset -= 4 + opcode.size();
            res.addAll(opcode);
            res.addAll(encode32BitsImmediate(offset));
            return res;
        }

        throw new UnknownAssemblerCommandException(mnemonic + " " + String.join(" ", params));
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
        put("sal", 4);
        put("shr", 5);
        put("sar", 7);
    }};

    // key: instruction mnemonic
    // value: list of two elements: opcodes for 8-bit and 32-bit relative offsets
    static HashMap<String, ArrayList<ArrayList<Byte>>> instructionsWithRelativeAddressesOpcodes = new HashMap<String, ArrayList<ArrayList<Byte>>>() {{
        put("call", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ }}); // opcode does not exist
            add(new ArrayList<Byte>() {{ add((byte)0xe8); }});
        }});

        put("jmp", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0xeb); }});
            add(new ArrayList<Byte>() {{ add((byte)0xe9); }});
        }});

        put("jo", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x70); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x80); }});
        }});

        put("jno", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x71); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x81); }});
        }});

        put("jb", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x72); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x82); }});
        }});

        put("jnae", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x72); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x82); }});
        }});

        put("jc", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x72); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x82); }});
        }});

        put("jnb", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x73); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x83); }});
        }});

        put("jae", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x73); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x83); }});
        }});

        put("jnc", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x73); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x83); }});
        }});

        put("jz", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x74); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x84); }});
        }});

        put("je", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x74); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x84); }});
        }});

        put("jnz", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x75); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x85); }});
        }});

        put("jne", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x75); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x85); }});
        }});

        put("jbe", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x76); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x86); }});
        }});

        put("jna", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x76); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x86); }});
        }});

        put("jnbe", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x77); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x87); }});
        }});

        put("ja", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x77); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x87); }});
        }});

        put("js", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x78); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x88); }});
        }});

        put("jns", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x79); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x89); }});
        }});

        put("jp", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7a); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8a); }});
        }});

        put("jpe", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7a); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8a); }});
        }});

        put("jnp", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7b); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8b); }});
        }});

        put("jpo", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7b); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8b); }});
        }});

        put("jl", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7c); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8c); }});
        }});

        put("jnge", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7c); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8c); }});
        }});

        put("jnl", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7d); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8d); }});
        }});

        put("jge", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7d); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8d); }});
        }});

        put("jle", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7e); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8e); }});
        }});

        put("jng", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7e); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8e); }});
        }});

        put("jnle", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7f); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8f); }});
        }});

        put("jg", new ArrayList<ArrayList<Byte>>() {{
            add(new ArrayList<Byte>() {{ add((byte)0x7f); }});
            add(new ArrayList<Byte>() {{ add((byte)0x0f); add((byte)0x8f); }});
        }});
    }};

    static String stringRemovePrefix(String str, String prefix) {
        if (str.startsWith(prefix)) {
            return str.substring(prefix.length(), str.length());
        }
        return str;
    }

    static int getRegisterIndex(String register) {
        if (register.charAt(register.length() - 1) == 'd') {
            register = register.substring(0, register.length() - 1);
        }
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

    static ArrayList<Byte> encode32BitsImmediate(int immediate) {
        ArrayList<Byte> bytes = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            bytes.add((byte)((immediate >> (8 * i)) & 0xFF));
        }
        return bytes;
    }

    static ArrayList<Byte> encode32BitsImmediate(String immediate) {
        return encode32BitsImmediate(Integer.decode(immediate.substring(1, immediate.length())));
    }
}