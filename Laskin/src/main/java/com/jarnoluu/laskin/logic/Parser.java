package com.jarnoluu.laskin.logic;

import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.exceptions.LaskinParseException;
import java.util.LinkedList;
import java.util.function.Function;

/**
 * Luokka joka hoitaa tekstimuotoisen laskun muuttamisen tokeneiksi.
 * @author Jarno Luukkonen
 */
public class Parser {
    private Parser() {
    }
    
    private static boolean isNumberSymbol(char c) {
        return Character.isDigit(c);
    }
    
    private static boolean isNumberOrDecimalSeparatorSymbol(char c) {
        return Character.isDigit(c) || c == '.';
    }
    
    private static boolean isLetterSymbol(char c) {
        return Character.isLetter(c);
    }
        
    private static boolean isHexSymbol(char c) {
        return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f'));
    }
    
    private static boolean isBinarySymbol(char c) {
        return (c == '0' || c == '1');
    }
    
    private static boolean isOctalSymbol(char c) {
        return (c >= '0' && c <= '7');
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
    
    /**
     * Muuntaa laskulausekkeen listaksi tokeneja.
     * @param input laskulauseke merkkijonona.
     * @return kusta tokeneja
     * @throws LaskinParseException 
     */
    public static LinkedList<Token> tokenize(String input) throws LaskinParseException {
        return Parser.tokenize(input, true, false);
    }
    
    private static String collect(String input, int i, Function<Character, Boolean> func) {
        String str = "";
        
        while (i < input.length() - 1) {
            i++;
            char c = input.charAt(i);

            if (func.apply(c)) {
                str += c;
            } else {
                break;
            }
        }
        
        return str;
    }
    
    private static void checkDecimals(String number) throws LaskinParseException {
        int count = number.length() - number.replace(".", "").length();
        if (count > 1) {
            throw new LaskinParseException("Too many decimal points");
        }
    }
    
    /**
     * Muuntaa merkkijonopohjaisen laskulausekkeen listaksi tokeneja.
     * @param input laskulauseke merkkijonona
     * @param checkBrackets sallitaanko parittomat sulut
     * @param allowUnknown sallitaanko tuntemattomat merkit
     * @return lista tokeneja
     * @throws LaskinParseException 
     */
    public static LinkedList<Token> tokenize(String input, boolean checkBrackets, boolean allowUnknown) throws LaskinParseException {
        input = Parser.removeWhiteSpace(input);
        input = input.toLowerCase();
        
        if (checkBrackets) {
            Parser.validateBrackets(input);
        }
        
        LinkedList<Token> tokens = new LinkedList();
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            char next = 0;
            if (i < input.length() - 1) {
                next = input.charAt(i + 1);
            }
            
            if (c == '0' && (next == 'x' || next == 'b' || next == 'o')) {
                Function<Character, Boolean> f;
                
                switch (next) {
                    case 'x':
                        f = Parser::isHexSymbol;
                        break;
                    case 'b':
                        f = Parser::isBinarySymbol;
                        break;
                    default:
                        f = Parser::isOctalSymbol;
                        break;
                }
                
                String number = Parser.collect(input, i + 1, f);
                i += number.length() + 1;
                number = String.valueOf(c) + next + number;
                
                switch (next) {
                    case 'x':
                        tokens.add(new Token(Token.Type.NUMBER_HEX, number));
                        break;
                    case 'b':
                        tokens.add(new Token(Token.Type.NUMBER_BIN, number));
                        break;
                    default:
                        tokens.add(new Token(Token.Type.NUMBER_OCT, number));
                        break;
                }
            } else if (c == '.') {
                String number = Parser.collect(input, i, Parser::isNumberOrDecimalSeparatorSymbol);
                i += number.length();
                
                number = "0." + number;
                
                Parser.checkDecimals(number);
                
                tokens.add(new Token(Token.Type.NUMBER, number));
            } else if (Parser.isNumberSymbol(c)) {
                String number = Parser.collect(input, i - 1, Parser::isNumberOrDecimalSeparatorSymbol);
                i += number.length() - 1;
                Parser.checkDecimals(number);
                
                tokens.add(new Token(Token.Type.NUMBER, number));
            } else if (Util.isOper(c)) {
                if (c == '-' &&
                    i < input.length() - 1 && Parser.isNumberSymbol(next) &&
                    (tokens.isEmpty() || input.charAt(i - 1) == '(' || Util.isOper(input.charAt(i - 1)))) {
                    
                    String number = Parser.collect(input, i, Parser::isNumberOrDecimalSeparatorSymbol);
                    i += number.length();

                    number = String.valueOf(c) + number;
                    Parser.checkDecimals(number);
                    
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
            } else if (Parser.isLetterSymbol(c)) {
                String str = Parser.collect(input, i - 1, Parser::isLetterSymbol);
                i += str.length() - 1;
                
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