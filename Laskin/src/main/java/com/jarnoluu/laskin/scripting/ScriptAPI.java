package com.jarnoluu.laskin.scripting;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class ScriptAPI {
    private static boolean terminated = false;
    private static String terminatedMsg;
    
    public static void log(String str) {
        System.out.println(str);
    }
    
    public static double terminate(String msg) {
        ScriptAPI.terminated = true;
        ScriptAPI.terminatedMsg = msg;
        return 0.0;
    }
    
    public static boolean getTerminated() {
        boolean t = ScriptAPI.terminated;
        ScriptAPI.terminated = false;
        return t;
    }
    
    public static String getTerminatedMessage() {
        return ScriptAPI.terminatedMsg;
    }
}
