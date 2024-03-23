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
    }
}