package com.jarnoluu.laskin.ui;

import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.exceptions.LaskinCalculationException;
import com.jarnoluu.laskin.exceptions.LaskinParseException;
import com.jarnoluu.laskin.logiikka.Calculator;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class GUIController implements Initializable {
    private Calculator calculator;
    private final StringBuilder calculation = new StringBuilder();
    
    private static final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");
    
    @FXML
    private Label textCalculation;
    
    @FXML
    private Label calcHistory;
    
    public String getCalculation() {
        return this.calculation.toString();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.calculator = new Calculator();
        } catch (Exception e) {
            System.out.println("Failed to start calculator: " + e.getMessage());
        }
        
        this.calcHistory.setSkin(new CustomLabelSkin(this.calcHistory, Color.rgb(210, 210, 210)));
        this.textCalculation.setSkin(new CustomLabelSkin(this.textCalculation, Color.rgb(230, 230, 230)));
    }
    
    private String simpleFormat(Double val) {
        // Format the value so that it loses its trailing zeros (e.g. 123.0 -> 123)
        DecimalFormat format = new DecimalFormat("0.#####");
        return format.format(val);
    }
    
    public void executeCommand(String cmd) {
        this.textCalculation.pseudoClassStateChanged(GUIController.ERROR_PSEUDO_CLASS, false);
        
        switch (cmd) {
            case "clear":
                this.calculation.setLength(0);
                this.updateCalculation();
                
                break;
            case "erase":
                if (this.calculation.length() > 0) {
                    this.calculation.setLength(this.calculation.length() - 1);
                    this.updateCalculation();
                }

                break;
            case "add":
                this.insert('+');
                break;
            case "sub":
                this.insert('-');
                break;
            case "mult":
                this.insert('*');
                break;
            case "div":
                this.insert('/');
                break;
            case "pow":
                this.insert('^');
                break;
            case "mod":
                this.insert('%');
                break;
            case "calc":
            {
                try {
                    String calc = this.calculation.toString();
                    
                    if (calc.length() == 0 || calc.equals("0")) {
                        return;
                    }
                    
                    Double last = this.calculator.getLastValue();
                    double val = this.calculator.calculate(calc);
                    
                    this.calcHistory.setText(calc.replaceAll("\\$", this.simpleFormat(last)));

                    this.calculation.setLength(0);
                    this.calculation.append(this.simpleFormat(val));
                } catch (LaskinParseException | LaskinCalculationException e) {
                    this.textCalculation.pseudoClassStateChanged(GUIController.ERROR_PSEUDO_CLASS, true);
                    System.out.println(e);
                }
                
                this.updateCalculation();
                
                break;
            }
        }
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button b = (Button) event.getSource();
       
        if (b.getId() != null) {
            this.executeCommand(b.getId());
        } else {
            this.insert(b.getText().charAt(0));
        }
    }
    
    public void insert(String str) {
        for (char c : str.toCharArray()) {
            if (!this.insert(c)) {
                return;
            }
        }
    }
    
    public boolean insert(char c) {
        if (!Util.validChar(c)) {
            return false;
        }
        
        int len = this.calculation.length();
        
        if (len > 50) {
            return false;
        }
        
        boolean isOper = Util.isOper(c);
        
        if (len > 0 && isOper &&
                Util.isOper(this.calculation.substring(len - 1).charAt(0))) {
            return false;
        }
        
        this.textCalculation.pseudoClassStateChanged(GUIController.ERROR_PSEUDO_CLASS, false);
        
        if (len == 1 && this.calculation.charAt(0) == '0') {
            this.calculation.setLength(0);
        }
        
        if (len == 0 && isOper && c != '-') {
            return false;
        }
        
        if (c == '.') {
            List<String> parts = Arrays.asList(this.calculation.toString().split("[+\\-\\*/\\(\\)%^]"));
            String last = parts.get(parts.size() - 1);
            
            if (last.indexOf('.') != -1) {
                return false;
            }
        }
        
        this.calculation.append(c);
        
        this.updateCalculation();
        
        return true;
    }
    
    private void updateCalculation() {
        this.textCalculation.setText(this.calculation.toString().toLowerCase());
        this.textCalculation.requestLayout();
    }
}
