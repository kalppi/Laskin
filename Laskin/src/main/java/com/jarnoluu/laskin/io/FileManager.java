package com.jarnoluu.laskin.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Tietostojen käsittelystä vastaava luokka.
 * @author Jarno Luukkonen
 */
public class FileManager {
    /**
     * Avaa resurssi tiedoston virraksi.
     * @param path tiedoston sijainti
     * @return tiedosto luettavana streamina
     */
    public static InputStream openResourceFileStream(String path) {
        URL url = FileManager.getResource(path);
        
        try {
            return url.openStream();
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Avaa tiedoston virraksi.
     * @param path tiedoston sijainti
     * @return tiedosto luettavana streamina
     */
    public static InputStream openFileStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
    
    /**
     * Palauttaa resurssin polkuna.
     * @param path resurssin sijainti
     * @return resurssi polkuna
     */
    public static URL getResource(String path) {
        return FileManager.class.getClassLoader().getResource(path);
    }
}
