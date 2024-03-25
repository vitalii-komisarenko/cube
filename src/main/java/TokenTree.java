package cube;

import cube.Tokenizer.Token;
import cube.Tokenizer.TokenType;

import java.util.ArrayList;

public class TokenTree {
    public enum TokenTreeNodeType {
        TopLevel,
        Leaf,
        InRoundBrackets,
        InSquareBrackets,
        InCurlyBrackets,
    }

    public class TokenTreeException extends Exception {
        public TokenTreeException() {
            super("");
        }
    }

    public TokenTree(ArrayList<Token> tokens, int starting_pos, TokenTreeNodeType type) {
        this.type = type;

        if (type == TokenTreeNodeType.Leaf) {
            token = tokens.get(starting_pos);
            tokens_parsed = 1;
            return;
        }

        if (type == TokenTreeNodeType.TopLevel) {
            int parser_pos = starting_pos;

            while (parser_pos < tokens.size()) {
                TokenType token_type = tokens.get(parser_pos).type;
                TokenTreeNodeType child_type = token_type == TokenType.OpeningRoundBracket ? TokenTreeNodeType.InRoundBrackets :
                                               token_type == TokenType.OpeningSquareBracket ? TokenTreeNodeType.InSquareBrackets :
                                               token_type == TokenType.OpeningCurlyBracket ? TokenTreeNodeType.InCurlyBrackets :
                                               TokenTreeNodeType.Leaf;

                TokenTree new_child = new TokenTree(tokens, parser_pos, child_type);
                children.add(new_child);
                parser_pos += new_child.tokens_parsed;
            }

            return;
        }

        TokenType closing_bracket = type == TokenTreeNodeType.InRoundBrackets ? TokenType.ClosingRoundBracket :
                                    type == TokenTreeNodeType.InSquareBrackets ? TokenType.ClosingSquareBracket :
                                    TokenType.ClosingCurlyBracket;

        int parser_pos = starting_pos;
        parser_pos++; // skip opening bracket

        while (tokens.get(parser_pos).type != closing_bracket) {
            TokenType token_type = tokens.get(parser_pos).type;
            TokenTreeNodeType child_type = token_type == TokenType.OpeningRoundBracket ? TokenTreeNodeType.InRoundBrackets :
                                           token_type == TokenType.OpeningSquareBracket ? TokenTreeNodeType.InSquareBrackets :
                                           token_type == TokenType.OpeningCurlyBracket ? TokenTreeNodeType.InCurlyBrackets :
                                           TokenTreeNodeType.Leaf;

            TokenTree child = new TokenTree(tokens, parser_pos, child_type);
            children.add(child);
            parser_pos += child.tokens_parsed;
        }

        tokens_parsed = parser_pos - starting_pos + 1;
    }

    public TokenTreeNodeType type;
    public Token token;
    public ArrayList<TokenTree> children = new ArrayList<TokenTree>();

    public int tokens_parsed;
}