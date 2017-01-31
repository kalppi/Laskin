package com.jarnoluu.laskin.logiikka;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class PostfixerTest {
    private final Calculator calculator;
    
    public PostfixerTest() {
        this.calculator = new Calculator();
    }
    
    @Test
    public void testInfixToPostfix() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "12"),
                new Token(Token.Type.NUMBER, "10"),
                new Token(Token.Type.OPER, "+")
        );
        
        List<Token> tokens = this.calculator.getParser().tokenize("12+10");
        List<Token> postfix = this.calculator.getPostfixer().infixToPostfix(tokens);

        assertThat(postfix, is(expected));
    }
    
    @Test
    public void testInfixToPostfix2() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "3"),
                new Token(Token.Type.NUMBER, "4"),
                new Token(Token.Type.NUMBER, "2"),
                new Token(Token.Type.OPER, "*"),
                new Token(Token.Type.NUMBER, "1"),
                new Token(Token.Type.NUMBER, "5"),
                new Token(Token.Type.OPER, "-"),
                new Token(Token.Type.NUMBER, "2"),
                new Token(Token.Type.NUMBER, "3"),
                new Token(Token.Type.OPER, "^"),
                new Token(Token.Type.OPER, "^"),
                new Token(Token.Type.OPER, "/"),
                new Token(Token.Type.OPER, "+")
        );
        
        List<Token> tokens = this.calculator.getParser().tokenize("3+4*2/(1-5)^2^3");
        List<Token> postfix = this.calculator.getPostfixer().infixToPostfix(tokens);

        assertThat(postfix, is(expected));
    }
    
    @Test
    public void testInfixToPostfix3() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "2"),
                new Token(Token.Type.NUMBER, "3"),
                new Token(Token.Type.FUNC, "max"),
                new Token(Token.Type.NUMBER, "3"),
                new Token(Token.Type.OPER, "/"),
                new Token(Token.Type.NUMBER, "3"),
                new Token(Token.Type.OPER, "*"),
                new Token(Token.Type.FUNC, "sin")
        );
                
        List<Token> tokens = this.calculator.getParser().tokenize("sin ( max ( 2, 3 ) / 3 * 3)");
        List<Token> postfix = this.calculator.getPostfixer().infixToPostfix(tokens);

        assertThat(postfix, is(expected));
    }
}
