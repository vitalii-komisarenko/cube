import java.util.ArrayList;


class Tokenizer {
    enum TokenType {
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

    class Token {
        TokenType type;
        String value;
    }

    class TokenizerException extends Exception {
        public TokenizerException() {
            super("Tokenizer Exception");
        }
    }

    public Tokenizer(String sourceCode) throws TokenizerException {
        while (parser_pos < sourceCode.length()) {
            throw new TokenizerException();
        }
    }

    public ArrayList<Token> tokens = new ArrayList<Token>();

    private int parser_pos;
}