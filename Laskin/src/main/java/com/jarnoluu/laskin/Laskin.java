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
package com.jarnoluu.laskin;

import com.jarnoluu.laskin.logiikka.Calculator;
import com.jarnoluu.laskin.logiikka.Parser;
import com.jarnoluu.laskin.logiikka.Token;
import java.util.List;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Laskin {
    public static void main(String[] args) {
        Parser p = new Parser();
        Calculator c = new Calculator();
        
        try {
            List<Token> tokens = p.tokenize("3^(2+3)");

            tokens.stream().forEach((t) -> {
                System.out.println(t);
            });
            
            System.out.println("");
            
            List<Token> postfix = c.infixToPostfix(tokens);
            
            postfix.stream().forEach((t) -> {
                System.out.println(t);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
