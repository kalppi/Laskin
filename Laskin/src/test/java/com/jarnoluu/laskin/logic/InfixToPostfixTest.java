package com.jarnoluu.laskin.logic;

import com.jarnoluu.laskin.logic.InfixToPostfix;
import com.jarnoluu.laskin.logic.Parser;
import com.jarnoluu.laskin.logic.Token;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class InfixToPostfixTest {
    public InfixToPostfixTest() throws Exception {

    }
    
    @Test
    public void testInfixToPostfix() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "12"),
                new Token(Token.Type.NUMBER, "10"),
                new Token(Token.Type.OPER, "+")
        );
        
        LinkedList<Token> tokens = Parser.tokenize("12+10");
        LinkedList<Token> postfix = InfixToPostfix.transform(tokens);

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
        
        LinkedList<Token> tokens = Parser.tokenize("3+4*2/(1-5)^2^3");
        LinkedList<Token> postfix = InfixToPostfix.transform(tokens);
        
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
                
        LinkedList<Token> tokens = Parser.tokenize("sin ( max ( 2, 3 ) / 3 * 3)");
        LinkedList<Token> postfix = InfixToPostfix.transform(tokens);

        assertThat(postfix, is(expected));
    }
}
