package com.jarnoluu.laskin.logiikka;

import com.jarnoluu.laskin.exceptions.LaskinParseException;
import com.jarnoluu.laskin.exceptions.LaskinCalculationException;
import com.jarnoluu.laskin.exceptions.LaskinScriptException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class Calculator {
    private final Map<String, String> operators = new HashMap();
    private final ScriptManager scriptManager;
    
    private double lastValue = 0;
    
    public Calculator() throws LaskinScriptException {
        this.scriptManager = new ScriptManager("JavaScript", "js/");
        
        this.scriptManager.loadScript("operators.js");
        this.scriptManager.loadScript("functions.js");
        
        this.operators.put("+", "_plus");
        this.operators.put("-", "_minus");
        this.operators.put("*", "_mult");
        this.operators.put("/", "_div");
        this.operators.put("^", "_pow");
        this.operators.put("%", "_mod");
    }
    
    public double getLastValue() {
        return this.lastValue;
    }
    
    private double getSpecialValue(String special) {
        switch (special) {
            case "pi":
                return Math.PI;
            case "$":
                return this.lastValue;
            default:
                return 0;
        }
    }
     
    public double calculate(String input) throws LaskinParseException, LaskinCalculationException {
        List<Token> tokens = Parser.tokenize(input);
        
        tokens = InfixToPostfix.transform(tokens);
        
        LinkedList<Double> stack = new LinkedList();
        
        while (tokens.size() > 0) {
            Token t = tokens.remove(0);
            
            switch (t.getType()) {
                case NUMBER:
                    stack.add(Double.parseDouble(t.getData()));
                    break;
                case SPECIAL:
                    stack.add(this.getSpecialValue(t.getData()));
                    break;
                case OPER:
                    String func = this.operators.get(t.getData());
                    
                    if (func == null) {
                        throw new LaskinCalculationException("Unknown operator (" + t.getData() + ")");
                    }
                    
                    stack.add(this.scriptManager.invokeFunction(func, stack));
                    
                    break;
                case FUNC:
                    if (!this.scriptManager.functionExists(t.getData())) {
                        throw new LaskinCalculationException("Unknown function (" + t.getData() + ")");
                    }
                    
                    stack.add(this.scriptManager.invokeFunction(t.getData(), stack));
                    
                    break;
            }
        }
        
        if (stack.size() > 1) {
            throw new LaskinCalculationException("Error");
        } else if (stack.isEmpty()) {
            return 0;
        }
        
        double val = stack.get(0);
        this.lastValue = val;
        
        return val;
    }
    
    public static String formatValue(Double val) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.#####");
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        
        return decimalFormat.format(val);
    }
}
