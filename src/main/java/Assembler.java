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
            return res;
        }

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
        HashMap<String, Integer> registerNumbers = new HashMap<String, Integer>();
        registerNumbers.put("rax", 0);
        registerNumbers.put("rcx", 1);
        registerNumbers.put("rdx", 2);
        registerNumbers.put("rbx", 3);
        registerNumbers.put("rsp", 4);
        registerNumbers.put("rbp", 5);
        registerNumbers.put("rsi", 6);
        registerNumbers.put("rdi", 7);
        registerNumbers.put("r8", 8);
        registerNumbers.put("r9", 9);
        registerNumbers.put("r10", 10);
        registerNumbers.put("r11", 11);
        registerNumbers.put("r12", 12);
        registerNumbers.put("r13", 13);
        registerNumbers.put("r14", 14);
        registerNumbers.put("r15", 15);

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

        throw new UnknownAssemblerCommandException("");//mnemonic + " " + String.join(" ", params));
    }
}