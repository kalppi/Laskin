package com.jarnoluu.laskin;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Util {
    private final static List<Character> VALID = Arrays.asList('-', '+', '*', '/', '%', '^', '$', '(', ')', ',', '.');
    
    public static boolean validChar(char c) {
        return  (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || Util.VALID.contains(c);
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
    
    public static boolean isHexNumber(String str) {
        return str.matches("^0x[0-9a-f]+$");
    }
    
    public static boolean isBinaryNumber(String str) {
        return str.matches("^0b[01]+$");
    }
    
    public static boolean isOctalNumber(String str) {
        return str.matches("^0o[0-7]+$");
    }
    
    public static boolean isIntegerNumber(String str) {
        return str.matches("^[0-9]+$");
    }
    
    public static double hexToDouble(String hex) {
        return (double) Integer.parseInt(hex.substring(2), 16);
    }
    
    public static double binToDouble(String bin) {
        return (double) Integer.parseInt(bin.substring(2), 2);
    }
    
    public static double octToDouble(String bin) {
        return (double) Integer.parseInt(bin.substring(2), 8);
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

    public static String formatSimple(Double val) {
        // Format the value so that it loses its trailing zeros (e.g. 123.0 -> 123)
        DecimalFormat format = new DecimalFormat("0.#####");
        return format.format(val).replace(",", ".");
    }

    public static String formatValue(Double val) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.#####");
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        return decimalFormat.format(val);
    }
}
