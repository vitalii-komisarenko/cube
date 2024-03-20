package cube.test;

import cube.Tokenizer;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;


public class TokenizerTest {
    @Test
    public void testEmptyInput() {
        try {
          assertEquals(new Tokenizer("").tokens, new ArrayList<Tokenizer.Token>());
        }
        catch (Tokenizer.TokenizerException e) {
            fail("TokenizerException");
        }
    }
}