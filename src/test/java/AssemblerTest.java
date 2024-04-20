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
}