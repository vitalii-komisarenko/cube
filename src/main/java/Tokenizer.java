package cube;

import java.util.ArrayList;
import java.util.Objects;


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

        OpeningRoundBracket,  // (
        ClosingRoundBracket,  // )
        OpeningSquareBracket, // [
        ClosingSquareBracket, // ]
        OpeningCurlyBracket,  // {
        ClosingCurlyBracket,  // }

        Plus,                 // +
        Minus,                // -
        Asterisk,             // *
        Slash,                // /
        Modulo,               // %
        AddAssign,            // +=
        SubstractAssign,      // -=
        MultipleAssign,       // *=
        DivideAssign,         // /=
        ModuloAssign,         // %=
        Increment,            // ++
        Decrement,            // --
        Ampersand,            // &
        Comma,                // ,
        Colon,                // :
        Semicolon,            // ;
        Less,                 // <
        LessOrEqual,          // <=
        Assignment,           // =
        IsEqual,              // ==
        More,                 // >
        MoreOrEqual,          // >=
        ShiftLeft,            // <<
        ShiftRight,           // >>
        PointerDereference,   // ->
        QuestionMark,         // ?
        ExclamationMark,      // !
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

        @Override
        public int hashCode() {
            return Objects.hash(type, value);
        }

        @Override
        public String toString() {
            return "Token [ " + type + " " + value + " ]";
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
                tokens.add(new Token(TokenType.OpeningRoundBracket, "("));
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

            if (tokenizeIfPrefix("++", TokenType.Increment))
                continue;

            if (tokenizeIfPrefix("+=", TokenType.AddAssign))
                continue;

            if (tokenizeIfPrefix("+", TokenType.Plus))
                continue;

            if (tokenizeIfPrefix("--", TokenType.Decrement))
                continue;

            if (tokenizeIfPrefix("-=", TokenType.SubstractAssign))
                continue;

            if (tokenizeIfPrefix("->", TokenType.PointerDereference))
                continue;

            if (tokenizeIfPrefix("-", TokenType.Minus))
                continue;

            if (tokenizeIfPrefix("*=", TokenType.MultipleAssign))
                continue;

            if (tokenizeIfPrefix("*", TokenType.Asterisk))
                continue;

            if (tokenizeIfPrefix("/=", TokenType.DivideAssign))
                continue;

            if (tokenizeIfPrefix("/", TokenType.Slash))
                continue;

            if (tokenizeIfPrefix("%=", TokenType.Modulo))
                continue;

            if (tokenizeIfPrefix("%", TokenType.ModuloAssign))
                continue;

            if (tokenizeIfPrefix("<<", TokenType.ShiftLeft))
                continue;

            if (tokenizeIfPrefix("<=", TokenType.LessOrEqual))
                continue;

            if (tokenizeIfPrefix("<", TokenType.Less))
                continue;

            if (tokenizeIfPrefix(">>", TokenType.ShiftRight))
                continue;

            if (tokenizeIfPrefix(">=", TokenType.MoreOrEqual))
                continue;

            if (tokenizeIfPrefix(">", TokenType.More))
                continue;

            if (tokenizeIfPrefix("==", TokenType.IsEqual))
                continue;

            if (tokenizeIfPrefix("=", TokenType.Assignment))
                continue;

            if (tokenizeIfPrefix("?", TokenType.QuestionMark))
                continue;

            if (tokenizeIfPrefix("!", TokenType.ExclamationMark))
                continue;

            if (ch == ',') {
                tokens.add(new Token(TokenType.Comma, ","));
                parser_pos++;
                continue;
            }

            if (ch == ':') {
                tokens.add(new Token(TokenType.Colon, ":"));
                parser_pos++;
                continue;
            }

            if (ch == ';') {
                tokens.add(new Token(TokenType.Semicolon, ";"));
                parser_pos++;
                continue;
            }

            if (isNum(ch) || (ch == '.')) {
                tokens.add(readNumberToken());
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

    private boolean tokenizeIfPrefix(String prefix, TokenType type) {
        if (parser_pos + prefix.length() > sourceCode.length()) {
            return false;
        }
        String actual_prefix = sourceCode.substring(parser_pos, parser_pos + prefix.length());
        if (actual_prefix.equals(prefix)) {
            tokens.add(new Token(type, prefix));
            parser_pos += prefix.length();
            return true;
        }
        return false;
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

    private String readNumericSequence() {
        String res = "";
        while (parser_pos < sourceCode.length()) {
            char ch = sourceCode.charAt(parser_pos);
            if (!isNum(ch)) {
                break;
            }
            res += ch;
            parser_pos++;
        }
        return res;
    }

    String readIntegerPartOfNumber() throws TokenizerException {
        /**
         * For number .123 it is empty string
         * For number 123 it is 123
         * For number 123. it is 123
         * For number 123.456 it is 123
         * For number 123e+456 it is 123
         * For number 123.456e+789 it is 123
         */
        char next_char = sourceCode.charAt(parser_pos);
        if (next_char == '.') {
            return "";
        }
        return readNumericSequence();
    }

    String readFractionalPartOfNumber() throws TokenizerException {
        /**
         * For number .123 it is .123
         * For number 123 it is empty string
         * For number 123. it is .
         * For number 123.456 it is .456
         * For number 123e+456 it is empty string
         * For number 123.456e+789 it is .456
         */
        if (parser_pos >= sourceCode.length()) {
            return "";
        }

        char next_char = sourceCode.charAt(parser_pos);
        if (next_char != '.') {
            return "";
        }

        String res = "" + next_char;
        parser_pos++;

        res += readNumericSequence();
        return res;
    }

    String readExponentialPartOfNumber() throws TokenizerException {
        /**
         * Read an exponent part of a number.
         * It can look like:
         *    e123
         *    e+123
         *    e-123
         *    E123
         *    E+123
         *    E-123
         *
         * If the following part does not look like exponent, return empty string
         */
        if (parser_pos >= sourceCode.length()) {
            return "";
        }

        String res = "";

        char next_char = sourceCode.charAt(parser_pos);
        if ((next_char != 'e') && (next_char != 'E')) {
            return "";
        }

        res += next_char;
        parser_pos++;
        assertParserNotOutOfBounds();

        next_char = sourceCode.charAt(parser_pos);
        if ((next_char == '+') || (next_char == '-')) {
            res += next_char;
            parser_pos++;
        }

        String exponent = readNumericSequence();
        if (exponent.equals("")) {
            throw new TokenizerException();
        }
        res += exponent;

        return res;
    }

    String readTypePartOfNumber() throws TokenizerException {
        /** Read type part of number such as u, l, ull, f etc. */
        String type = readAlphaNumUnderscoreSequence();
        if (type.equals(""))
            return type;
        if (type.toLowerCase().equals("u"))
            return type;
        if (type.toLowerCase().equals("ul"))
            return type;
        if (type.toLowerCase().equals("ull"))
            return type;
        if (type.toLowerCase().equals("l"))
            return type;
        if (type.toLowerCase().equals("ll"))
            return type;
        if (type.toLowerCase().equals("f"))
            return type;

        throw new TokenizerException();
    }

    Token readNumberToken() throws TokenizerException {
        String integer_part = readIntegerPartOfNumber();
        String fractional_part = readFractionalPartOfNumber();
        String exponential_part = readExponentialPartOfNumber();
        String explciit_type_part = readTypePartOfNumber();
        TokenType type = (fractional_part == "") ? TokenType.Integer : TokenType.Float;
        return new Token(type, integer_part + fractional_part + exponential_part + explciit_type_part);
    }
}