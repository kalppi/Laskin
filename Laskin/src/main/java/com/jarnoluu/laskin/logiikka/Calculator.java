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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Calculator {
    private Map<String, Function<Stack<Double>, Double>> functions = new HashMap();
    
    private enum Associativity {
        LEFT, RIGHT
    }
    
    private final Parser parser;
    
    public Calculator() {
        this.parser = new Parser();
        
        this.functions.put("+", (Stack<Double> stack) -> {
            return stack.pop() + stack.pop();
        });
    }
    
    private int getOpPrecedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 2;
            case "*":
            case "/":
                return 3;
            case "^":
                return 4;
            default:
                return 0;
        }
    }
    
    private Associativity getOpAssociativity(String op) {
        if (op.equals("^")) {
            return Associativity.RIGHT;
        } else {
            return Associativity.LEFT;
        }
    }
    
    /**
     * Converts tokenized calculation from infix notation to
     * postfix (reverse polish) notation using Shunting-yard algorithm.
     * 
     * @param infix Tokens in infix notation
     * @return List<Token> Tokens in poistfix notation
     */
    
    public List<Token> infixToPostfix(List<Token> infix) {
        List<Token> output = new ArrayList();
        Stack<Token> stack = new Stack();
        
        while (infix.size() > 0) {
            Token t = infix.remove(0);
            
            switch (t.getType()) {
                case NUMBER:
                    output.add(t);
                    break;
                case OPER:
                    while (stack.size() > 0 && stack.peek().getType() == TokenType.OPER) {
                        Token t2 = stack.peek();
                        
                        if (this.getOpAssociativity(t.getData()) == Associativity.LEFT) {
                            if (this.getOpPrecedence(t.getData()) <= this.getOpPrecedence(t2.getData())) {
                                output.add(stack.pop());
                            } else {
                                break;
                            }
                        } else {
                            if (this.getOpPrecedence(t.getData()) < this.getOpPrecedence(t2.getData())) {
                                output.add(stack.pop());
                            } else {
                                break;
                            }
                        }
                    }
                    
                    stack.add(t);
                    break;
                case BRACKET_START:
                    stack.add(t);
                    break;
                case BRACKET_END:
                    while (stack.peek().getType() != TokenType.BRACKET_START) {
                        output.add(stack.pop());
                    }
                    
                    stack.pop();
                    
                    break;
            }
        }
        
        while (stack.size() > 0) {
            output.add(stack.pop());
        }
        
        return output;
    }
    
    public double calculate(String input) throws LaskinParseException, LaskinCalculationException {
        List<Token> tokens = this.parser.tokenize(input);
        
        tokens = this.infixToPostfix(tokens);
        
        Stack<Double> stack = new Stack();
        List<Double> args = new ArrayList();
        
        double val = 0;
        
        while (tokens.size() > 0) {
            Token t = tokens.remove(0);
            
            if (t.getType() == TokenType.NUMBER) {
                stack.add(Double.parseDouble(t.getData()));
            } else if (t.getType() == TokenType.OPER) {
                Function<Stack<Double>, Double> f = this.functions.get(t.getData());
                
                if(f == null) {
                    throw new LaskinCalculationException("Unknown operator (" + t.getData() + ")");
                }
                
                stack.push(f.apply(stack));
            }
        }
        
        if(stack.size() > 1) {
            
        }
        
        return stack.pop();
    }
    
    public String formatValue(Double val) {
        DecimalFormat decimalFormat = new DecimalFormat("0.#####");
        return decimalFormat.format(val);
    }
}
