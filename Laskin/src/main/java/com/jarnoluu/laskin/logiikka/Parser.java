package com.jarnoluu.laskin.logiikka;

import com.jarnoluu.laskin.exceptions.LaskinParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Parser {
    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }
    
    private boolean isLetter(char c) {
        return Character.isLetter(c);
    }
    
    private boolean isOper(char c) {
        switch (c) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
            case '%':
                return true;
            default:
                return false;
        }
    }
    
    private void validBrackets(String input) throws LaskinParseException {
        int brackets = 0;
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '(') {
                brackets++;
            } else if (c == ')') {
                if (brackets == 0) {
                    throw new LaskinParseException("Wrong number or order of brackets");
                }
                
                brackets--;
            }
        }
        
        if (brackets != 0) {
            throw new LaskinParseException("Wrong number or order of brackets");
        }
    }
    
    private String removeWhiteSpace(String input) {
        return input.replaceAll("\\s", "");
    }

    
    public List<Token> tokenize(String input) throws LaskinParseException {
        input = this.removeWhiteSpace(input);
        
        this.validBrackets(input);
        
        List<Token> tokens = new ArrayList();
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            if (this.isDigit(c)) {
                String number = String.valueOf(c);
                
                while (i < input.length() - 1) {
                    i++;
                    c = input.charAt(i);
                    
                    if (this.isDigit(c)) {
                        number += c;
                    } else {
                        i--;
                        break;
                    }
                }
                
                tokens.add(new Token(Token.Type.NUMBER, number));
            } else if (this.isOper(c)) {
                if (c == '-' &&
                    this.isDigit(input.charAt(i + 1)) &&
                    (tokens.isEmpty() || input.charAt(i - 1) == '(' || this.isOper(input.charAt(i - 1)))) {
                    
                    String number = String.valueOf(c);
                    
                    while (i < input.length() - 1) {
                        i++;
                        c = input.charAt(i);

                        if (this.isDigit(c)) {
                            number += c;
                        } else {
                            i--;
                            break;
                        }
                    }
                    
                    tokens.add(new Token(Token.Type.NUMBER, number));
                } else {
                    tokens.add(new Token(Token.Type.OPER, String.valueOf(c)));
                }
            } else if (c == '(') {
                tokens.add(new Token(Token.Type.BRACKET_START));
            }  else if (c == ')') {
                tokens.add(new Token(Token.Type.BRACKET_END));
            } else if (c == ',') {
                tokens.add(new Token(Token.Type.COMMA));
            } else if (this.isLetter(c)) {
                String str = "";
                
                while (this.isLetter(c)) {
                    str += c;
                    i++;
                    
                    if (i == input.length()) {
                        break;
                    }
                    
                    c = input.charAt(i);
                }
                
                i--;
                
                tokens.add(new Token(Token.Type.FUNC, str));
            } else {
                throw new LaskinParseException("Unknown character (" + c + ")");
            }
        }
        
        return tokens;
    }
}
