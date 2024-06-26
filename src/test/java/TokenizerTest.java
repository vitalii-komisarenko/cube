package cube.test;

import cube.Tokenizer;
import cube.Tokenizer.Token;
import cube.Tokenizer.TokenType;
import cube.Tokenizer.TokenizerException;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang3.builder.EqualsBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;


public class TokenizerTest {
    void checkParsingToSingleToken(String codeToTokenize, TokenType expectedType, String expectedValue) {
        try {
            ArrayList<Token> actual = new Tokenizer(codeToTokenize).tokens;
            if (actual.size() != 1) {
                fail("Token list size is not 1");
            }
            if (!actual.get(0).type.equals(expectedType)) {
                fail("Token type mismatch");
            }
            if (!actual.get(0).value.equals(expectedValue)) {
                fail("Token value mismatch");
            }
        }
        catch (Tokenizer.TokenizerException e) {
            fail("TokenizerException");
        }
    }

    static public void printTokenList(ArrayList<Token> token_list) {
        System.out.println("\t<" + token_list.size() + " item(s)>");
        for (Token token : token_list) {
            System.out.println("\t" + token);
        }
    }

    static public void checkTokenListsEqual(ArrayList<Token> first, ArrayList<Token> second) {
        if (first.size() != second.size()) {
            System.out.println("First list: ");
            printTokenList(first);
            System.out.println("Second list: ");
            printTokenList(second);
            fail("Token lists have different size");
        }
        for (int i = 0; i < first.size(); ++i) {
            if (!first.get(i).equals(second.get(i))) {
                System.out.println("First list: ");
                printTokenList(first);
                System.out.println("Second list: ");
                printTokenList(second);
                fail("Tokens differ");
            }
        }
    }

    @Test
    public void testEmptyInput() {
        try {
            assertEquals(new Tokenizer("").tokens, new ArrayList<Tokenizer.Token>());
        }
        catch (Tokenizer.TokenizerException e) {
            fail("TokenizerException");
        }
    }

    @Test
    public void testNewLines() {
        try {
            assertTrue(EqualsBuilder.reflectionEquals(
                new Tokenizer("\n").tokens,
                new ArrayList<>(Arrays.asList(
                    new Token(TokenType.NewLine, "\n")
                ))));
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            assertTrue(EqualsBuilder.reflectionEquals(
                new Tokenizer("\n\n").tokens,
                new ArrayList<>(Arrays.asList(
                    new Token(TokenType.NewLine, "\n"),
                    new Token(TokenType.NewLine, "\n")
                ))));
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }


        try {
            assertTrue(EqualsBuilder.reflectionEquals(
                new Tokenizer("\n\n\n\n\n").tokens,
                new ArrayList<>(Arrays.asList(
                    new Token(TokenType.NewLine, "\n"),
                    new Token(TokenType.NewLine, "\n"),
                    new Token(TokenType.NewLine, "\n"),
                    new Token(TokenType.NewLine, "\n"),
                    new Token(TokenType.NewLine, "\n")
                ))));
        }
        catch (Tokenizer.TokenizerException e) {
            fail("TokenizerException");
        }
    }

    @Test
    public void testChars() {
        checkParsingToSingleToken("'a'", TokenType.Char, "a");
        checkParsingToSingleToken("'\\''", TokenType.Char, "'");
        checkParsingToSingleToken("'\\\\'", TokenType.Char, "\\");

        assertThrows(TokenizerException.class, () -> new Tokenizer("'"));
        assertThrows(TokenizerException.class, () -> new Tokenizer("''"));
        assertThrows(TokenizerException.class, () -> new Tokenizer("'a"));
        assertThrows(TokenizerException.class, () -> new Tokenizer("'\\"));
        assertThrows(TokenizerException.class, () -> new Tokenizer("'\\a"));
    }

    @Test
    public void testStrings() {
        checkParsingToSingleToken("\"\"", TokenType.String, "");
        checkParsingToSingleToken("\" \"", TokenType.String, " ");
        checkParsingToSingleToken("\"\\\\\"", TokenType.String, "\\");
        checkParsingToSingleToken("\"\\\"\"", TokenType.String, "\"");
        checkParsingToSingleToken("\"'\"", TokenType.String, "'");

        assertThrows(TokenizerException.class, () -> new Tokenizer("\""));
        assertThrows(TokenizerException.class, () -> new Tokenizer("\"\\"));
    }

    @Test
    public void testIdentifiers() {
        try {
            assertTrue(EqualsBuilder.reflectionEquals(
                new Tokenizer("_").tokens,
                new ArrayList<>(Arrays.asList(
                    new Token(TokenType.Identifier, "_")
                ))));
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            assertTrue(EqualsBuilder.reflectionEquals(
                new Tokenizer("abc123").tokens,
                new ArrayList<>(Arrays.asList(
                    new Token(TokenType.Identifier, "abc123")
                ))));
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }
    }

