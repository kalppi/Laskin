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
public class CalculatorTest {
    private final Calculator calculator;
    private final Parser parser;
    
    public CalculatorTest() {
        this.calculator = new Calculator();
        this.parser = new Parser();
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
    
    @Test
    public void testInfixToPostfix() throws Exception {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "12"),
                new Token(Token.Type.NUMBER, "10"),
                new Token(Token.Type.OPER, "+")
        );
        
        List<Token> tokens = this.parser.tokenize("12+10");
        List<Token> postfix = this.calculator.infixToPostfix(tokens);

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
        
        List<Token> tokens = this.parser.tokenize("3+4*2/(1-5)^2^3");
        List<Token> postfix = this.calculator.infixToPostfix(tokens);

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
                
        List<Token> tokens = this.parser.tokenize("sin ( max ( 2, 3 ) / 3 * 3)");
        List<Token> postfix = this.calculator.infixToPostfix(tokens);

        assertThat(postfix, is(expected));
    }
    
    @Test
    public void testCalculation() throws Exception {
        double val = this.calculator.calculate("5*15+2");

        System.out.println(val);

        assertEquals(77.0, val, 0.00001);
    }
    
    @Test
    public void testCalculation2() throws Exception {
        double val = this.calculator.calculate("10+(2^10)-2");

        assertEquals(1032, val, 0.00001);
    }
    
    @Test
    public void testCalculation3() throws Exception {
        double val = this.calculator.calculate("10+(2^10-4)");

        assertEquals(1030, val, 0.00001);
    }
    
    @Test
    public void testCalculation4() throws Exception {
        double val = this.calculator.calculate("2+max(2,3)");

        assertEquals(5, val, 0.00001);
    }
    
    @Test
    public void testCalculation5() throws Exception {
        double val = this.calculator.calculate(" 1+(2+   -2)");

        assertEquals(1, val, 0.00001);
    }
    
    @Test
    public void testCalculation6() throws Exception {
        double val = this.calculator.calculate("11%3+2");

        assertEquals(4, val, 0.00001);
    }
}
