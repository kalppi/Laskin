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
        FUNC
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
        if (this.data != null) {
            return this.type.toString() + "(" + this.data + ")";
        } else {
            return this.type.toString();
        }
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
