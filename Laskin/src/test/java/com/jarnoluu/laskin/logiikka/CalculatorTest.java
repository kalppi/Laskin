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
    public void testInfixToPostfix() {
        List<Token> expected = Arrays.asList(
                new Token(Token.Type.NUMBER, "12"),
                new Token(Token.Type.NUMBER, "10"),
                new Token(Token.Type.OPER, "+")
        );
        try {
            List<Token> tokens = this.parser.tokenize("12+10");
            List<Token> postfix = this.calculator.infixToPostfix(tokens);
            
            assertThat(postfix, is(expected));
        } catch(Exception e) {
        
        }
    }
    
    @Test
    public void testInfixToPostfix2() {
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
        try {
            List<Token> tokens = this.parser.tokenize("3+4*2/(1-5)^2^3");
            List<Token> postfix = this.calculator.infixToPostfix(tokens);
            
            assertThat(postfix, is(expected));
        } catch(Exception e) {
        
        }
    }
    
    @Test
    public void testCalculation() {
        try {
            double val = this.calculator.calculate("5*15+2");
            
            System.out.println(val);
            
            assertEquals(77.0, val, 0.00001);
        } catch(Exception e) {
        
        }
    }
    
    @Test
    public void testCalculation2() {
        try {
            double val = this.calculator.calculate("10+(2^10)");
            
            assertEquals(1034, val, 0.00001);
        } catch(Exception e) {
        
        }
    }
    
    @Test
    public void testCalculation3() {
        try {
            double val = this.calculator.calculate("10+(2^10-4)");
            
            assertEquals(1030, val, 0.00001);
        } catch(Exception e) {
        
        }
    }
}
