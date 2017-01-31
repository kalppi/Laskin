package com.jarnoluu.laskin.logiikka;

import com.jarnoluu.laskin.exceptions.LaskinParseException;
import com.jarnoluu.laskin.exceptions.LaskinCalculationException;
import java.text.DecimalFormat;
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
    
    private final Parser parser;
    private final Postfixer postfixer;
    
    public Calculator() {
        this.parser = new Parser();
        this.postfixer = new Postfixer();
        this.scriptManager = new ScriptManager("JavaScript");
        
        this.scriptManager.loadScript("operators.js");
        this.scriptManager.loadScript("functions.js");
        
        this.operators.put("+", "_plus");
        this.operators.put("-", "_minus");
        this.operators.put("*", "_mult");
        this.operators.put("/", "_div");
        this.operators.put("^", "_pow");
        this.operators.put("%", "_mod");
    }
    
    public Parser getParser() {
        return this.parser;
    }
    
    public Postfixer getPostfixer() {
        return this.postfixer;
    }
     
    public double calculate(String input) throws LaskinParseException, LaskinCalculationException {
        List<Token> tokens = this.parser.tokenize(input);
        
        tokens = this.postfixer.infixToPostfix(tokens);
        
        LinkedList<Double> stack = new LinkedList();
                
        while (tokens.size() > 0) {
            Token t = tokens.remove(0);
            
            switch (t.getType()) {
                case NUMBER:
                    stack.add(Double.parseDouble(t.getData()));
                    break;
                case OPER:
                    String func = this.operators.get(t.getData());
                    
                    if(func == null) {
                        throw new LaskinCalculationException("Unknown operator (" + t.getData() + ")");
                    }
                    
                    stack.add(
                        this.scriptManager.invokeFunction(func, stack)
                    );
                    
                    break;
                case FUNC:
                    if(!this.scriptManager.functionExists(t.getData())) {
                        throw new LaskinCalculationException("Unknown function (" + t.getData() + ")");
                    }
                    
                    stack.add(
                         this.scriptManager.invokeFunction(t.getData(), stack)
                    );
                    
                    break;
            }
        }
        
        if (stack.size() > 1) {
            throw new LaskinCalculationException("Error");
        }
        
        return stack.get(0);
    }
    
    public String formatValue(Double val) {
        DecimalFormat decimalFormat = new DecimalFormat("0.#####");
        return decimalFormat.format(val);
    }
}
