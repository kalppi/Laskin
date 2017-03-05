package com.jarnoluu.laskin.logic;

import com.jarnoluu.laskin.scripting.ScriptManager;
import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.exceptions.LaskinParseException;
import com.jarnoluu.laskin.exceptions.LaskinCalculationException;
import com.jarnoluu.laskin.exceptions.LaskinInvalidArgumentException;
import com.jarnoluu.laskin.exceptions.LaskinScriptException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Luokka, joka hoitaa laskujen laskemisen.
 * @author Jarno Luukkonen
 */
public class Calculator {
    /**
     * Pitää sisällään operaattorien funktiot.
     */
    private final Map<String, String> operators = new HashMap();
    
    private final ScriptManager scriptManager;
    
    /**
     * Edellisen laskun tulos.
     */
    private double lastValue = 0;
    
    /**
     * Konstruktori.
     * @throws LaskinScriptException 
     */
    public Calculator() throws LaskinScriptException {
        this.scriptManager = new ScriptManager("JavaScript");
        
        this.scriptManager.loadResourceScript("js/operators.js");
        this.scriptManager.loadResourceScript("js/functions.js");
        this.scriptManager.loadLocalScript("./laskin.js");
        
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
    
    /**
     * Laskee lausekkeen tuloksen.
     * @param input laskulauseke merkkijonona
     * @return tulos
     * @throws LaskinInvalidArgumentException
     * @throws LaskinParseException
     * @throws LaskinCalculationException 
     */
    public double calculate(String input) throws LaskinInvalidArgumentException, LaskinParseException, LaskinCalculationException {
        if (input == null || input.length() == 0) {
            throw new LaskinInvalidArgumentException("Empty input");
        }
        
        LinkedList<Token> tokens = Parser.tokenize(input);
        
        return this.calculate(tokens);
    }
    
    /**
     * Laskee lausekkeen tuloksen.
     * @param tokens laskulauseke listana tokeneja
     * @return tulos
     * @throws LaskinInvalidArgumentException
     * @throws LaskinParseException
     * @throws LaskinCalculationException 
     */
    public double calculate(LinkedList<Token> tokens) throws LaskinInvalidArgumentException, LaskinParseException, LaskinCalculationException {
        tokens = InfixToPostfix.transform(tokens);
        
        LinkedList<Double> stack = new LinkedList();
        
        while (tokens.size() > 0) {
            Token t = tokens.remove(0);
            String data = t.getData();
            
            switch (t.getType()) {
                case NUMBER:
                    stack.add(Double.parseDouble(data));
                    break;
                case NUMBER_HEX:
                    stack.add(Util.hexToDouble(data));
                    break;
                case NUMBER_BIN:
                    stack.add(Util.binToDouble(data));
                    break;
                case NUMBER_OCT:
                    stack.add(Util.octToDouble(data));
                    break;
                case SPECIAL:
                    stack.add(this.getSpecialValue(data));
                    break;
                case OPER:
                    String func = this.operators.get(data);
                    
                    if (func == null) {
                        throw new LaskinCalculationException("Unknown operator (" + data + ")");
                    }
                    
                    stack.add(this.scriptManager.invokeFunction(func, stack));
                    
                    break;
                case FUNC:
                    if (!this.scriptManager.functionExists(t.getData())) {
                        throw new LaskinCalculationException("Unknown function (" + data + ")");
                    }
                    
                    stack.add(this.scriptManager.invokeFunction(data, stack));
                    
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
}
