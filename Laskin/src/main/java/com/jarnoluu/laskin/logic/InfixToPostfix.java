package com.jarnoluu.laskin.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class InfixToPostfix {
    private InfixToPostfix() {
        
    }
    
    private static enum Associativity {
        LEFT, RIGHT
    }
        
    private static InfixToPostfix.Associativity getOpAssociativity(String op) {
        if (op.equals("^")) {
            return InfixToPostfix.Associativity.RIGHT;
        } else {
            return InfixToPostfix.Associativity.LEFT;
        }
    }
    
    private static int getOpPrecedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 2;
            case "*":
            case "/":
            case "%":
                return 3;
            case "^":
                return 4;
            default:
                return 0;
        }
    }
    
    
    /**
     * Converts tokenized calculation from infix notation to
     * postfix (reverse polish) notation using Shunting-yard algorithm.
     * 
     * @param infix List of tokens in infix notation
     * @return List of tokens in postfix notation
     */
    
    public static LinkedList<Token> transform(LinkedList<Token> infix) {
        LinkedList<Token> output = new LinkedList();
        Stack<Token> stack = new Stack();
        
        for (Token t : infix) {
        /*while (infix.size() > 0) {
            Token t = infix.remove(0);*/
            
            switch (t.getType()) {
                case NUMBER:
                case NUMBER_HEX:
                case NUMBER_BIN:
                case NUMBER_OCT:
                case SPECIAL:
                    output.add(new Token(t));
                    break;
                case OPER:
                    while (stack.size() > 0 && stack.peek().getType() == Token.Type.OPER) {
                        Token t2 = stack.peek();
                        
                        if (InfixToPostfix.getOpAssociativity(t.getData()) == InfixToPostfix.Associativity.LEFT) {
                            if (InfixToPostfix.getOpPrecedence(t.getData()) <= InfixToPostfix.getOpPrecedence(t2.getData())) {
                                output.add(new Token(stack.pop()));
                            } else {
                                break;
                            }
                        } else {
                            if (InfixToPostfix.getOpPrecedence(t.getData()) < InfixToPostfix.getOpPrecedence(t2.getData())) {
                                output.add(new Token(stack.pop()));
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
                    while (stack.peek().getType() != Token.Type.BRACKET_START) {
                        output.add(stack.pop());
                    }
                    
                    stack.pop();
                    
                    if (stack.size() > 0 && stack.peek().getType() == Token.Type.FUNC) {
                        output.add(new Token(stack.pop()));
                    }
                    
                    break;
                case FUNC:
                    stack.add(t);
                    break;
                case COMMA:
                    while (stack.peek().getType() != Token.Type.BRACKET_START) {
                        output.add(new Token(stack.pop()));
                    }
                    
                    break;
                
            }
        }
        
        while (stack.size() > 0) {
            output.add(new Token(stack.pop()));
        }
        
        return output;
    }
}
