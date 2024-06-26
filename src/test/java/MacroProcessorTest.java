package cube.test;

import cube.test.TokenizerTest;
import cube.Tokenizer.Token;
import cube.Tokenizer.TokenType;
import cube.Tokenizer.TokenizerException;
import cube.MacroProcessor;
import cube.MacroProcessor.MacroProcessorException;

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
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>();
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(input_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testHelloWorld() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "void"),
                new Token(TokenType.Identifier, "main"),
                new Token(TokenType.OpeningCurlyBracket, "{"),
                new Token(TokenType.ClosingCurlyBracket, "}")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(input_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testBuiltInMacroReplacement() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "__unix__")
            ));

            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Integer, "1")
            ));

            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }

    }

    @Test
    public void testBuiltInMacroReplacement2() {
        try {
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
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }

    }

    @Test
    public void testBuiltInMacroReplacement3() {
        try {
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
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testUnunsedDefineWithoutBrackets() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "sample_macro")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>();
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testUnunsedDefineWithBracketsNoParams() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "sample_macro"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>();
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testUnunsedDefineOneParam() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "sample_macro"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "param1"),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>();
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testUnunsedDefineTwoParams() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "sample_macro"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "param1"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.Identifier, "param2"),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>();
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testDefineAndUseMacroWithoutBrackets() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "sample_macro"),
                new Token(TokenType.Identifier, "main"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "void"),
                new Token(TokenType.Identifier, "sample_macro"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.OpeningCurlyBracket, "{"),
                new Token(TokenType.ClosingCurlyBracket, "}")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "void"),
                new Token(TokenType.Identifier, "main"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.OpeningCurlyBracket, "{"),
                new Token(TokenType.ClosingCurlyBracket, "}")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testReplaceAllSingleTokensWithMultipleTokens_replaceWithZeroTokens() {
        ArrayList<Token> inputList = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.Identifier, "a"),
            new Token(TokenType.ClosingRoundBracket, ")")
        ));
        Token tokenToReplace = new Token(TokenType.Identifier, "a");
        ArrayList<Token> replacement = new ArrayList<Token>(Arrays.asList(
        ));
        ArrayList<Token> expectedList = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.ClosingRoundBracket, ")")
        ));
        ArrayList<Token> actualList = MacroProcessor.replaceAllSingleTokensWithMultipleTokens(inputList, tokenToReplace, replacement);
        TokenizerTest.checkTokenListsEqual(actualList, expectedList);
    }

    @Test
    public void testReplaceAllSingleTokensWithMultipleTokens_replaceWithOneToken() {
        ArrayList<Token> inputList = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.Identifier, "a"),
            new Token(TokenType.ClosingRoundBracket, ")")
        ));
        Token tokenToReplace = new Token(TokenType.Identifier, "a");
        ArrayList<Token> replacement = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.Identifier, "b")
        ));
        ArrayList<Token> expectedList = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.Identifier, "b"),
            new Token(TokenType.ClosingRoundBracket, ")")
        ));
        ArrayList<Token> actualList = MacroProcessor.replaceAllSingleTokensWithMultipleTokens(inputList, tokenToReplace, replacement);
        TokenizerTest.checkTokenListsEqual(actualList, expectedList);
    }

    @Test
    public void testReplaceAllSingleTokensWithMultipleTokens_replaceWithSeveralToken() {
        ArrayList<Token> inputList = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.Identifier, "a"),
            new Token(TokenType.ClosingRoundBracket, ")")
        ));
        Token tokenToReplace = new Token(TokenType.Identifier, "a");
        ArrayList<Token> replacement = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.Identifier, "b"),
            new Token(TokenType.Comma, ","),
            new Token(TokenType.Identifier, "c")
        ));
        ArrayList<Token> expectedList = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.Identifier, "b"),
            new Token(TokenType.Comma, ","),
            new Token(TokenType.Identifier, "c"),
            new Token(TokenType.ClosingRoundBracket, ")")
        ));
        ArrayList<Token> actualList = MacroProcessor.replaceAllSingleTokensWithMultipleTokens(inputList, tokenToReplace, replacement);
        TokenizerTest.checkTokenListsEqual(actualList, expectedList);
    }

    @Test
    public void testReplaceAllSingleTokensWithMultipleTokens_replaceTwoTokensWithOneTokenEach() {
        ArrayList<Token> inputList = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.Identifier, "a"),
            new Token(TokenType.Comma, ","),
            new Token(TokenType.Identifier, "a"),
            new Token(TokenType.ClosingRoundBracket, ")")
        ));
        Token tokenToReplace = new Token(TokenType.Identifier, "a");
        ArrayList<Token> replacement = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.Identifier, "b")
        ));
        ArrayList<Token> expectedList = new ArrayList<Token>(Arrays.asList(
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.Identifier, "b"),
            new Token(TokenType.Comma, ","),
            new Token(TokenType.Identifier, "b"),
            new Token(TokenType.ClosingRoundBracket, ")")
        ));
        ArrayList<Token> actualList = MacroProcessor.replaceAllSingleTokensWithMultipleTokens(inputList, tokenToReplace, replacement);
        TokenizerTest.checkTokenListsEqual(actualList, expectedList);
    }

    @Test
    public void testDefineAndUseMacroWithBracketsWithoutArguemnts() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "sample_macro"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "main"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "void"),
                new Token(TokenType.Identifier, "sample_macro"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.OpeningCurlyBracket, "{"),
                new Token(TokenType.ClosingCurlyBracket, "}")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "void"),
                new Token(TokenType.Identifier, "main"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.OpeningCurlyBracket, "{"),
                new Token(TokenType.ClosingCurlyBracket, "}")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testDefineAndUseMacroWithOneArguemnt() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "print_text"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "text"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "printf"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "text"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "void"),
                new Token(TokenType.Identifier, "main"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.OpeningCurlyBracket, "{"),
                new Token(TokenType.Identifier, "print_text"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.String, "Hello, World!"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Semicolon, ";"),
                new Token(TokenType.ClosingCurlyBracket, "}")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "void"),
                new Token(TokenType.Identifier, "main"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.OpeningCurlyBracket, "{"),
                new Token(TokenType.Identifier, "printf"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.String, "Hello, World!"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Semicolon, ";"),
                new Token(TokenType.ClosingCurlyBracket, "}")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }


    @Test
    public void testDefineAndUseMacroWithTwoArguemnts() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "print_text"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "format"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.Identifier, "text"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "printf"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "format"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.Identifier, "text"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "void"),
                new Token(TokenType.Identifier, "main"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.OpeningCurlyBracket, "{"),
                new Token(TokenType.Identifier, "print_text"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.String, "%s"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.String, "Hello, World!"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Semicolon, ";"),
                new Token(TokenType.ClosingCurlyBracket, "}")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "void"),
                new Token(TokenType.Identifier, "main"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.OpeningCurlyBracket, "{"),
                new Token(TokenType.Identifier, "printf"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.String, "%s"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.String, "Hello, World!"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Semicolon, ";"),
                new Token(TokenType.ClosingCurlyBracket, "}")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testDefineMacroWithBracketsWithoutArguemntsAndUseTwice() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "macro_name"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Integer, "1"),
                new Token(TokenType.Plus, "+"),
                new Token(TokenType.Integer, "2"),
                new Token(TokenType.Semicolon, ";"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "macro_name"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "macro_name"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Integer, "1"),
                new Token(TokenType.Plus, "+"),
                new Token(TokenType.Integer, "2"),
                new Token(TokenType.Semicolon, ";"),
                new Token(TokenType.Integer, "1"),
                new Token(TokenType.Plus, "+"),
                new Token(TokenType.Integer, "2"),
                new Token(TokenType.Semicolon, ";")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testDefineMacroWithBracketsWithOneArguemntsAndUseTwice() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "macro_name"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.Semicolon, ";"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "macro_name"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "macro_name"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.Semicolon, ";"),
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.Semicolon, ";")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testDefineMacroWithBracketsWithOneArguemntsAndUseTwice2() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "macro_name"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.Plus, "+"),
                new Token(TokenType.Integer, "2"),
                new Token(TokenType.Semicolon, ";"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "macro_name"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "macro_name"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.Plus, "+"),
                new Token(TokenType.Integer, "2"),
                new Token(TokenType.Semicolon, ";"),
                new Token(TokenType.Identifier, "param"),
                new Token(TokenType.Plus, "+"),
                new Token(TokenType.Integer, "2"),
                new Token(TokenType.Semicolon, ";")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testOneDefineWithoutBracketsCalledFromTwoPlaces() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Identifier, "b"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Identifier, "a")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "b"),
                new Token(TokenType.Identifier, "b")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testMacroWithParameterCalledAsMacro() {
        /*
        Input:
            #define macro(macro) macro
            macro(macro)
        Output:
            macro
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "macro"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "macro"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "macro"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "macro"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "macro"),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "macro")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testMacroNoBracketsWithUndef() {
        /*
        Input:
            #define a b
            a
            #undef a
            a
        Output:
            b
            a
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Identifier, "b"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.MacroIdentifier, "#undef"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "b"),
                new Token(TokenType.Identifier, "a")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testBuiltInMacroNoBracketsWithUndef() {
        /*
        Input:
            __unix__
            #undef __unix__
            __unix__
        Output:
            1
            __unix__
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "__unix__"),
                new Token(TokenType.MacroIdentifier, "#undef"),
                new Token(TokenType.Identifier, "__unix__"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "__unix__")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Integer, "1"),
                new Token(TokenType.Identifier, "__unix__")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testMacroWithBracketsNoParamsWithUndef() {
        /*
        Input:
            #define a() b
            a()
            #undef a
            a()
        Output:
            b
            a()
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "b"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.MacroIdentifier, "#undef"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "b"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testUndefNonExistingMacro() {
        /*
        Input:
            #undef macro_that_does_not_exist
        Output:
            <none>
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#undef"),
                new Token(TokenType.Identifier, "macro_that_does_not_exist"),
                new Token(TokenType.NewLine, "\n")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(new ArrayList<Token>(), output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testUndefNonExistingMacroNoNewline() {
        /*
        Input:
            #undef macro_that_does_not_exist
        Output:
            <none>
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#undef"),
                new Token(TokenType.Identifier, "macro_that_does_not_exist")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(new ArrayList<Token>(), output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testStringification() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "STR"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "x"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Octothorp, "#"),
                new Token(TokenType.Identifier, "x"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "STR"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "y"),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.String, "y")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testConcatenationTwoTokens() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "CONCAT"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "x"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.Identifier, "y"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "y"),
                new Token(TokenType.DoubleOctothorp, "##"),
                new Token(TokenType.Identifier, "x"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "CONCAT"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.Identifier, "b"),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "ba")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testConcatenationThreeTokens() {
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "CONCAT"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "x"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.Identifier, "y"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.Identifier, "z"),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Identifier, "x"),
                new Token(TokenType.DoubleOctothorp, "##"),
                new Token(TokenType.Identifier, "y"),
                new Token(TokenType.DoubleOctothorp, "##"),
                new Token(TokenType.Identifier, "z"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "CONCAT"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.Identifier, "b"),
                new Token(TokenType.Comma, ","),
                new Token(TokenType.Identifier, "c"),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Identifier, "abc")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testDefineUndefDefineNoBrackets() {
        /*
        Input:
            #define a 3
            a
            #undef a
            #define a 4
            a
        Output:
            3
            4
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Integer, "3"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.MacroIdentifier, "#undef"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Integer, "4"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Integer, "3"),
                new Token(TokenType.Integer, "4")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testDefineUndefUseDefineNoBrackets() {
        /*
        Input:
            #define a 3
            a
            #undef a
            a
            #define a 4
            a
        Output:
            3
            a
            4
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Integer, "3"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.MacroIdentifier, "#undef"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Integer, "4"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Integer, "3"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Integer, "4")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testDefineUndefUseDefineBracketsNoParameters() {
        /*
        Input:
            #define a() 3
            a()
            #undef a
            a()
            #define a() 4
            a()
        Output:
            3
            a()
            4
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Integer, "3"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.MacroIdentifier, "#undef"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Integer, "4"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Integer, "3"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Integer, "4")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testDefineMacroWithBracketsUndefDefineWithoutBrackets() {
        /*
        Input:
            #define a() 3
            a()
            #undef a
            a()
            #define a b
            a()
        Output:
            3
            a()
            b()
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Integer, "3"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.MacroIdentifier, "#undef"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Integer, "b"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Integer, "3"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Integer, "b"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }

    @Test
    public void testDefineMacroWithoutBracketsUndefDefineWithBrackets() {
        /*
        Input:
            #define a x
            a()
            #undef a
            a()
            #define a() y
            a()
        Output:
            x()
            a()
            y
        */
        try {
            ArrayList<Token> input_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Integer, "3"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.MacroIdentifier, "#undef"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.MacroIdentifier, "#define"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.Integer, "b"),
                new Token(TokenType.NewLine, "\n"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> expected_tokens = new ArrayList<Token>(Arrays.asList(
                new Token(TokenType.Integer, "3"),
                new Token(TokenType.Identifier, "a"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")"),
                new Token(TokenType.Integer, "b"),
                new Token(TokenType.OpeningRoundBracket, "("),
                new Token(TokenType.ClosingRoundBracket, ")")
            ));
            ArrayList<Token> output_tokens = new MacroProcessor(input_tokens).tokens;
            TokenizerTest.checkTokenListsEqual(expected_tokens, output_tokens);
        }
        catch (MacroProcessorException e) {
            fail("MacroProcessorException");
        }
    }
}