package com.jarnoluu.laskin.logiikka;

import com.jarnoluu.laskin.exceptions.LaskinCalculationException;
import com.jarnoluu.laskin.exceptions.LaskinScriptException;
import com.jarnoluu.laskin.io.FileManager;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    private final static int MAX_FUNCTION_ARGS = 4;
    
    public ScriptManager(String language, String path) {
        this.language = language;
        this.path = path;
        
        this.functions = new HashMap();
        this.factory = new ScriptEngineManager();
        
        this.pattern = Pattern.compile("function\\s+_?[a-zA-Z0-9]+\\((.*?)\\)\\s+\\{");
    }
    
    public boolean functionExists(String f) {
        return this.functions.containsKey(f);
    }
    
    public void loadScript(String file) throws LaskinScriptException {
        InputStream stream = FileManager.openFileStream(this.path + file);
        
        if (stream == null) {
            throw new LaskinScriptException("Could not load script file (" + file + ")");
        }
        
        Reader reader = new InputStreamReader(stream);
        
        ScriptEngine engine = this.factory.getEngineByName(this.language);
        Compilable compilingEngine = (Compilable) engine;

        CompiledScript script = null;
        Bindings bindings = null;

        try {
            script = compilingEngine.compile(reader);
            bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
                    
            script.eval(bindings);
        } catch (ScriptException e) {
            throw new LaskinScriptException("Could not load script file (" + file + ")");
        }

        Invocable inv = (Invocable) script.getEngine();
        
        for (Entry<String, Object> e : bindings.entrySet()) {
            Matcher m = this.pattern.matcher(e.getValue().toString());
            
            if (!m.find()) {
                continue;
            }

            int argCount = m.group(1).split(",").length;

            if (argCount > ScriptManager.MAX_FUNCTION_ARGS) {
                throw new LaskinScriptException("Invalid function \"" + e.getKey() + "\" (more than " + ScriptManager.MAX_FUNCTION_ARGS + " parameters)");
            }
            
            this.functions.put((String) e.getKey(),
                Pair.with(inv, argCount)
            );
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
            throw new LaskinCalculationException("Failed to call function (" + f + ")");
        } catch (Exception e) {
            throw new LaskinCalculationException("Unknown error");
        }
    }
}
