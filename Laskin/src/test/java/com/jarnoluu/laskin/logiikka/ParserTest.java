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
                new Token(Token.Type.NUMBER, "1"),
                new Token(Token.Type.OPER, "+"),
                new Token(Token.Type.NUMBER, "2"),
                new Token(Token.Type.OPER, "*"),
                new Token(Token.Type.NUMBER, "3")
        );
        
        List<Token> tokens = this.parser.tokenize("1+2*3");
        
        assertThat(tokens, is(expected));
    }
    
    @Test
    public void testTokenize2() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "3"),
                new Token(Token.Type.OPER, "^"),
                new Token(Token.Type.BRACKET_START),
                new Token(Token.Type.NUMBER, "2"),
                new Token(Token.Type.OPER, "+"),
                new Token(Token.Type.NUMBER, "3"),
                new Token(Token.Type.BRACKET_END)
        );
        
        List<Token> tokens = this.parser.tokenize("3^(2+3)");
        
        assertThat(tokens, is(expected));
    }
    
    @Test
    public void testTokenize3() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "2"),
                new Token(Token.Type.OPER, "+"),
                new Token(Token.Type.FUNC, "max"),
                new Token(Token.Type.BRACKET_START),
                new Token(Token.Type.NUMBER, "2"),
                new Token(Token.Type.COMMA),
                new Token(Token.Type.NUMBER, "3"),
                new Token(Token.Type.BRACKET_END),
                new Token(Token.Type.OPER, "-"),
                new Token(Token.Type.NUMBER, "2")
        );
        
        List<Token> tokens = this.parser.tokenize("2+max(2,3)-2");
        
        assertThat(tokens, is(expected));
    }
    
    @Test
    public void testTokenize4() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "-2"),
                new Token(Token.Type.OPER, "+"),
                new Token(Token.Type.BRACKET_START),
                new Token(Token.Type.NUMBER, "-3"),
                new Token(Token.Type.OPER, "-"),
                new Token(Token.Type.NUMBER, "4"),
                new Token(Token.Type.BRACKET_END),
                new Token(Token.Type.OPER, "-"),
                new Token(Token.Type.NUMBER, "5")
        );
        
        List<Token> tokens = this.parser.tokenize("-2+(-3-4)-5");
        
        assertThat(tokens, is(expected));
    }
}
