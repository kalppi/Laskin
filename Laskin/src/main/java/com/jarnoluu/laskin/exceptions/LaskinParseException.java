package com.jarnoluu.laskin.exceptions;

/**
 * Virheellisestö laskusta aihetuva poikkeus
 * @author Jarno Luukkonen
 */
public class LaskinParseException extends Exception {
    public LaskinParseException(String message) {
        super(message);
    }
}
