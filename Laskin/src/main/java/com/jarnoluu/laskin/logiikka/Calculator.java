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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Calculator {
    private enum Associativity {
        LEFT, RIGHT
    }
    
    private Parser parser;
    
    public Calculator() {
        this.parser = new Parser();
    }
    
    private int getOpPrecedence(String op) {
        switch(op) {
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
        if(op.equals("^")) return Associativity.RIGHT;
        else return Associativity.LEFT;
    }
    
    public List<Token> infixToPostfix(List<Token> infix) {
        List<Token> output = new ArrayList();
        Stack<Token> stack = new Stack();
        
        while(infix.size() > 0) {
            Token t = infix.remove(0);
            
            switch(t.getType()) {
                case NUMBER:
                    output.add(t);
                    break;
                case OPER:
                    while(stack.size() > 0 && stack.peek().getType() == TokenType.OPER) {
                        Token t2 = stack.peek();
                        
                        if(this.getOpAssociativity(t.getData()) == Associativity.LEFT) {
                            if(this.getOpPrecedence(t.getData()) <= this.getOpPrecedence(t2.getData())) {
                                output.add(stack.pop());
                            } else {
                                break;
                            }
                        } else {
                            if(this.getOpPrecedence(t.getData()) < this.getOpPrecedence(t2.getData())) {
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
                    while(stack.peek().getType() != TokenType.BRACKET_START) {
                        output.add(stack.pop());
                    }
                    
                    stack.pop();
                    
                    break;
            }
        }
        
        while(stack.size() > 0) {
            output.add(stack.pop());
        }
        
        return output;
    }
    
    public double calculate(String input) throws LaskinParseException {
        List<Token> tokens = this.parser.tokenize(input);
        
        
        return 0;
    }
}
