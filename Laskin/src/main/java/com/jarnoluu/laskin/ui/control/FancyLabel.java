package com.jarnoluu.laskin.ui.control;

import com.jarnoluu.laskin.logic.CalculationString;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;

/**
 *
 * @author Jarno Luukkonen
 */
public class FancyLabel extends Control {
    private IFancyLabelBehavior behavior;
    private CalculationString calc;
    private Font font;
    
    public FancyLabel() {
        getStyleClass().setAll("FancyLabel");
    }
    
    public Skin<FancyLabel> createDefaultSkin() {
        return new FancyLabelSkin(this);
    }
    
    public void setBehavior(IFancyLabelBehavior behavior) {
        this.behavior = behavior;
    }
    
    public IFancyLabelBehavior getBehavior() {
        return this.behavior;
    }
    
    public void setCalculation(CalculationString calc) {
        this.calc = calc;
    }
    
    public CalculationString getCalculation() {
        return this.calc;
    }
    
    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }
}

