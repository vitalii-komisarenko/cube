package cube;

import cube.Tokenizer;
import cube.Tokenizer.Token;
import cube.Tokenizer.TokenType;
import cube.Tokenizer.TokenizerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

        processStringification();
        processTokenConcatenation();
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

        if (macro_type == "#undef") {
            // actual treating of #undef is treated in #define
            tokens.remove(parser_pos); // #undef
            tokens.remove(parser_pos); // macro_name
            if (parser_pos < tokens.size())
                tokens.remove(parser_pos); // \n
            return;
        }

        int macro_beginning_index = parser_pos;

        parser_pos++;
        assertParserNotOutOfBounds();

        if (tokens.get(parser_pos).type != TokenType.Identifier) {
            throw new MacroProcessorException();
        }
        String macro_name = tokens.get(parser_pos).value;

        parser_pos++;

        ArrayList<Token> raw_macro_parameters = new ArrayList<Token>(); // including opening and closing brackets
        if ((parser_pos < tokens.size()) && (tokens.get(parser_pos).type == TokenType.OpeningRoundBracket)) {
            for (; parser_pos < tokens.size(); ++parser_pos) {
                assertParserNotOutOfBounds();
                Token token = tokens.get(parser_pos);
                raw_macro_parameters.add(token);
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
            if (raw_macro_parameters.size() == 0) {
                processMacroWithoutBrackets(macro_name, macro_replacement, macro_beginning_index);
            }
            else {
                ArrayList<Token> macro_parameters = new ArrayList<Token>();
                for (int i = 1; i < raw_macro_parameters.size() - 1; ++i) {
                    Token token = raw_macro_parameters.get(i);
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
        boolean undef_encountered = false;
        for (int i = starting_pos; i < tokens.size(); ++i) {
            Token token = tokens.get(i);
            if ((i < tokens.size() - 1) && token.equals(new Token(TokenType.MacroIdentifier, "#undef")) && tokens.get(i+1).equals(new Token(TokenType.Identifier, macro_name))) {
                undef_encountered = true;
                i += 2;
                continue;
            }
            if (!undef_encountered && token.equals(new Token(TokenType.Identifier, macro_name))) {
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

        if (token.type != TokenType.OpeningRoundBracket) {
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

    public static ArrayList<Token> replaceAllSingleTokensWithMultipleTokens(ArrayList<Token> inputList, Token tokenToReplace, ArrayList<Token> replacement) {
        ArrayList<Token> outputList = new ArrayList<Token>();
        for (int i = 0; i < inputList.size(); ++i) {
            Token token = inputList.get(i);
            if (token.equals(tokenToReplace)) {
                outputList.addAll(replacement);
            }
            else {
                outputList.add(token);
            }
        }
        return outputList;
    }

    void replaceMacroAtPosition(int starting_pos,
                                int ending_pos,
                                ArrayList<Token> macroPattern,
                                HashMap<Token, ArrayList<Token>> parametersMapping) {
        List<Token> before = tokens.subList(0, starting_pos);
        List<Token> after = tokens.subList(ending_pos, tokens.size());

        ArrayList<Token> after_replacement = new ArrayList<Token>();
        for (Token token : macroPattern) {
            ArrayList<Token> value = parametersMapping.get(token);
            if (value == null) {
                after_replacement.add(token);
            }
            else {
                after_replacement.addAll(value);
            }
        }

        tokens = new ArrayList<Token>();
        tokens.addAll(before);
        tokens.addAll(after_replacement);
        parser_pos = tokens.size();
        tokens.addAll(after);
    }

    void processMacroWithBrackets(String macro_name,
                                  ArrayList<Token> macro_parameters,
                                  ArrayList<Token> macro_replacement,
                                  int starting_pos) throws MacroProcessorException {
        for (parser_pos = starting_pos; parser_pos < tokens.size(); ++parser_pos) {
            Token token = tokens.get(parser_pos);

            if ((parser_pos < tokens.size() - 1) && token.equals(new Token(TokenType.MacroIdentifier, "#undef")) && tokens.get(parser_pos+1).equals(new Token(TokenType.Identifier, macro_name))) {
                tokens.remove(parser_pos); // #undef
                tokens.remove(parser_pos); // macro_name
                if (parser_pos < tokens.size())
                    tokens.remove(parser_pos); // \n
                break;
            }

            if ((token.type != TokenType.Identifier) || (!token.value.equals(macro_name))) {
                continue;
            }

            int macro_replacement_start = parser_pos;
            int macro_replacement_size = macro_replacement.size();
            parser_pos++; // skip macro name

            // check the opening bracket
            if (tokens.get(parser_pos++).type != TokenType.OpeningRoundBracket) {
                throw new MacroProcessorException();
            }

            HashMap<Token, ArrayList<Token>> caller_macro_params = new HashMap<Token, ArrayList<Token>>();

            for (int i = 0; i < macro_parameters.size(); ++i) {
                Token token_to_replace = macro_parameters.get(i);
                ArrayList<Token> replacement = readMacroParameter();
                caller_macro_params.put(token_to_replace, replacement);

                parser_pos++; // skip comma or closing bracket
            }

            if (macro_parameters.size() == 0) {
                parser_pos++; // closing bracket
            }

            replaceMacroAtPosition(macro_replacement_start, parser_pos, macro_replacement, caller_macro_params);

            parser_pos--;
        }
    }

    void processStringification() {
        for (int i = 0; i < tokens.size() - 1; ++i) {
            if (tokens.get(i).type == TokenType.Octothorp) {
                tokens.get(i).type = TokenType.String;
                tokens.get(i).value = tokens.get(i+1).value;
                tokens.remove(i+1);
            }
        }
    }

    void processTokenConcatenation() {
        for (int i = 1; i < tokens.size() - 1; ++i) {
            if (tokens.get(i).type == TokenType.DoubleOctothorp) {
                tokens.get(i).type = TokenType.Identifier;
                tokens.get(i).value = tokens.get(i-1).value + tokens.get(i+1).value;
                tokens.remove(i+1);
                tokens.remove(i-1);
                i--;
            }
        }
    }

    private int parser_pos;
    public ArrayList<Token> tokens;
    private HashMap<String, ArrayList<Token>> macros_without_brackets;
}