package com.jarnoluu.laskin;

import com.jarnoluu.laskin.ui.GUI;
import com.jarnoluu.laskin.ui.TextUI;
import com.jarnoluu.laskin.ui.UIManager;
        
/**
 * Pääluokka
 * @author Jarno Luukkonen
 */
public class Laskin {
    /**
     * main metodi
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {        
        UIManager.addUI(new GUI());
    }
}
