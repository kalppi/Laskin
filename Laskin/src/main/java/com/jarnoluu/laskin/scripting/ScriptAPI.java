package com.jarnoluu.laskin.scripting;

/**
 * Rajapinta jota skriptifunktiot voivat kutsua.
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class ScriptAPI {
    /**
     * Halusiko skriptifunktio lopettaa suorituksensa.
     */
    private static boolean terminated = false;
    
    /**
     * Viesti miksi skriptifunktio halusi lopettaa suorituksensa.
     */
    private static String terminatedMsg;
    
    /**
     * Kirjoittaa lokiin.
     * @param str lokiviesti.
     */
    public static void log(String str) {
        System.out.println(str);
    }
    
    /**
     * Ilmoittaa funktion haluavan lopettaa suoriuksensa.
     * @param msg syy lopetukselle.
     */
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
