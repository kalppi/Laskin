package com.jarnoluu.laskin.ui;

import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.logic.Calculator;
import com.jarnoluu.laskin.exceptions.LaskinCalculationException;
import com.jarnoluu.laskin.exceptions.LaskinInvalidArgumentException;
import com.jarnoluu.laskin.exceptions.LaskinParseException;
import com.jarnoluu.laskin.logic.InfixToPostfix;
import com.jarnoluu.laskin.logic.Parser;
import com.jarnoluu.laskin.logic.Token;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Jarno Luukkonen
 */
public class TextUI implements UI {
    @Override
    public void start() {
        Scanner s = new Scanner(System.in);
        
        System.out.println("Input calculation");
        System.out.println("Commands:");
        System.out.println("  <input>");
        System.out.println("  .exit");
        System.out.println("  .tokenize <input>");
        System.out.println("  .postfix <input>");
        
        boolean ok = true;
        
        Calculator c;
        
        try {
            c = new Calculator();
        } catch (Exception e) {
            System.out.println("Failed to start calculator: " + e.getMessage());
            return;
        }
        
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
                        List<Token> tokens = Parser.tokenize(parts[1]);
                        
                        tokens.stream().forEach((t) -> {
                            System.out.println(t);
                        });
                    } catch (LaskinParseException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    
                    break;
                case ".postfix":
                    try {
                        LinkedList<Token> tokens = Parser.tokenize(parts[1]);
                        tokens = InfixToPostfix.transform(tokens);
                        
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
                        
                        System.out.println(Util.formatValue(val));
                    } catch (LaskinInvalidArgumentException | LaskinParseException | LaskinCalculationException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
            }
        }
    }
}
