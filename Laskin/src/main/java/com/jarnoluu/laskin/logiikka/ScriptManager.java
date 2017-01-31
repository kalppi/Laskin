package com.jarnoluu.laskin.logiikka;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
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
            
    public ScriptManager(String language) {
        this.language = language;
        
        this.functions = new HashMap();
        this.factory = new ScriptEngineManager();
    }
    
    public boolean functionExists(String f) {
        return this.functions.containsKey(f);
    }
    
    public int getFunctionArgCount(String f) {
        return this.functions.get(f).getValue1();
    }
    
    public void loadScript(String file) {
        try {
            URL r = ScriptManager.class.getClassLoader().getResource("js/" + file);
            
            try {
                Reader reader = new InputStreamReader(r.openStream());
                
                ScriptEngine engine = this.factory.getEngineByName(this.language);
                
                Compilable compilingEngine = (Compilable) engine;
                CompiledScript script = compilingEngine.compile(reader);
                
                Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
                
                script.eval(bindings);
                
                Invocable inv = (Invocable) script.getEngine();
                
                Pattern p = Pattern.compile(".*function\\s+_?[a-zA-Z0-9]+\\((.*?)\\)\\s+\\{.*", Pattern.MULTILINE);
                
                for(Map.Entry e : bindings.entrySet()) {
                    Matcher m = p.matcher(e.getValue().toString());
                    m.find();
                    
                    int argCount = m.group(1).split(",").length;
                    
                    this.functions.put((String)e.getKey(),
                        Pair.with(inv, argCount)
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Double invokeFunction(String f, Double v) {
        return 0.0;
    }
    
    public Double invokeFunction(String f, Double a, Double b) {
        Pair<Invocable, Integer> func = this.functions.get(f);
        
        try {
            return (Double)func.getValue0().invokeFunction(f, b, a);
        } catch (ScriptException | NoSuchMethodException e) {
            return 0.0;
        }
    }
}
