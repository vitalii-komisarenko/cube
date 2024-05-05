package cube.test;

import cube.Assembler;
import cube.Assembler.AssemblerException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

public class AssemblerTest {
    static void printArrayListOfBytes(List<Byte> list) {
        System.out.println("  <" + list.size() + " item(s)>");
        for (Byte item : list) {
            System.out.println("    " + item);
        }
    }

    static void assertByteArraysAreEqual(List<Byte> first, List<Byte> second) {
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

    static List<Byte> parseStringIntoBytes(String input) {
        // a helper function to parse strings like this: 12 34 56 78 90 ab cd ef
        List<Byte> res = new ArrayList<>();
        for (String byteString : input.split("\\s+")) {
            res.add((byte)Integer.parseInt(byteString, 16));
        }
        return res;
    }

    static void checkAsm(String assemblerLine, String expectedOutput) {
        try {
            List<Byte> actual = Assembler.encodeCommand(assemblerLine);
            List<Byte> expected = parseStringIntoBytes(expectedOutput);
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }
    }

    static void checkAsm(String assemblerLine, String expectedOutput, int currentInstructionAddress) {
        try {
            List<Byte> actual = Assembler.encodeCommand(assemblerLine, currentInstructionAddress);
            List<Byte> expected = parseStringIntoBytes(expectedOutput);
            assertByteArraysAreEqual(actual, expected);
        }
        catch (AssemblerException e) {
            fail("AssemblerException: " + e.getMessage());
        }
    }

    @Test
    public void testNop() {
        checkAsm("nop", "90");
    }

    @Test
    public void testRet() {
        checkAsm("ret", "c3");
    }

    @Test
    public void testSyscall() {
        checkAsm("syscall", "0f 05");
    }

    @Test
    public void testArithmeticAccumulatorWithImmediate8bits() {
        checkAsm("add $0 %al", "04 00");
        checkAsm("add $0x1 %al", "04 01");
        checkAsm("add $0xff %al", "04 ff");
        checkAsm("add $0x7f %al", "04 7f");
        checkAsm("add $-0x80 %al", "04 80");
        checkAsm("add $-0xff %al", "04 01");

        checkAsm("or $0x1 %al", "0c 01");
        checkAsm("adc $0 %al", "14 00 ");
        checkAsm("sbb $0 %al", "1c 00");
        checkAsm("and $0xff %al", "24 ff");
        checkAsm("sub $0x1 %al", "2c 01");
        checkAsm("xor $0x1 %al", "34 01");
        checkAsm("cmp $0x7f %al", "3c 7f");

        checkAsm("add $0x100 %eax", "05 00 01 00 00");
        checkAsm("or $0x100 %eax", "0d 00 01 00 00");
        checkAsm("adc $0x100 %eax", "15 00 01 00 00");
        checkAsm("sbb $0x100 %eax", "1d 00 01 00 00");
        checkAsm("and $0x100 %eax", "25 00 01 00 00");
        checkAsm("sub $0x100 %eax", "2d 00 01 00 00");
        checkAsm("xor $0x100 %eax", "35 00 01 00 00");
        checkAsm("cmp $0x100 %eax", "3d 00 01 00 00");

        checkAsm("add %r15b %cl", "44 00 f9");

        checkAsm("add 0x1(%r8) %sil", "41 02 70 01");
        checkAsm("add %sil 0x1(%r8)", "41 00 70 01");
        checkAsm("or 0x1(%r8) %sil", "41 0a 70 01");
        checkAsm("or %sil 0x1(%r8)", "41 08 70 01");
        checkAsm("adc 0x1(%r8) %sil", "41 12 70 01");
        checkAsm("adc %sil 0x1(%r8)", "41 10 70 01");
        checkAsm("sbb 0x1(%r8) %sil", "41 1a 70 01");
        checkAsm("sbb %sil 0x1(%r8)", "41 18 70 01");
        checkAsm("and 0x1(%r8) %sil", "41 22 70 01");
        checkAsm("and %sil 0x1(%r8)", "41 20 70 01");
        checkAsm("sub 0x1(%r8) %sil", "41 2a 70 01");
        checkAsm("sub %sil 0x1(%r8)", "41 28 70 01");
        checkAsm("xor 0x1(%r8) %sil", "41 32 70 01");
        checkAsm("xor %sil 0x1(%r8)", "41 30 70 01");
        checkAsm("cmp 0x1(%r8) %sil", "41 3a 70 01");
        checkAsm("cmp %sil 0x1(%r8)", "41 38 70 01");

        checkAsm("add %rax %rbx", "48 01 c3");
        checkAsm("add %r15 %rcx", "4c 01 f9");
        checkAsm("add 0x1(%r8) %rsi", "49 03 70 01");
        checkAsm("add %rsi 0x1(%r8)", "49 01 70 01");
        checkAsm("or 0x1(%r8) %rsi", "49 0b 70 01");
        checkAsm("or %rsi 0x1(%r8)", "49 09 70 01");
        checkAsm("adc 0x1(%r8) %rsi", "49 13 70 01");
        checkAsm("adc %rsi 0x1(%r8)", "49 11 70 01");
        checkAsm("sbb 0x1(%r8) %rsi", "49 1b 70 01");
        checkAsm("sbb %rsi 0x1(%r8)", "49 19 70 01");
        checkAsm("and 0x1(%r8) %rsi", "49 23 70 01");
        checkAsm("and %rsi 0x1(%r8)", "49 21 70 01");
        checkAsm("sub 0x1(%r8) %rsi", "49 2b 70 01");
        checkAsm("sub %rsi 0x1(%r8)", "49 29 70 01");
        checkAsm("xor 0x1(%r8) %rsi", "49 33 70 01");
        checkAsm("xor %rsi 0x1(%r8)", "49 31 70 01");
        checkAsm("cmp 0x1(%r8) %rsi", "49 3b 70 01");
        checkAsm("cmp %rsi 0x1(%r8)", "49 39 70 01");
    }

    @Test
    public void testArithmeticWithByteImmediate() {
        checkAsm("add $0x8 %rsp", "48 83 c4 08");
        checkAsm("adc $0x0 %rbp", "48 83 d5 00");
        checkAsm("sub $0x8 %rsp", "48 83 ec 08");
        checkAsm("and $0x40 %esi", "83 e6 40");
        checkAsm("or $0x20 %eax", "83 c8 20");
        checkAsm("cmp $0x1 %eax", "83 f8 01");
    }

    @Test
    public void testArithmeticsWithTwoRegisters() {
        checkAsm("sbb %eax %eax", "19 c0");
        checkAsm("sbb %eax %ebx", "19 c3");
    }

    @Test
    public void testXor() {
        checkAsm("xor %eax %eax", "31 c0");
        checkAsm("xor %esi %esi", "31 f6");
        checkAsm("xor $0x45 %r15", "49 83 f7 45");
    }

    @Test
    public void testImul() {
        checkAsm("imul $0xf4240 %rdx %rdx", "48 69 d2 40 42 0f 00");
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
        checkAsm("je ccabf", "0f 84 4b fd ff ff", 0xccd6e);
        checkAsm("jne 10df4a", "75 6f", 0x10ded9);
        checkAsm("jle 10dd6a", "0f 8e 8e 01 00 00", 0x10dbd6);
        checkAsm("jle 10d000", "0f 8e 76 fb ff ff", 0x10d484);
        checkAsm("jle 10c950", "7e 22", 0x10c92c);
        checkAsm("call 30b20", "e8 63 2c f2 ff", 0x10deb8);
    }

    @Test
    public void testMov() {
        checkAsm("mov $0x20 %edi", "bf 20 00 00 00");
        checkAsm("mov $0x18 %r15d", "41 bf 18 00 00 00");
    }

    @Test
    public void testIncDec() {
        checkAsm("inc %eax", "ff c0");
        checkAsm("dec %eax", "ff c8");
        checkAsm("inc %r15", "49 ff c7");
        checkAsm("dec %rax", "48 ff c8");
    }

    @Test
    public void testSomeXmmInstructions() {
        checkAsm("pxor %xmm0 %xmm0", "66 0f ef c0");
        checkAsm("pcmpeqd %xmm0 %xmm0", "66 0f 76 c0");
        checkAsm("movaps %xmm0 (%r14)", "41 0f 29 06");
        checkAsm("movdqa 0x410(%rsp) %xmm0", "66 0f 6f 84 24 10 04");
    }
}