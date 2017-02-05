package com.jarnoluu.laskin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Util {
    public static boolean validChar(char c) {
        List<Character> valid = Arrays.asList('-', '+', '*', '/', '%', '^', '$', '(', ')', ',', '.');
        
        return valid.contains(c) || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
    }
    
    public static boolean isOper(char c) {
        char[] oper = {'+', '-', '*', '/', '^', '%'};
        
        for (char cc : oper) {
            if (cc == c) {
                return true;
            }
        }
        
        return false;
    }
    
    public static LinkedList<String> splitNumber(String number, int groups) {
        LinkedList<String> parts = new LinkedList();
        
        int pos = number.indexOf('.');
        if (pos == -1) {
            pos = number.length();
        }
        
        String part = number.substring(pos);

        for (int i = pos - 1, count = 1; i >= 0; i--, count++) {
            part = number.charAt(i) + part;

            if (count == groups) {
                parts.addFirst(part);
                part = "";
                count = 0;
            }
        }

        if (part.length() > 0) {
            parts.addFirst(part);
        }
        
        return parts;
    }
}
