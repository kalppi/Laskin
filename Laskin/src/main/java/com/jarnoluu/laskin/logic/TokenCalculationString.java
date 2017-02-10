package com.jarnoluu.laskin.logic;

import com.jarnoluu.laskin.Util;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import org.javatuples.Pair;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public final class TokenCalculationString extends CalculationString {    
    private int length = 0;
    
    public TokenCalculationString(Calculator calc) {
        super(calc);
    }
    
    public int length() {
        return this.tokens.size();
    }
    
    public void clear() {
        this.tokens.clear();
        this.cursor = 0;
        this.length = 0;
        
        this.fireCalculationChangeEvent();
    }
    
    private boolean convertSelected(Function<String, Boolean> test, Function<String, String> converter) {
        if (this.tokens.size() > 0) {
            String text = this.tokens.get(this.tokens.size() - 1 - this.cursor).toString();
            
            if (test.apply(text)) {
                try {
                    this.tokens.set(this.tokens.size() - 1 - this.cursor, Parser.tokenize(converter.apply(text)).getFirst());
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        
        return false;
    }
    
    public void convertAtCursor(String to) {
        List<Pair<Function<String, Boolean>, Function<String, String>>> conv = this.getConversions().get(to);
        
        if (conv == null) {
            return;
        }
        
        for (Pair<Function<String, Boolean>, Function<String, String>> e : conv) {
            if (this.convertSelected(e.getValue0(), e.getValue1())) {
                break;
            }
        }
        
        this.fireCalculationChangeEvent();
    }
    
    public void negateAtCursor() {
        if (this.tokens.size() > 0) {
            Token t = this.tokens.get(this.tokens.size() - 1 - this.cursor);
            
            if (t.getType() == Token.Type.NUMBER) {
                Double val = Double.parseDouble(t.getData()) * -1;
                this.tokens.set(this.tokens.size() - 1 - this.cursor, new Token(Token.Type.NUMBER, Util.formatSimple(val)));
                this.fireCalculationChangeEvent();
            }
        }
    }
    
    public void replaceWith(String str) {
        this.clear();
        this.cursor = 0;
        this.length = 0;
        this.insertAtCursor(str);
    }
    
    public void eraseAtCursor() {
        try {
            if (this.tokens.size() > 0) {
                int index = this.tokens.size() - 1 - this.cursor;
                Token t = this.tokens.get(index);
                
                Token.Type type = t.getType();
                
                if ((type == Token.Type.OPER) && this.cursor > 0) {
                    return;
                }
                
                this.tokens.remove(index);
                
                String text = t.toString();
                
                if (text.length() > 1) {
                    text = text.substring(0, text.length() - 1);
                    LinkedList<Token> newTokens = Parser.tokenize(text, false, true);
                    this.tokens.addAll(index, newTokens);
                } else if (this.cursor > 0 && text.length() == 1) {
                    this.tokens.add(index, new Token(Token.Type.EMPTY));
                }
                
                this.length--;
                this.setError(false);
                this.fireCalculationChangeEvent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean insertAtCursor(String str) {
        for (char c : str.toCharArray()) {
            if (!this.insert(c, false)) {
                return false;
            }
        }
        
        this.fireCalculationChangeEvent();
        
        return true;
    }
    
    public boolean insertAtCursor(char c) {
        return this.insert(c, true);
    }
       
    private boolean insert(char c, boolean update) {
        if (!Util.validChar(c)) {
            return false;
        }
        
        int len = this.length;
        
        if (len > 50) {
            return false;
        }
        
        boolean isOper = Util.isOper(c);
        
        try {
            if (this.tokens.size() > 0) {
                int index = this.tokens.size() - 1 - this.cursor;
                
                Token t = this.tokens.remove(index);
                Token next = (index < this.tokens.size() - 1 ? this.tokens.get(index) : null);
                Token prev = (index > 0 ? this.tokens.get(index - 1) : null);
                
                if (isOper && next != null && next.getType() == Token.Type.OPER) {
                    this.tokens.remove(index);
                    this.cursor--;
                    this.length--;
                }
                
                if (isOper && t.getType() == Token.Type.OPER) {
                    this.tokens.add(index, new Token(Token.Type.OPER, String.valueOf(c)));
                } else {
                    if (Character.isDigit(c) && t.getType() == Token.Type.OPER && t.getData().equals("-")
                        && (prev != null && prev.getType() != Token.Type.BRACKET_START && prev.getType() != Token.Type.OPER)) {
                       
                        String s = String.valueOf(c);
                        LinkedList<Token> newTokens = Parser.tokenize(s, false, true);
                        this.tokens.add(index, t);
                        this.tokens.addAll(index + 1, newTokens);
                    } else {
                        String s = t.toString() + c;
                        LinkedList<Token> newTokens = Parser.tokenize(s, false, true);
                        this.tokens.addAll(index, newTokens);
                    }
                }
                this.length++;
            } else {
                this.tokens = Parser.tokenize(String.valueOf(c), false, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.setError(false);
        
        if (update) {
            this.fireCalculationChangeEvent();
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return Token.join("", this.tokens);
    }
}
