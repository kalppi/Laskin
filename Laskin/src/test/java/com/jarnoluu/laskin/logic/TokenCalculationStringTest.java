package com.jarnoluu.laskin.logic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class TokenCalculationStringTest {
    private CalculationString s;
    
    public TokenCalculationStringTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        Calculator c = new Calculator();
        this.s = new TokenCalculationString(c);
    }
    
    @Test
    public void testInsert() throws Exception {
        s.insertAtCursor('a');
        s.insertAtCursor('b');
        s.insertAtCursor("?");
        s.insertAtCursor("cd");

        assertEquals(s.toString(), "abcd");
    }
    
    @Test
    public void testNegate() throws Exception {
        s.insertAtCursor("123+321");
        s.negateAtCursor();

        assertEquals(s.toString(), "123+-321");
    }
    
    @Test
    public void testReplace() throws Exception {
        s.insertAtCursor("123+321");
        s.replaceWith("456");

        assertEquals(s.toString(), "456");
    }
    
    @Test
    public void testErase() throws Exception {
        s.insertAtCursor("123+321");
        s.eraseAtCursor();
        s.eraseAtCursor();

        assertEquals(s.toString(), "123+3");
    }
    
    @Test
    public void testError() throws Exception {
        s.insertAtCursor("123+");
        s.calculate();

        assertEquals(s.getError(), true);
    }
    
    @Test
    public void testMoveCursor() throws Exception {
        s.insertAtCursor("123+321-432");
        s.setCursor(2);
        s.eraseAtCursor(2);

        assertEquals(s.toString(), "123+3-432");
    }
}
