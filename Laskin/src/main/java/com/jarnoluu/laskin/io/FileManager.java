package com.jarnoluu.laskin.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Tietostojen käsittelystä vastaava luokka
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class FileManager {
    /**
     * Avaa tiedoston
     * @param path tiedoston sijainti
     * @return tiedosto luettavana streamina
     */
    public static InputStream openFileStream(String path) {
        URL url = FileManager.getResource(path);
        
        try {
            return url.openStream();
        } catch (IOException e) {
            return null;
        }
    }
    
    public static URL getResource(String path) {
        return FileManager.class.getClassLoader().getResource(path);
    }
}