    @Test
    public void testOctothorps() {
        checkParsingToSingleToken("#", TokenType.Octothorp, "#");
        checkParsingToSingleToken("##", TokenType.DoubleOctothorp, "##");
        checkParsingToSingleToken("#define", TokenType.MacroIdentifier, "#define");
    }

    @Test
    public void testFixedToken() {
        checkParsingToSingleToken("(", TokenType.OpeningRoundBracket, "(");
        checkParsingToSingleToken(")", TokenType.ClosingRoundBracket, ")");
        checkParsingToSingleToken("[", TokenType.OpeningSquareBracket, "[");
        checkParsingToSingleToken("]", TokenType.ClosingSquareBracket, "]");
        checkParsingToSingleToken("{", TokenType.OpeningCurlyBracket, "{");
        checkParsingToSingleToken("}", TokenType.ClosingCurlyBracket, "}");
        checkParsingToSingleToken(",", TokenType.Comma, ",");
        checkParsingToSingleToken(":", TokenType.Colon, ":");
        checkParsingToSingleToken(";", TokenType.Semicolon, ";");

        checkParsingToSingleToken("++", TokenType.Increment, "++");
        checkParsingToSingleToken("+=", TokenType.AddAssign, "+=");
        checkParsingToSingleToken("+", TokenType.Plus, "+");

        checkParsingToSingleToken("--", TokenType.Decrement, "--");
        checkParsingToSingleToken("-=", TokenType.SubstractAssign, "-=");
        checkParsingToSingleToken("->", TokenType.PointerDereference, "->");
        checkParsingToSingleToken("-", TokenType.Minus, "-");

        checkParsingToSingleToken("*=", TokenType.MultipleAssign, "*=");
        checkParsingToSingleToken("*", TokenType.Asterisk, "*");

        checkParsingToSingleToken("/=", TokenType.DivideAssign, "/=");
        checkParsingToSingleToken("/", TokenType.Slash, "/");

        checkParsingToSingleToken("%=", TokenType.Modulo, "%=");
        checkParsingToSingleToken("%", TokenType.ModuloAssign, "%");

        checkParsingToSingleToken("<<", TokenType.ShiftLeft, "<<");
        checkParsingToSingleToken("<=", TokenType.LessOrEqual, "<=");
        checkParsingToSingleToken("<", TokenType.Less, "<");

        checkParsingToSingleToken(">>", TokenType.ShiftRight, ">>");
        checkParsingToSingleToken(">=", TokenType.MoreOrEqual, ">=");
        checkParsingToSingleToken(">", TokenType.More, ">");

        checkParsingToSingleToken("==", TokenType.IsEqual, "==");
        checkParsingToSingleToken("=", TokenType.Assignment, "=");

        checkParsingToSingleToken("?", TokenType.QuestionMark, "?");
        checkParsingToSingleToken("!", TokenType.ExclamationMark, "!");
    }

    @Test
    public void testSingleTokenNumbers() {
        checkParsingToSingleToken("0", TokenType.Integer, "0");
        checkParsingToSingleToken("12345", TokenType.Integer, "12345");
        checkParsingToSingleToken("0.1", TokenType.Float, "0.1");
        checkParsingToSingleToken("0.", TokenType.Float, "0.");
        checkParsingToSingleToken(".1", TokenType.Float, ".1");
        checkParsingToSingleToken(".1e3", TokenType.Float, ".1e3");
        checkParsingToSingleToken(".1e+3", TokenType.Float, ".1e+3");
        checkParsingToSingleToken("5.e-0", TokenType.Float, "5.e-0");
        checkParsingToSingleToken("1ull", TokenType.Integer, "1ull");
        checkParsingToSingleToken("123.456e-4F", TokenType.Float, "123.456e-4F");
        checkParsingToSingleToken("1.3f", TokenType.Float, "1.3f");
        checkParsingToSingleToken("1.3e+3f", TokenType.Float, "1.3e+3f");
    }

    @Test
    public void testComments() {
        try {
            checkTokenListsEqual(new Tokenizer("//").tokens, new ArrayList<Token>());
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            checkTokenListsEqual(new Tokenizer("/**/").tokens, new ArrayList<Token>());
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            checkTokenListsEqual(new Tokenizer("// some text").tokens, new ArrayList<Token>());
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            checkTokenListsEqual(new Tokenizer("/* some text */").tokens, new ArrayList<Token>());
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            checkTokenListsEqual(new Tokenizer("//\n").tokens, new ArrayList<Token>());
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            checkTokenListsEqual(new Tokenizer("// some text\n").tokens, new ArrayList<Token>());
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            checkTokenListsEqual(new Tokenizer("/* some\ntext */").tokens, new ArrayList<Token>());
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            checkTokenListsEqual(new Tokenizer("/*/ some\ntext */").tokens, new ArrayList<Token>());
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }
    }
}