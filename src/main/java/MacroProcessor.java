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

        ArrayList<Token> raw_macro_paramaters = new ArrayList<Token>(); // including opening and closing brackets
        if ((parser_pos < tokens.size()) && (tokens.get(parser_pos).type == TokenType.OpenRoundBracket)) {
            for (; parser_pos < tokens.size(); ++parser_pos) {
                assertParserNotOutOfBounds();
                Token token = tokens.get(parser_pos);
                raw_macro_paramaters.add(token);
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
        parser_pos = macro_beginning_index;

        if (macro_type == "#define") {
            if (raw_macro_paramaters.size() == 0) {
                processMacroWithoutBrackets(macro_name, macro_replacement, macro_beginning_index);
            }
            else {
                ArrayList<Token> macro_parameters = new ArrayList<Token>();
                for (int i = 1; i < raw_macro_paramaters.size() - 1; ++i) {
                    Token token = raw_macro_paramaters.get(i);
                    if (i % 2 == 0) {
                        if (token.type != TokenType.Comma) {
                            throw new MacroProcessorException();
                        }
                    }
                    else {
                        macro_parameters.add(token);
                    }
                }
                processMacroWithBrackets(macro_name, macro_parameters, macro_replacement, macro_beginning_index);
            }
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

    ArrayList<Token> readMacroParameter() throws MacroProcessorException {
        ArrayList<Token> res = new ArrayList<Token>();
        assertParserNotOutOfBounds();
        Token token = tokens.get(parser_pos++);
        res.add(token);

        if (token.type != TokenType.OpenRoundBracket) {
            return res;
        }

        for(; parser_pos < tokens.size(); ++parser_pos) {
            res.addAll(readMacroParameter());
            if (tokens.get(parser_pos).type == TokenType.ClosingRoundBracket) {
                return res;
            }
        }

        throw new MacroProcessorException();
    }

    void processMacroWithBrackets(String macro_name,
                                  ArrayList<Token> macro_paramaters,
                                  ArrayList<Token> macro_replacement,
                                  int starting_pos) throws MacroProcessorException {
        ArrayList<Token> new_tokens = new ArrayList<Token>();
        for (int parser_pos = 0; (parser_pos < starting_pos) && (parser_pos < tokens.size()); ++parser_pos) {
            new_tokens.add(tokens.get(parser_pos));
        }

        for (; parser_pos + macro_replacement.size() < tokens.size(); ++parser_pos) {
            Token token = tokens.get(parser_pos);
            if ((token.type != TokenType.Identifier) || (!token.value.equals(macro_name))) {
                continue;
            }

            int macro_replacement_start = parser_pos;
            parser_pos++; // skip macro name

            // check the opening bracket
            if (tokens.get(parser_pos++).type != TokenType.OpenRoundBracket) {
                throw new MacroProcessorException();
            }

            ArrayList<ArrayList<Token>> caller_macro_params = new ArrayList<ArrayList<Token>>();

            for (int i = 0; i < macro_paramaters.size() - 2; ++i) {
                caller_macro_params.add(readMacroParameter());
                if ((i != macro_paramaters.size() - 1) && (tokens.get(parser_pos++ + 1).type != TokenType.Comma)) {
                    throw new MacroProcessorException();
                }
            }

            // check the closing bracket
            if (tokens.get(parser_pos + macro_paramaters.size() - 2).type != TokenType.OpenRoundBracket) {
                throw new MacroProcessorException();
            }
        }

        tokens = new_tokens;
    }

    private int parser_pos;
    public ArrayList<Token> tokens;
    private HashMap<String, ArrayList<Token>> macros_without_brackets;
}