package com.jarnoluu.laskin.logiikka;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Postfixer {
    private enum Associativity {
        LEFT, RIGHT
    }
        
    private Postfixer.Associativity getOpAssociativity(String op) {
        if (op.equals("^")) {
            return Postfixer.Associativity.RIGHT;
        } else {
            return Postfixer.Associativity.LEFT;
        }
    }
    
    private int getOpPrecedence(String op) {
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
                    while (stack.size() > 0 && stack.peek().getType() == Token.Type.OPER) {
                        Token t2 = stack.peek();
                        
                        if (this.getOpAssociativity(t.getData()) == Postfixer.Associativity.LEFT) {
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
                    while (stack.peek().getType() != Token.Type.BRACKET_START) {
                        output.add(stack.pop());
                    }
                    
                    stack.pop();
                    
                    if (stack.size() > 0 && stack.peek().getType() == Token.Type.FUNC) {
                        output.add(stack.pop());
                    }
                    
                    break;
                case FUNC:
                    stack.add(t);
                    break;
                case COMMA:
                    while (stack.peek().getType() != Token.Type.BRACKET_START) {
                        output.add(stack.pop());
                    }
                    
                    break;
                
            }
        }
        
        while (stack.size() > 0) {
            output.add(stack.pop());
        }
        
        return output;
    }
}
