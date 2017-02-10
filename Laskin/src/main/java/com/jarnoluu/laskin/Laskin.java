package com.jarnoluu.laskin;

import com.jarnoluu.laskin.ui.GUI;
import com.jarnoluu.laskin.ui.TextUI;
import com.jarnoluu.laskin.ui.UIManager;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Laskin {
    public static void main(String[] args) throws Exception {
        UIManager.addUI(new GUI());
    }
}
