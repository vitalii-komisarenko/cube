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
}