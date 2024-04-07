import java.util.HashMap;

public class Assembler {
    public class AssemblerException extends Exception {
        public AssemblerException(String description) {
            super(description);
        }
    }

    public class UnknownAssemblerCommandException extends AssemblerException {
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
}