package cube.test;

import cube.Tokenizer.Token;
import cube.Tokenizer.TokenType;
import cube.TokenTree;
import cube.TokenTree.TokenTreeNodeType;

import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TokenTreeTest {
    @Test
    public void testZeroTokens() {
        TokenTree tree = new TokenTree(new ArrayList());
        assertEquals(tree.children.size(), 0);
        assertEquals(tree.type, TokenTreeNodeType.TopLevel);
    }

    @Test
    public void testSingleStatementToken() {
        TokenTree tree = new TokenTree(new ArrayList(Arrays.asList(
            new Token(TokenType.Identifier, "void")
        )));
        assertEquals(tree.children.size(), 1);
        assertEquals(tree.children.get(0).children.size(), 0);
        assertEquals(tree.children.get(0).token, new Token(TokenType.Identifier, "void"));
        assertEquals(tree.children.get(0).type, TokenTreeNodeType.Leaf);
    }

    @Test
    public void testTwoStatementTokens() {
        TokenTree tree = new TokenTree(new ArrayList(Arrays.asList(
            new Token(TokenType.Identifier, "void"),
            new Token(TokenType.Identifier, "main")
        )));
        assertEquals(tree.children.size(), 2);
        assertEquals(tree.children.get(0).children.size(), 0);
        assertEquals(tree.children.get(0).token, new Token(TokenType.Identifier, "void"));
        assertEquals(tree.children.get(1).children.size(), 0);
        assertEquals(tree.children.get(1).token, new Token(TokenType.Identifier, "main"));
    }

    @Test
    public void testEmptyRoundBrackets() {
        TokenTree tree = new TokenTree(new ArrayList(Arrays.asList(
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.ClosingRoundBracket, ")")
        )));

        assertEquals(tree.children.size(), 1);
        assertEquals(tree.children.get(0).children.size(), 0);
        assertEquals(tree.children.get(0).type, TokenTreeNodeType.InRoundBrackets);
    }

    @Test
    public void testBasicHelloWorld() {
        TokenTree tree = new TokenTree(new ArrayList(Arrays.asList(
            new Token(TokenType.Identifier, "void"),
            new Token(TokenType.Identifier, "main"),
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.ClosingRoundBracket, ")"),
            new Token(TokenType.OpeningCurlyBracket, "{"),
            new Token(TokenType.Identifier, "printf"),
            new Token(TokenType.OpeningRoundBracket, "("),
            new Token(TokenType.String, "Hello, World!"),
            new Token(TokenType.ClosingRoundBracket, ")"),
            new Token(TokenType.ClosingCurlyBracket, "}")
        )));

        assertEquals(tree.children.size(), 4);

        assertEquals(tree.children.get(0).children.size(), 0);
        assertEquals(tree.children.get(0).type, TokenTreeNodeType.Leaf);
        assertEquals(tree.children.get(0).token, new Token(TokenType.Identifier, "void"));

        assertEquals(tree.children.get(1).children.size(), 0);
        assertEquals(tree.children.get(1).type, TokenTreeNodeType.Leaf);
        assertEquals(tree.children.get(1).token, new Token(TokenType.Identifier, "main"));

        assertEquals(tree.children.get(2).children.size(), 0);
        assertEquals(tree.children.get(2).type, TokenTreeNodeType.InRoundBrackets);

        assertEquals(tree.children.get(3).children.size(), 2);
        assertEquals(tree.children.get(3).type, TokenTreeNodeType.InCurlyBrackets);

        assertEquals(tree.children.get(3).children.get(0).children.size(), 0);
        assertEquals(tree.children.get(3).children.get(0).type, TokenTreeNodeType.Leaf);
        assertEquals(tree.children.get(3).children.get(0).token, new Token(TokenType.Identifier, "printf"));

        assertEquals(tree.children.get(3).children.get(1).children.size(), 1);
        assertEquals(tree.children.get(3).children.get(1).type, TokenTreeNodeType.InRoundBrackets);

        assertEquals(tree.children.get(3).children.get(1).children.get(0).children.size(), 0);
        assertEquals(tree.children.get(3).children.get(1).children.get(0).type, TokenTreeNodeType.Leaf);
        assertEquals(tree.children.get(3).children.get(1).children.get(0).token, new Token(TokenType.String, "Hello, World!"));
    }
}