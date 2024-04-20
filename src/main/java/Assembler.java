package cube;

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
            int rex = (1 << 6) + (rex_w ? (1 << 3) : 0) + (rex_r ? ( 1 << 2) : 0) + (rex_x ? (1 << 1) : 0) + (rex_b ? 1 : 0);
            if (rex != (1 << 6)) {
                res.add((byte)rex);
            }
            return res;
        }

        boolean rex_w = false;
        boolean rex_r = false;
        boolean rex_x = false;
        boolean rex_b = false;

        public int mode;
        public int reg;
        public int register_or_mode;
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
                    if (params.get(1).startsWith("%r")) { // 64-bit register
                        int registerIndex = registerIndexes.get(params.get(1).substring(1, params.get(1).length()));
                        res.add((byte) (0x48 + (registerIndex > 7 ? 4 : 0)));
                        res.add((byte) 0x83);
                        res.add((byte) (0xc4 + (operationIndex << 3)));
                        res.add((byte) immediate);
                        return res;
                    }
                }
            }
        }

        throw new UnknownAssemblerCommandException("");//mnemonic + " " + String.join(" ", params));
    }

    static HashMap<String, Integer> registerIndexes = new HashMap<String, Integer>() {{
        put("rax", 0);
        put("rcx", 1);
        put("rdx", 2);
        put("rbx", 3);
        put("rsp", 4);
        put("rbp", 5);
        put("rsi", 6);
        put("rdi", 7);
        put("r8", 8);
        put("r9", 9);
        put("r10", 10);
        put("r11", 11);
        put("r12", 12);
        put("r13", 13);
        put("r14", 14);
        put("r15", 15);
    }};

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