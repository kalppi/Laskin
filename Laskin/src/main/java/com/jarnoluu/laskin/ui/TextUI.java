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
package com.jarnoluu.laskin.ui;

import com.jarnoluu.laskin.logiikka.Calculator;
import com.jarnoluu.laskin.logiikka.LaskinCalculationException;
import com.jarnoluu.laskin.logiikka.LaskinParseException;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class TextUI implements UI {
    public void start() {
        Scanner s = new Scanner(System.in);
        
        System.out.println("Input calculation");
        System.out.println("Commands:");
        System.out.println("  .exit");
        
        boolean ok = true;
        
        Calculator c = new Calculator();
        
        while (ok) {
            System.out.print("> ");
            String line = s.nextLine();
            
            switch (line) {
                case ".exit":
                    ok = false;
                    break;
                default:
                    try {
                        Double val = c.calculate(line);
                        
                        System.out.println(c.formatValue(val));
                    } catch (LaskinParseException | LaskinCalculationException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
            }
        }
    }
}
