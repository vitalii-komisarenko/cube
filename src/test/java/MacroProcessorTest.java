package cube.test;

import cube.test.TokenizerTest;
import cube.Tokenizer.Token;
import cube.Tokenizer.TokenType;
import cube.Tokenizer.TokenizerException;
import cube.MacroProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang3.builder.EqualsBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

public class MacroProcessorTest {
    @Test
    public void testEmptyInput() {
        ArrayList<Token> input_tokens = new ArrayList<Token>();
        ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
        TokenizerTest.checkTokenListsEqual(input_tokens, output_tokens);
    }

    @Test
    public void testHelloWorld() {
        ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.Identifier, "void"),
            new Token(TokenType.Identifier, "main"),
            new Token(TokenType.OpeningCurlyBracket, "{"),
            new Token(TokenType.ClosingCurlyBracket, "}")
        ));
        ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
        TokenizerTest.checkTokenListsEqual(input_tokens, output_tokens);
    }

    @Test
    public void testBuiltInMacroReplacement() {
        ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.Identifier, "__unix__")
        ));
        
        ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.Integer, "1")
        ));

        ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
        TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
    }

    @Test
    public void testBuiltInMacroReplacement2() {
        ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.Identifier, "__unix__"),
            new Token(TokenType.Identifier, "__unix__")
        ));
        
        ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.Integer, "1"),
            new Token(TokenType.Integer, "1")
        ));

        ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
        TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
    }

    @Test
    public void testBuiltInMacroReplacement3() {
        ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.Identifier, "__unix__"),
            new Token(TokenType.Identifier, "unix")
        ));
        
        ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.Integer, "1"),
            new Token(TokenType.Integer, "1")
        ));

        ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
        TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
    }

}