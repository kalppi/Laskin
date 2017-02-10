package com.jarnoluu.laskin.scripting;

import com.jarnoluu.laskin.exceptions.LaskinCalculationException;
import com.jarnoluu.laskin.exceptions.LaskinScriptException;
import com.jarnoluu.laskin.io.FileManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
        
        this.pattern = Pattern.compile("function\\s+_?[a-zA-Z0-9]+\\((.*?)\\)");
    }
    
    public boolean functionExists(String f) {
        return this.functions.containsKey(f);
    }
    
    public void loadScript(String file) throws LaskinScriptException {
        try (InputStream stream = FileManager.openFileStream(this.path + file)) {
            if (stream == null) {
                throw new LaskinScriptException("Could not load script file (" + file + ")");
            }

            ScriptEngine engine = this.factory.getEngineByName(this.language);
            Compilable compilingEngine = (Compilable) engine;

            CompiledScript script = null;
            Bindings bindings = null;

            try {
                List<String> doc = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
                
                doc.add(0, "var api = Java.type(\"com.jarnoluu.laskin.scripting.ScriptAPI\");");
               
                String scriptStr = String.join("\n", doc);
                
                script = compilingEngine.compile(scriptStr);
                bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);

                script.eval(bindings);
            } catch (ScriptException  e) {
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
        } catch (IOException e) {
            throw new LaskinScriptException("Could not load script file (" + file + ")");
        }
    }
    
    public double invokeFunction(String f, List<Double> stack) throws LaskinCalculationException {
        Pair<Invocable, Integer> func = this.functions.get(f);
        
        try {
            Invocable inv = func.getValue0();
            int argCount = func.getValue1();
            
            if (stack.size() < argCount) {
                throw new LaskinCalculationException("Not enough arguments for function (" + f + ")");
            }
            
            double val;
            
            switch (argCount) {
                case 1:
                    val = (double) inv.invokeFunction(f, stack.remove(stack.size() - 1));
                    break;
                case 2:
                    val = (double) inv.invokeFunction(f, stack.remove(stack.size() - 2), stack.remove(stack.size() - 1));
                    break;
                case 3:
                    val = (double) inv.invokeFunction(f, stack.remove(stack.size() - 3), stack.remove(stack.size() - 2), stack.remove(stack.size() - 1));
                    break;
                case 4:
                    val = (double) inv.invokeFunction(f, stack.remove(stack.size() - 4), stack.remove(stack.size() - 3), stack.remove(stack.size() - 2), stack.remove(stack.size() - 1));
                    break;
                default:
                    throw new LaskinCalculationException("Unknown error");
            }
            
            if (ScriptAPI.getTerminated()) {
                throw new LaskinCalculationException(ScriptAPI.getTerminatedMessage());
            }
            
            return val;
        } catch (NoSuchMethodException e) {
            throw new LaskinCalculationException("Failed to call function (" + f + ")");
        } catch (ScriptException e) {
            throw new LaskinCalculationException("Error in function (" + f + ")");
        } catch (LaskinCalculationException e) {
            throw e;
        }
    }
}
