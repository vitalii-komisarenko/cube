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
        MoreOrEqual,           // >=
    }
}