package cube;

import cube.Tokenizer;
import cube.Tokenizer.Token;
import cube.Tokenizer.TokenType;
import cube.Tokenizer.TokenizerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MacroProcessor {
    private class AllMacrosProcessed extends Exception {
        public AllMacrosProcessed() {
            super("");
        }
    }

    public class MacroProcessorException extends Exception {
        public MacroProcessorException() {
            super("");
        }
    }

    private void assertParserNotOutOfBounds() throws MacroProcessorException {
        if (parser_pos >= tokens.size()) {
            throw new MacroProcessorException();
        }
    }

    public MacroProcessor(ArrayList<Token> tokens) throws MacroProcessorException {
        this.parser_pos = 0;
        this.tokens = tokens;
        this.macros_without_brackets = new HashMap<String, ArrayList<Token>>();
        this.macros_without_brackets.put("__unix__", new ArrayList<Token>(Arrays.asList(new Token(TokenType.Integer, "1"))));
        this.macros_without_brackets.put("unix", new ArrayList<Token>(Arrays.asList(new Token(TokenType.Integer, "1"))));

        for (Map.Entry<String, ArrayList<Token>> entry : this.macros_without_brackets.entrySet()) {
            processMacroWithoutBrackets(entry.getKey(), entry.getValue(), 0);
        }

        try {
            while(true) {
                processOneMacro();
            }
        }
        catch (AllMacrosProcessed e) {
        }
    }

    void processOneMacro() throws MacroProcessorException, AllMacrosProcessed {
        for (parser_pos = 0; parser_pos < tokens.size(); ++parser_pos) {
            if (tokens.get(parser_pos).type == TokenType.MacroIdentifier) {
                break;
            }
        }

        if (parser_pos == tokens.size()) {
            throw new AllMacrosProcessed();
        }

        String macro_type = tokens.get(parser_pos).value;
        int macro_beginning_index = parser_pos;

        parser_pos++;
        assertParserNotOutOfBounds();

        if (tokens.get(parser_pos).type != TokenType.Identifier) {
            throw new MacroProcessorException();
        }
        String macro_name = tokens.get(parser_pos).value;

        parser_pos++;

        ArrayList<Token> macro_paramaters = new ArrayList<Token>(); // including opening and closing brackets
        if ((parser_pos < tokens.size()) && (tokens.get(parser_pos).type == TokenType.OpenRoundBracket)) {
            for (; parser_pos < tokens.size(); ++parser_pos) {
                assertParserNotOutOfBounds();
                Token token = tokens.get(parser_pos);
                macro_paramaters.add(token);
                if (token.type == TokenType.ClosingRoundBracket) {
                    break;
                }
                if (token.type == TokenType.NewLine) {
                    throw new MacroProcessorException();
                }
            }

            assertParserNotOutOfBounds();
            parser_pos++;
        }

        ArrayList<Token> macro_replacement = new ArrayList<Token>();
        for (; parser_pos < tokens.size(); ++parser_pos) {
            Token token = tokens.get(parser_pos);
            if (token.type == TokenType.NewLine) {
                ++parser_pos;
                break;
            }
            macro_replacement.add(token);
        }

        tokens.subList(macro_beginning_index, parser_pos).clear();

        if (macro_type == "#define") {
            processMacroWithoutBrackets(macro_name, macro_replacement, macro_beginning_index);
        }
        else {
            throw new MacroProcessorException();
        }
    }

    void processMacroWithoutBrackets(String macro_name, ArrayList<Token> macro_replacement, int starting_pos) {
        ArrayList<Token> new_tokens = new ArrayList<Token>();
        for (int i = 0; (i < starting_pos) && (i < tokens.size()); ++i) {
            new_tokens.add(tokens.get(i));
        }
        for (int i = starting_pos; i < tokens.size(); ++i) {
            Token token = tokens.get(i);
            if (token.equals(new Token(TokenType.Identifier, macro_name))) {
                new_tokens.addAll(macro_replacement);
            }
            else {
                new_tokens.add(token);
            }
        }
        tokens = new_tokens;
    }

    private int parser_pos;
    public ArrayList<Token> tokens;
    private HashMap<String, ArrayList<Token>> macros_without_brackets;
}