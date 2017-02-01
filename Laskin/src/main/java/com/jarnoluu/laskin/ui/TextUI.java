package com.jarnoluu.laskin.ui;

import com.jarnoluu.laskin.logiikka.Calculator;
import com.jarnoluu.laskin.exceptions.LaskinCalculationException;
import com.jarnoluu.laskin.exceptions.LaskinParseException;
import com.jarnoluu.laskin.logiikka.Token;
import java.util.List;
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
        System.out.println("  <input>");
        System.out.println("  .exit");
        System.out.println("  .tokenize <input>");
        System.out.println("  .postfix <input>");
        
        boolean ok = true;
        
        Calculator c = new Calculator();
        
        while (ok) {
            System.out.print("> ");
            String line = s.nextLine();
            String[] parts = line.split(" ", 2);
            
            switch (parts[0]) {
                case ".exit":
                    ok = false;
                    break;
                case ".tokenize":
                    try {
                        List<Token> tokens = c.getParser().tokenize(parts[1]);
                        
                        tokens.stream().forEach((t) -> {
                            System.out.println(t);
                        });
                    } catch (LaskinParseException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    
                    break;
                case ".postfix":
                    try {
                        List<Token> tokens = c.getParser().tokenize(parts[1]);
                        tokens = c.getPostfixer().infixToPostfix(tokens);
                        
                        tokens.stream().forEach((t) -> {
                            System.out.println(t);
                        });
                    } catch (LaskinParseException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    
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
