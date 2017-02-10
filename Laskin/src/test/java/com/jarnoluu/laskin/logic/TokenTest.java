package com.jarnoluu.laskin.logic;

import com.jarnoluu.laskin.logic.Token;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class TokenTest {
    public TokenTest() {
    }
    
    @Test
    public void testToString1() {
        assertEquals(new Token(Token.Type.NUMBER, "2").toString(), "2");
    }
    
    @Test
    public void testToString2() {
        assertEquals(new Token(Token.Type.BRACKET_START).toString(), "(");
    }
    
    @Test
    public void testToString3() {
        assertEquals(new Token(Token.Type.SPECIAL, "$").toString(), "$");
    }
    
    @Test
    public void testEquals1() {
        assertTrue(new Token(Token.Type.BRACKET_START).equals(new Token(Token.Type.BRACKET_START)));
    }
    
    @Test
    public void testEquals2() {
        assertFalse(new Token(Token.Type.BRACKET_START).equals("("));
    }
    
    @Test
    public void testJoin() {
        List<Token> tokens = new ArrayList() {{
            add(new Token(Token.Type.BRACKET_START));
            add(new Token(Token.Type.NUMBER, "3"));
            add(new Token(Token.Type.BRACKET_END));
        }};
        
        assertEquals(Token.join("-", tokens), "(-3-)");
    }
    
    @Test
    public void testPrettyStringPi() {
        assertEquals(new Token(Token.Type.SPECIAL, "pi").toPrettyString(), "π");
    }
    
    @Test
    public void testEquals3() {
        Token t = new Token(Token.Type.BRACKET_START);
        assertTrue(t.equals(t));
    }
}
