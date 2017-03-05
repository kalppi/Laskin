package com.jarnoluu.laskin.exceptions;

/**
 * Virheellisestä skriptistä aiheutuva poikkeus
 * @author Jarno Luukkonen
 */
public class LaskinScriptException extends Exception {
    public LaskinScriptException(String message) {
        super(message);
    }
}