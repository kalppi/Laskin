package com.jarnoluu.laskin.logiikka;

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
        assertEquals(new Token(Token.Type.NUMBER, "2").toString(), "NUMBER(2)");
    }
    
    @Test
    public void testToString2() {
        assertEquals(new Token(Token.Type.BRACKET_START).toString(), "BRACKET_START");
    }
    
    @Test
    public void testEquals1() {
        assertTrue(new Token(Token.Type.BRACKET_START).equals(new Token(Token.Type.BRACKET_START)));
    }
    
    @Test
    public void testEquals2() {
        assertFalse(new Token(Token.Type.BRACKET_START).equals(null));
    }
    
    @Test
    public void testEquals3() {
        assertFalse(new Token(Token.Type.BRACKET_START).equals("("));
    }
    
    @Test
    public void testEquals4() {
        Token t = new Token(Token.Type.BRACKET_START);
        assertTrue(t.equals(t));
    }
}
