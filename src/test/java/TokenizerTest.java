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
        try {
            assertTrue(EqualsBuilder.reflectionEquals(
                new Tokenizer("'a'").tokens,
                new ArrayList<>(Arrays.asList(
                    new Token(TokenType.Char, "a")
                ))));
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            assertTrue(EqualsBuilder.reflectionEquals(
                new Tokenizer("'\\''").tokens,
                new ArrayList<>(Arrays.asList(
                    new Token(TokenType.Char, "'")
                ))));
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        try {
            assertTrue(EqualsBuilder.reflectionEquals(
                new Tokenizer("'\\\\'").tokens,
                new ArrayList<>(Arrays.asList(
                    new Token(TokenType.Char, "\\")
                ))));
        }
        catch (TokenizerException e) {
            fail("TokenizerException");
        }

        assertThrows(TokenizerException.class, () -> new Tokenizer("'"));
        assertThrows(TokenizerException.class, () -> new Tokenizer("''"));
        assertThrows(TokenizerException.class, () -> new Tokenizer("'a"));
        assertThrows(TokenizerException.class, () -> new Tokenizer("'\\"));
        assertThrows(TokenizerException.class, () -> new Tokenizer("'\\a"));
    }
}