package com.jarnoluu.laskin.logiikka;

import com.jarnoluu.laskin.exceptions.LaskinCalculationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.javatuples.Pair;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class ScriptManager {
    private final Map<String, Pair<Invocable, Integer>> functions;
    private final ScriptEngineManager factory;
    private final String language;
    private final String path;
    private final Pattern pattern;
            
    public ScriptManager(String language, String path) {
        this.language = language;
        this.path = path;
        
        this.functions = new HashMap();
        this.factory = new ScriptEngineManager();
        
        this.pattern = Pattern.compile(".*function\\s+_?[a-zA-Z0-9]+\\((.*?)\\)\\s+\\{.*", Pattern.MULTILINE);
    }
    
    public boolean functionExists(String f) {
        return this.functions.containsKey(f);
    }
    
    public void loadScript(String file) {
        try {
            URL r = ScriptManager.class.getClassLoader().getResource(this.path + file);
            
            try {
                Reader reader = new InputStreamReader(r.openStream());
                
                ScriptEngine engine = this.factory.getEngineByName(this.language);
                
                Compilable compilingEngine = (Compilable) engine;
                CompiledScript script = compilingEngine.compile(reader);
                
                Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
                
                script.eval(bindings);
                
                Invocable inv = (Invocable) script.getEngine();
                
                bindings.entrySet().stream().forEach((e) -> {
                    Matcher m = this.pattern.matcher(e.getValue().toString());
                    m.find();
                    
                    int argCount = m.group(1).split(",").length;
                    
                    this.functions.put((String) e.getKey(),
                        Pair.with(inv, argCount)
                    );
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Double invokeFunction(String f, List<Double> stack) throws LaskinCalculationException {
        Pair<Invocable, Integer> func = this.functions.get(f);
        
        try {
            Invocable inv = func.getValue0();
            int argCount = func.getValue1();
            
            if (stack.size() < argCount) {
                throw new LaskinCalculationException("Not enough arguments for function (" + f + ")");
            }
            
            switch (argCount) {
                case 1:
                    return (Double) inv.invokeFunction(f, stack.remove(stack.size() - 1));
                case 2:
                    return (Double) inv.invokeFunction(f, stack.remove(stack.size() - 2), stack.remove(stack.size() - 1));
                case 3:
                    return (Double) inv.invokeFunction(f, stack.remove(stack.size() - 3), stack.remove(stack.size() - 2), stack.remove(stack.size() - 1));
                case 4:
                    return (Double) inv.invokeFunction(f, stack.remove(stack.size() - 4), stack.remove(stack.size() - 3), stack.remove(stack.size() - 2), stack.remove(stack.size() - 1));
                default:
                    throw new LaskinCalculationException("Unknown error");
            }
        } catch (ScriptException | NoSuchMethodException e) {
            throw new LaskinCalculationException("Unknown error");
        }
    }
}
