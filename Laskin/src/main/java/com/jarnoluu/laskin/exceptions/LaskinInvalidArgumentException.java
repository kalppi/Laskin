package com.jarnoluu.laskin.exceptions;

/**
 * Laskimelle väärien argumenttien antamisesta johtuva poikkeus
 * @author Jarno Luukkonen
 */
public class LaskinInvalidArgumentException extends Exception {
    public LaskinInvalidArgumentException(String message) {
        super(message);
    }
}
