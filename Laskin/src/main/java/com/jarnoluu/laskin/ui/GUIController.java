package com.jarnoluu.laskin.ui;

import com.jarnoluu.laskin.ui.control.FancyLabelSkin;
import com.jarnoluu.laskin.ui.control.FancyLabel;
import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.logic.CalculationString;
import com.jarnoluu.laskin.logic.Calculator;
import com.jarnoluu.laskin.logic.TokenCalculationString;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import com.jarnoluu.laskin.ui.control.IFancyLabelBehavior;
import com.jarnoluu.laskin.ui.control.FancyLabelBehaviorEditable;
import javafx.scene.Scene;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class GUIController {
    private CalculationString calc;
    
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
    
    @FXML
    private FancyLabel textCalculation;
    
    @FXML
    private FancyLabel calcHistory;
    
    public void setup(Scene scene) {
        try {
            this.calc = new TokenCalculationString(new Calculator());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.calc.addErrorStateChangeListener((state) -> {
            if (state) {
                this.showError();
            } else {
                this.hideError();
            }
        });
        
        this.calc.addCalculationChangeListener(this::update);
        this.calc.addCalculationListener(this::onCalculate);
        
        this.textCalculation.setCalculation(calc);
        this.calcHistory.setCalculation(new TokenCalculationString(null));
        
        IFancyLabelBehavior behavior = new FancyLabelBehaviorEditable(this.textCalculation);
        scene.setOnKeyPressed(behavior::onKeyPressed);
        scene.setOnKeyTyped(behavior::onKeyTyped);
        
        this.textCalculation.setBehavior(behavior);
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        this.hideError();
         
        Button b = (Button) event.getSource();
       
        if (b.getId() != null) {
            switch (b.getId()) {
                case "calc":
                    this.calc.calculate();
                    break;
                case "erase":
                    this.calc.eraseAtCursor();
                    break;
                case "add":
                    this.calc.insertAtCursor('+');
                    break;
                case "sub":
                    this.calc.insertAtCursor('-');
                    break;
                case "mult":
                    this.calc.insertAtCursor('*');
                    break;
                case "div":
                    this.calc.insertAtCursor('/');
                    break;
                case "pow":
                    this.calc.insertAtCursor('^');
                    break;
                case "mod":
                    this.calc.insertAtCursor('%');
                    break;
            }
        } else {
            this.calc.insertAtCursor(b.getText().charAt(0));
        }
    }
    
    private void onCalculate(double val, double old) {
        if (this.calc.getError()) {
            return;
        }
        
        this.calcHistory.getCalculation().replaceWith(this.calc.toString().replaceAll("\\$", Util.formatSimple(old)));
        this.calc.replaceWith(Util.formatSimple(val));

    }
    
    private void hideError() {
        this.textCalculation.pseudoClassStateChanged(GUIController.ERROR_PSEUDO_CLASS, false);
        this.textCalculation.requestLayout();
    }
    
    private void showError() {
        this.textCalculation.pseudoClassStateChanged(GUIController.ERROR_PSEUDO_CLASS, true);
        this.textCalculation.requestLayout();
    }
    
    public void update() {
        this.textCalculation.requestLayout();
        this.calcHistory.requestLayout();
    }
}
