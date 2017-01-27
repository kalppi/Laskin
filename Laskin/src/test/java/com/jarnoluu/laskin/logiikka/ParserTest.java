/*
 * Copyright (C) 2017 Jarno Luukkonen <luukkonen.jarno@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jarnoluu.laskin.logiikka;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class ParserTest {
    private final Parser parser;
    
    public ParserTest() {
        parser = new Parser();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test(expected=LaskinParseException.class)
    public void testWrongNumberOfBrackets() throws Exception {
        this.parser.tokenize("1+2*(4+4))");
    }
    
    @Test(expected=LaskinParseException.class)
    public void testWrongOrderOfBrackets() throws Exception {
        this.parser.tokenize("1+2*)4+4(");
    }
    
    @Test
    public void testRightNumberOfTokens() throws Exception {
        List<Token> tokens = parser.tokenize("1 *  2+   4/5+(6 - 7)");
        assertEquals(tokens.size(), 13);
    }
    
    @Test(expected=LaskinParseException.class)
    public void testUnknownCharacters() throws Exception {
        this.parser.tokenize("1+2+_34");
    }
    
    @Test(expected=LaskinParseException.class)
    public void testUnknownCharacters2() throws Exception {
        this.parser.tokenize("1+2'+34");
    }
    
    @Test
    public void testWhitespace() throws Exception {
        this.parser.tokenize("1   +  2      * 4");
    }
    
    @Test
    public void testTokenize() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(TokenType.NUMBER, "1"),
                new Token(TokenType.OPER, "+"),
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.OPER, "*"),
                new Token(TokenType.NUMBER, "3")
        );
        
        List<Token> tokens = this.parser.tokenize("1+2*3");
        
        assertThat(tokens, is(expected));
    }
    
    @Test
    public void testTokenize2() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.OPER, "^"),
                new Token(TokenType.BRACKET_START),
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.OPER, "+"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.BRACKET_END)
        );
        
        List<Token> tokens = this.parser.tokenize("3^(2+3)");
        
        assertThat(tokens, is(expected));
    }
}
