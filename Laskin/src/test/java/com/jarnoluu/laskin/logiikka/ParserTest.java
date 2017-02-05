package com.jarnoluu.laskin.logiikka;

import com.jarnoluu.laskin.exceptions.LaskinParseException;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class ParserTest {
    public ParserTest() {

    }
    
    @Test(expected=LaskinParseException.class)
    public void testWrongNumberOfBrackets() throws Exception {
        Parser.tokenize("1+2*(4+4))");
    }
    
    @Test(expected=LaskinParseException.class)
    public void testWrongOrderOfBrackets() throws Exception {
        Parser.tokenize("1+2*)4+4(");
    }
    
    @Test
    public void testRightNumberOfTokens() throws Exception {
        List<Token> tokens = Parser.tokenize("1 *  2+   4/5+(6 - 7)");
        assertEquals(tokens.size(), 13);
    }
    
    @Test(expected=LaskinParseException.class)
    public void testUnknownCharacters() throws Exception {
        Parser.tokenize("1+2+_34");
    }
    
    @Test(expected=LaskinParseException.class)
    public void testUnknownCharacters2() throws Exception {
        Parser.tokenize("1+2'+34");
    }
    
    @Test
    public void testWhitespace() throws Exception {
        Parser.tokenize("1   +  2      * 4");
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
        
        List<Token> tokens = Parser.tokenize("1+2*3");
        
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
        
        List<Token> tokens = Parser.tokenize("3^(2+3)");
        
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
        
        List<Token> tokens = Parser.tokenize("2+max(2,3)-2");
        
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
        
        List<Token> tokens = Parser.tokenize("-2+(-3-4)-5");
        
        assertThat(tokens, is(expected));
    }
    
    @Test
    public void testTokenize5() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "-2"),
                new Token(Token.Type.OPER, "-")
        );
        
        List<Token> tokens = Parser.tokenize("-2-");
        
        assertThat(tokens, is(expected));
    }
    
    @Test
    public void testDecimals() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "0.222"),
                new Token(Token.Type.OPER, "+"),
                new Token(Token.Type.NUMBER, "0.333")
        );
        
        List<Token> tokens = Parser.tokenize("0.222+.333");
        
        assertThat(tokens, is(expected));
    }
    
    @Test(expected=LaskinParseException.class)
    public void testTooManyDecimalPoints() throws Exception {
        Parser.tokenize("0.22.2+.333");
    }
    
    @Test(expected=LaskinParseException.class)
    public void testTooManyDecimalPoints2() throws Exception {
        Parser.tokenize("0.222+..333");
    }
    
    @Test
    public void testMalformedCalculations() throws LaskinParseException {
        Parser.tokenize("+232-2/");
        Parser.tokenize("%32+()");
        Parser.tokenize("()-22+(43%)");
    }
}