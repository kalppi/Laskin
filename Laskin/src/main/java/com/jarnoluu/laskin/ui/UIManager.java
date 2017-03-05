package com.jarnoluu.laskin.ui;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jarno Luukkonen
 */
public class UIManager {
    private static List<UI> uis = new ArrayList();
    
    public static void addUI(UI ui) {
        UIManager.uis.add(ui);
        ui.start();
    }
}
