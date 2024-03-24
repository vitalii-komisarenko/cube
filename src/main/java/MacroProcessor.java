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

    public MacroProcessor(ArrayList<Token> tokens) {
        this.parser_pos = 0;
        this.tokens = tokens;
        this.macros_without_brackets = new HashMap<String, ArrayList<Token>>();
        this.macros_without_brackets.put("__unix__", new ArrayList<Token>(Arrays.asList(new Token(TokenType.Integer, "1"))));
        this.macros_without_brackets.put("unix", new ArrayList<Token>(Arrays.asList(new Token(TokenType.Integer, "1"))));

        for (Map.Entry<String, ArrayList<Token>> entry : this.macros_without_brackets.entrySet()) {
            processMacroWithoutBrackets(entry.getKey(), entry.getValue(), 0);
        }
    }

    void processMacroWithoutBrackets(String macro_name, ArrayList<Token> macro_replacement, int starting_pos) {
        ArrayList<Token> new_tokens = new ArrayList<Token>();
        for (int i = 0; i < starting_pos; ++i) {
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