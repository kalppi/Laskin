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

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Parser {
    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }
    
    private boolean isOper(char c) {
        switch (c) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
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
                tokens.add(new Token(Token.Type.OPER, String.valueOf(c)));
            } else if (c == '(') {
                tokens.add(new Token(Token.Type.BRACKET_START));
            }  else if (c == ')') {
                tokens.add(new Token(Token.Type.BRACKET_END));
            } else {
                throw new LaskinParseException("Unknown character (" + c + ")");
            }
        }
        
        return tokens;
    }
}
