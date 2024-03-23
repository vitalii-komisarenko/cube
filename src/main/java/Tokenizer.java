package cube;

import java.util.ArrayList;


public class Tokenizer {
    public enum TokenType {
        MacroIdentifier,      // #include, #ifdef
        Octothorp,            // #
        DoubleOctothorp,      // ##
        NewLine,              // \n

        Identifier,           // int, i, return etc.
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
        public TokenType type;
        public String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Token)) {
                return false;
            }
            Token other = (Token) object;
            return this.type.equals(other.type) && this.value.equals(other.value);
        }
    }

    public class TokenizerException extends Exception {
        public TokenizerException() {
            super("Tokenizer Exception");
        }
    }

    public Tokenizer(String sourceCode) throws TokenizerException {
        this.sourceCode = sourceCode;

        while (parser_pos < sourceCode.length()) {
            char ch = sourceCode.charAt(parser_pos);

            if (ch == '\n') {
                tokens.add(new Token(TokenType.NewLine, "\n"));
                parser_pos++;
                continue;
            }

            if (ch == '\'') {
                this.tokenizeChar();
                continue;
            }

            if (ch == '"') {
                this.tokenizeString();
                continue;
            }

            if (ch == '#') {
                parser_pos++;
                if ((parser_pos < sourceCode.length()) && sourceCode.charAt(parser_pos) == '#') {
                    parser_pos++;
                    tokens.add(new Token(TokenType.DoubleOctothorp, "##"));
                }
                else if ((parser_pos < sourceCode.length()) && isAlphaUnderscore(sourceCode.charAt(parser_pos))) {
                    tokens.add(new Token(TokenType.MacroIdentifier, "#" + readAlphaNumUnderscoreSequence()));
                }
                else {
                    tokens.add(new Token(TokenType.Octothorp, "#"));
                }
                continue;
            }

            if (ch == '(') {
                tokens.add(new Token(TokenType.OpenRoundBracket, "("));
                parser_pos++;
                continue;
            }

            if (ch == ')') {
                tokens.add(new Token(TokenType.ClosingRoundBracket, ")"));
                parser_pos++;
                continue;
            }

            if (ch == '[') {
                tokens.add(new Token(TokenType.OpeningSquareBracket, "["));
                parser_pos++;
                continue;
            }

            if (ch == ']') {
                tokens.add(new Token(TokenType.ClosingSquareBracket, "]"));
                parser_pos++;
                continue;
            }

            if (ch == '{') {
                tokens.add(new Token(TokenType.OpeningCurlyBracket, "{"));
                parser_pos++;
                continue;
            }

            if (ch == '}') {
                tokens.add(new Token(TokenType.ClosingCurlyBracket, "}"));
                parser_pos++;
                continue;
            }

            if (isAlphaUnderscore(ch)) {
                tokens.add(new Token(TokenType.Identifier, readAlphaNumUnderscoreSequence()));
                continue;
            }

            throw new TokenizerException();
        }
    }

    public ArrayList<Token> tokens = new ArrayList<Token>();

    private int parser_pos;
    private String sourceCode;

    private void assertParserNotOutOfBounds() throws TokenizerException {
        if (parser_pos >= sourceCode.length()) {
            throw new TokenizerException();
        }
    }

    private String readBackslashedOrOrdinaryChar() throws TokenizerException {
        assertParserNotOutOfBounds();

        if (sourceCode.charAt(parser_pos) != '\\') {
            String value = new Character(sourceCode.charAt(parser_pos)).toString();
            parser_pos++;
            return value;
        }

        // Parse backslash-escaped chars like '\n'
        parser_pos++; // skip backslash
        assertParserNotOutOfBounds();

        char escaped_symbol = sourceCode.charAt(parser_pos);
        parser_pos++;

        switch (escaped_symbol) {
            case '\\':
                return "\\";
            case '\'':
                return "'";
            case '"':
                return "\"";
            case 'n':
                return "\n";
            case 'r':
                return "\r";
            case 't':
                return "\t";
            default:
                throw new TokenizerException();
        }
    }

    private void tokenizeChar() throws TokenizerException {
        parser_pos++;  // skip opening '
        String value = readBackslashedOrOrdinaryChar();

        assertParserNotOutOfBounds();

        if (sourceCode.charAt(parser_pos) != '\'') {
            throw new TokenizerException();
        }
        parser_pos++; // skip closing '
        tokens.add(new Token(TokenType.Char, value));
    }

    private void tokenizeString() throws TokenizerException {
        parser_pos++;  // skip opening "
        String value = "";

        while (true) {
            assertParserNotOutOfBounds();

            if (sourceCode.charAt(parser_pos) == '"') {
                parser_pos++; // skip closing "
                tokens.add(new Token(TokenType.String, value));
                return;
            }

            value += readBackslashedOrOrdinaryChar();
        }
    }

    private boolean isNum(char ch) {
        return (ch >= '0') && (ch <= '9');
    }

    private boolean isAlphaUnderscore(char ch) {
        if ((ch >= 'a') && (ch <= 'z')) {
            return true;
        }

        if ((ch >= 'A') && (ch <= 'Z')) {
            return true;
        }

        return ch == '_';
    }

    private boolean isAlphaNumUnderscore(char ch) {
        return isNum(ch) || isAlphaUnderscore(ch);
    }

    private String readAlphaNumUnderscoreSequence() {
        String res = "";
        while (parser_pos < sourceCode.length()) {
            char ch = sourceCode.charAt(parser_pos);
            if (!isAlphaNumUnderscore(ch)) {
                break;
            }
            res += ch;
            parser_pos++;
        }
        return res;
    }
}