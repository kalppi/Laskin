package com.jarnoluu.laskin.ui.control;

import javafx.scene.input.KeyEvent;


/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public interface IFancyLabelBehavior  {
    public void onKeyPressed(KeyEvent event);
    public void onKeyTyped(KeyEvent event);
}
