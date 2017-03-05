package com.jarnoluu.laskin.exceptions;

/**
 * Laskemisessa tapahtuva poikkeus
 * @author Jarno Luukkonen
 */
public class LaskinCalculationException extends Exception {
    public LaskinCalculationException(String message) {
        super(message);
    }
}