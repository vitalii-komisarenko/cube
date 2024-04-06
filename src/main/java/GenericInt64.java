public class GenericInt64 {
    GenericInt64(String register, int offset) {
        this.register = register;
        this.offset = offset;
    }
    // The code is position independent.
    // `register` variable determines which register is used for the offset (e.g. instruction pointer or frame pointer).
    String register;
    int offset;

    String location() {
        return String.valueOf(offset) + "(%" + register + ")";
    }

    String assign(int value) {
        return "movq $" + String.valueOf(value) + ", " + location();
    }

    void moveToRegister(String targetRegisterName) {
        return "movq " + location() + ", " + targetRegisterName;
    }
}