package cube.test;

import cube.Assembler;
import cube.Assembler.AssemblerException;

import java.util.Arrays;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

public class AssemblerTest {
    static void printArrayListOfBytes(ArrayList<Byte> list) {
        System.out.println("  <" + list.size() + " item(s)>");
        for (Byte item : list) {
            System.out.println("    " + item);
        }
    }

    static void assertByteArraysAreEqual(ArrayList<Byte> first, ArrayList<Byte> second) {
        if (first.size() != second.size()) {
            System.out.println("Lists of bytes are different");
            System.out.println("First:");
            printArrayListOfBytes(first);
            System.out.println("Second:");
            printArrayListOfBytes(second);
            fail("Lists are different");
        }

        for (int i = 0; i < first.size(); ++i) {
            if (!first.get(i).equals(second.get(i))) {
                System.out.println("Lists of bytes are different");
                System.out.println("First:");
                printArrayListOfBytes(first);
                System.out.println("Second:");
                printArrayListOfBytes(second);
                fail("Lists are different");
            }
        }
    }

    static ArrayList<Byte> parseStringIntoBytes(String input) {
        // a helper function to parse strings like this: 12 34 56 78 90 ab cd ef
        ArrayList<Byte> res = new ArrayList<>();
        for (String byteString : input.split("\\s+")) {
            res.add((byte)Integer.parseInt(byteString, 16));
        }
        return res;
    }

    static void checkAsm(String assemblerLine, String expectedOutput) {
        try {
            ArrayList<Byte> actual = Assembler.encodeCommand(assemblerLine);
            ArrayList<Byte> expected = parseStringIntoBytes(expectedOutput);
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }
    }

    static void checkAsm(String assemblerLine, String expectedOutput, int currentInstructionAddress) {
        try {
            ArrayList<Byte> actual = Assembler.encodeCommand(assemblerLine, currentInstructionAddress);
            ArrayList<Byte> expected = parseStringIntoBytes(expectedOutput);
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }
    }

    @Test
    public void testNop() {
        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("nop", new ArrayList<String>());
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0x90));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }
    }

    @Test
    public void testRet() {
        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("ret", new ArrayList<String>());
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0xc3));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }
    }

    @Test
    public void testSyscall() {
        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("syscall", new ArrayList<String>());
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0x0f, (byte)0x05));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }
    }

    @Test
    public void testArithmeticWithByteImmediate() {
        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("add", new ArrayList<>(Arrays.asList("$0x8", "%rsp")));
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0x48, (byte)0x83, (byte)0xc4, (byte)0x08));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }

        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("adc", new ArrayList<>(Arrays.asList("$0x0", "%rbp")));
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0x48, (byte)0x83, (byte)0xd5, (byte)0x00));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }

        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("sub", new ArrayList<>(Arrays.asList("$0x8", "%rsp")));
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0x48, (byte)0x83, (byte)0xec, (byte)0x08));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }

        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("and", new ArrayList<>(Arrays.asList("$0x40", "%esi")));
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0x83, (byte)0xe6, (byte)0x40));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }

        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("or", new ArrayList<>(Arrays.asList("$0x20", "%eax")));
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0x83, (byte)0xc8, (byte)0x20));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }

        checkAsm("cmp $0x1 %eax", "83 f8 01");
    }

    @Test
    public void testArithmeticsWithTwoRegisters() {
        checkAsm("sbb %eax %eax", "19 c0");
        checkAsm("sbb %eax %ebx", "19 c3");
    }

    @Test
    public void testXorWithItself() {
        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("xor", new ArrayList<>(Arrays.asList("%eax", "%eax")));
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0x31, (byte)0xc0));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }

        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("xor", new ArrayList<>(Arrays.asList("%esi", "%esi")));
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0x31, (byte)0xf6));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }
    }

    @Test
    public void testImul() {
        try {
            ArrayList<Byte> actual = Assembler.encodeCommand("imul", new ArrayList<>(Arrays.asList("$0xf4240", "%rdx", "%rdx")));
            ArrayList<Byte> expected = new ArrayList<Byte>(Arrays.asList((byte)0x48, (byte)0x69, (byte)0xd2, (byte)0x40,
                                                                         (byte)0x42, (byte)0x0f, (byte)0x00));
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }
    }

    @Test
    public void testMul() {
        checkAsm("mul %r9", "49 f7 e1");
    }

    @Test
    public void testDivision() {
        checkAsm("div %rcx", "48 f7 f1");
        checkAsm("idiv %ebx", "f7 fb");
    }

    @Test
    public void testNot() {
        checkAsm("not %edx", "f7 d2");
    }

    @Test
    public void testRotationsAndShifts() {
        checkAsm("rol $0x8 %di", "66 c1 c7 08");
        checkAsm("ror $0x2 %rbp", "48 c1 cd 02");
        checkAsm("sal $0x3 %rbp", "48 c1 e5 03");
        checkAsm("shl $0x3 %rbp", "48 c1 e5 03");
        checkAsm("shr $0x1f %eax", "c1 e8 1f");
        checkAsm("sar $0x1f %esi", "c1 fe 1f");
    }

    @Test
    public void testPushPop() {
        checkAsm("push $0x1", "68 01 00 00 00");
        checkAsm("push %r12", "41 54");
        checkAsm("push %rbp", "55");
        checkAsm("pop %rbp", "5d");
        checkAsm("pop %r12", "41 5c");
    }

    @Test
    public void testXchg() {
        checkAsm("xchg %rbx %r12", "49 87 dc");
    }

    @Test
    public void testTest() {
        checkAsm("test %eax, %eax", "85 c0");
        checkAsm("test %rdi %rdi", "48 85 ff");
    }

    @Test
    public void testLea() {
        checkAsm("lea 0xd0847(%rip) %rax", "48 8d 05 47 08 0d 00");
    }

    @Test
    public void testJumpsAndCalls() {
        checkAsm("jmp 10df1f", "eb d5", 0x10df48);
        checkAsm("jmp 10df1f", "eb e4", 0x10df39);
        checkAsm("jmp 473d0", "e9 54 95 f3 ff", 0x10de77);
        checkAsm("call 30b20", "e8 63 2c f2 ff", 0x10deb8);
    }
}