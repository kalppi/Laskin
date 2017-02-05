package com.jarnoluu.laskin.logiikka;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Token {
    public enum Type {
        BRACKET_START,
        BRACKET_END,
        COMMA,
        NUMBER,
        OPER,
        FUNC,
        SPECIAL,
        UNKNOWN
    }
    
    private final Token.Type type;
    private final String data;
    
    public Token(Token.Type type) {
        this.type = type;
        this.data = null;
    }
    
    public Token(Token.Type type, String data) {
        this.type = type;
        this.data = data;
    }

    public Token.Type getType() {
        return type;
    }

    public String getData() {
        return data;
    }
    
    @Override
    public String toString() {
        String text = this.data;
                        
        if (text == null) {
            switch (type) {
                case BRACKET_START:
                    text = "(";
                    break;
                case BRACKET_END:
                    text = ")";
                    break;
                case COMMA:
                    text = ",";
                    break;
                default:
                    break;
            }
        } else {
            if (this.type == Token.Type.SPECIAL) {
                switch (text) {
                    case "pi":
                        text = "π";
                        break;
                }
            } else {
                if (text.equals("*")) {
                    text = "×";
                } else if (text.equals("/")) {
                    text = "÷";
                }
            }
        }
        
        return text;
    }
    
    @Override
    public int hashCode() {
        int code = 53;
        code = 31 * code + this.type.hashCode();
        code = 31 * code + this.data.hashCode();
        
        return code;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof Token)) {
            return false;
        } else if (o == this) {
            return true;
        }
        
        Token t = (Token) o;
        
        return this.type == t.type && (this.data == null || this.data.equals(t.data));
    }
}
