package cube;

import java.util.ArrayList;


public class Tokenizer {
    public enum TokenType {
        MacroIdentifier,      // #include, #ifdef
        Octothorp,            // #
        DoubleOctothorp,      // ##
        NewLine,              // \n

        Indentifier,          // int, i, return etc.
        Char,                 // 'a'
        String,               // "string"
        Integer,              // 123
        Float,                // 1.23

        OpenRoundBracket,     // (
        ClosingRoundBracket,  // )
        OpeningSquareBracket, // [
        ClosingSquareBracket, // ]
        OpeningCurlyBracket,  // {
        ClosingCurlyBracket,  // }

        Plus,                 // +
        Minus,                // -
        Asterisk,             // *
        Slash,                // /
        Ampersand,            // &
        Comma,                // ,
        Less,                 // <
        LessOrEqual,          // <=
        Assignment,           // =
        IsEqual,              // ==
        More,                 // >
        MoreOrEqual,          // >=
    }

    public static class Token {
        TokenType type;
        String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    public class TokenizerException extends Exception {
        public TokenizerException() {
            super("Tokenizer Exception");
        }
    }

    public Tokenizer(String sourceCode) throws TokenizerException {
        while (parser_pos < sourceCode.length()) {
            if (sourceCode.charAt(parser_pos) == '\n') {
                tokens.add(new Token(TokenType.NewLine, "\n"));
                parser_pos++;
                continue;
            }

            throw new TokenizerException();
        }
    }

    public ArrayList<Token> tokens = new ArrayList<Token>();

    private int parser_pos;
}