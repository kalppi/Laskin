package com.jarnoluu.laskin.logiikka;

import com.jarnoluu.laskin.exceptions.LaskinParseException;
import java.util.LinkedList;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Parser {
    private Parser() {
        
    }
    
    private static boolean isDigit(char c) {
        return Character.isDigit(c);
    }
    
    private static boolean isLetter(char c) {
        return Character.isLetter(c);
    }
    
    private static boolean isOper(char c) {
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
    
    private static void validateBrackets(String input) throws LaskinParseException {
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
    
    private static String removeWhiteSpace(String input) {
        return input.replaceAll("\\s", "");
    }
    
    public static LinkedList<Token> tokenize(String input) throws LaskinParseException {
        return Parser.tokenize(input, true, false);
    }
    
    public static LinkedList<Token> tokenize(String input, boolean checkBrackets, boolean allowUnknown) throws LaskinParseException {
        input = Parser.removeWhiteSpace(input);
        input = input.toLowerCase();
        
        if (checkBrackets) {
            Parser.validateBrackets(input);
        }
        
        LinkedList<Token> tokens = new LinkedList();
        
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            if (Parser.isDigit(c)) {
                String number = String.valueOf(c);
                boolean hasDecimalPoint = false;
                
                while (i < input.length() - 1) {
                    i++;
                    c = input.charAt(i);
                    
                    if (Parser.isDigit(c)) {
                        number += c;
                    } else if (c == '.') {
                        if (hasDecimalPoint) {
                            throw new LaskinParseException("Too many decimal points");
                        } else {
                            number += '.';
                            hasDecimalPoint = true;
                        }
                    } else {
                        i--;
                        break;
                    }
                }
                
                tokens.add(new Token(Token.Type.NUMBER, number));
            } else if (Parser.isOper(c)) {
                if (c == '-' &&
                    i < input.length() - 1 && Parser.isDigit(input.charAt(i + 1)) &&
                    (tokens.isEmpty() || input.charAt(i - 1) == '(' || Parser.isOper(input.charAt(i - 1)))) {
                    
                    String number = String.valueOf(c);
                    
                    while (i < input.length() - 1) {
                        i++;
                        c = input.charAt(i);

                        if (Parser.isDigit(c)) {
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
            } else if (c == '$') {
                tokens.add(new Token(Token.Type.SPECIAL, "$"));
            } else if (c == '.') {
                String number = "0" + String.valueOf(c);
                    
                while (i < input.length() - 1) {
                    i++;
                    c = input.charAt(i);

                    if (Parser.isDigit(c)) {
                        number += c;
                    } else if (c == '.') {
                        throw new LaskinParseException("Too many decimal points");
                    } else {
                        i--;
                        break;
                    }
                }

                tokens.add(new Token(Token.Type.NUMBER, number));
            } else if (Parser.isLetter(c)) {
                String str = "";
                
                while (Parser.isLetter(c)) {
                    str += c;
                    i++;
                    
                    if (i == input.length()) {
                        break;
                    }
                    
                    c = input.charAt(i);
                }
                
                i--;
                
                switch (str) {
                    case "pi":
                        tokens.add(new Token(Token.Type.SPECIAL, "pi"));
                        break;
                    default:
                        tokens.add(new Token(Token.Type.FUNC, str));
                }
            } else {
                if (allowUnknown) {
                    tokens.add(new Token(Token.Type.UNKNOWN, String.valueOf(c)));
                } else {
                    throw new LaskinParseException("Unknown character (" + c + ")");
                }
            }
        }
        
        return tokens;
    }
}
