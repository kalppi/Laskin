package com.jarnoluu.laskin;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Apuluokka monessa paikassa tarvittaviin operaatioihin.
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Util {
    private final static List<Character> VALID = Arrays.asList('-', '+', '*', '/', '%', '^', '$', '(', ')', ',', '.');
    
    /**
     * Onko merkki sallittu lausekkeessa.
     * @param c merkki
     * @return onko merkki sallittu.
     */
    public static boolean isValidChar(char c) {
        return  (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || Util.VALID.contains(c);
    }
    
    /**
     * Onko merkki operaattori.
     * @param c merkki
     * @return onko merkki operaattori.
     */
    public static boolean isOper(char c) {
        char[] oper = {'+', '-', '*', '/', '^', '%'};
        
        for (char cc : oper) {
            if (cc == c) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Onko merkkijono hex muodossa.
     * @param str merkkijono
     * @return onko merkkijono hex muodossa.
     */
    public static boolean isHexNumber(String str) {
        return str.matches("^0x[0-9a-f]+$");
    }
    
    /**
     * Onko merkkijono binääri muodossa.
     * @param str merkkijono
     * @return onko merkkijono binääri muodossa.
     */
    public static boolean isBinaryNumber(String str) {
        return str.matches("^0b[01]+$");
    }
    
    /**
     * Onko merkkijono oktaali muodossa.
     * @param str merkkijono.
     * @return onko merkkjono oktaali muodossa.
     */
    public static boolean isOctalNumber(String str) {
        return str.matches("^0o[0-7]+$");
    }
    
    /**
     * Onko merkkijono kokonaisluku.
     * @param str merkkijono.
     * @return onko merkkijono kokonaisluku.
     */
    public static boolean isIntegerNumber(String str) {
        return str.matches("^[0-9]+$");
    }
    
    /**
     * Muuntaa heksa muotoisen merkkijonon doubleksi.
     * @param hex merkkijono.
     * @return luku doublena.
     */
    public static double hexToDouble(String hex) {
        return (double) Integer.parseInt(hex.substring(2), 16);
    }
    
    /**
     * Muuntaa binääri muotoisen merkkijonon doubleksi.
     * @param bin merkkijono.
     * @return luku doublena
     */
    public static double binToDouble(String bin) {
        return (double) Integer.parseInt(bin.substring(2), 2);
    }
    
    /**
     * Muuntaa oktaali muotoisen merkkijonon doubleksi.
     * @param bin merkkijono.
     * @return luku doublena.
     */
    public static double octToDouble(String bin) {
        return (double) Integer.parseInt(bin.substring(2), 8);
    }
    
    /**
     * Muuntaa merkkijono muotoisen luvun tietyn pituisiksi ryhmiksi.
     * Esimerkiksi rymäkoolla 3 luku "12323456.789" muuttuu listaksi ["12", "323", "456.789"].
     * @param number luku merkkijonona.
     * @param groups kuinka monen merkin ryhmiin.
     * @return luvun ryhmät listana.
     */
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
    
    /**
     * Muuntaa luvun tekstiksi.
     * @param val luku
     * @return Luku tekstinä.
     */
    public static String formatValue(Double val) {
        DecimalFormat format = new DecimalFormat("0.#####");
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        format.setDecimalFormatSymbols(symbols);
        return format.format(val);
    }
}
