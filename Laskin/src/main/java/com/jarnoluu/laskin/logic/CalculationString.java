package com.jarnoluu.laskin.logic;

import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.ui.events.CalculationStringChangeEvent;
import com.jarnoluu.laskin.ui.events.CalculationStringErrorStateChangeEvent;
import com.jarnoluu.laskin.exceptions.LaskinCalculationException;
import com.jarnoluu.laskin.exceptions.LaskinInvalidArgumentException;
import com.jarnoluu.laskin.exceptions.LaskinParseException;
import com.jarnoluu.laskin.ui.events.CalculationStringCalculateEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.javatuples.Pair;

/**
 * Luokka joka kuvaa laskulauseketta.
 * @author Jarno Luukkonen
 */
public abstract class CalculationString {
    /**
     * Laskennan hoivata laskin.
     */
    protected final Calculator calculator;
    
    /**
     * Lausekkeen muodostavat tokenit listana.
     */
    protected LinkedList<Token> tokens = new LinkedList();
    
    /**
     * Onko lausekeen laskemisessa tullut virhe.
     */
    protected boolean error = false;
    
    /**
     * Missä kohtaa liikutettava kursori on.
     */
    protected int cursor = 0;
    
    /**
     * Mikä osa lausekkeesta on valittuna. -1 = ei valintaa.
     */
    protected int selected = -1;
    
    /**
     * Pitää sisällään eri lukujärjestelmämuutoksia hoivat funktiot.
     */
    private final Map<String, List<Pair<Function<String, Boolean>, Function<String, String>>>> conversions = new HashMap();
    
    /**
     * Lista tapahtumankäsittelijöitä kun lauseke muuttuu.
     */
    private final List<CalculationStringChangeEvent> events = new LinkedList();
    
    /**
     * Lista tapahtumankäsittelijöitä kun laskemisessa tapahtuu virhe.
     */
    private final List<CalculationStringErrorStateChangeEvent> onErrorChange = new LinkedList();
    
    /**
     * Lista tapahtumankäsittelijöitä kun lausekkeen laskenta tapahtuu. 
     */
    private final List<CalculationStringCalculateEvent> onCalculate = new LinkedList();
    
    /**
     * Konstruktori.
     * @param calc laskin jota käytettään laskujen laskemiseen.
     */
    public CalculationString(Calculator calc) {
        this(calc, null);
    }
    
    /**
     * Konstruktori.
     * @param calculator laskin, jota käytettään laskujen laskemiseen.
     * @param calc laskulauseke
     */
    public CalculationString(Calculator calculator, String calc) {
        this.calculator = calculator;
        
        if (calc != null) {
            this.insertAtCursor(calc);
        }
        
        this.clear();
        
        List<Pair<Function<String, Boolean>, Function<String, String>>> decConv = new LinkedList();
        decConv.add(Pair.with(Util::isHexNumber, (String s) -> Util.formatValue(Util.hexToDouble(s))));
        decConv.add(Pair.with(Util::isBinaryNumber, (String s) -> Util.formatValue(Util.binToDouble(s))));
        decConv.add(Pair.with(Util::isOctalNumber, (String s) -> Util.formatValue(Util.octToDouble(s))));
        
        List<Pair<Function<String, Boolean>, Function<String, String>>> hexConv = new LinkedList();
        hexConv.add(Pair.with(Util::isIntegerNumber, (String s) -> "0x" + Integer.toHexString(Integer.parseInt(s))));
        hexConv.add(Pair.with(Util::isBinaryNumber, (String s) -> "0x" + Integer.toHexString((int) Util.binToDouble(s))));
        hexConv.add(Pair.with(Util::isOctalNumber, (String s) -> "0x" + Integer.toHexString((int) Util.octToDouble(s))));
        
        List<Pair<Function<String, Boolean>, Function<String, String>>> binConv = new LinkedList();
        binConv.add(Pair.with(Util::isIntegerNumber, (String s) -> "0b" + Integer.toBinaryString(Integer.parseInt(s))));
        binConv.add(Pair.with(Util::isHexNumber, (String s) -> "0b" + Integer.toBinaryString((int) Util.hexToDouble(s))));
        binConv.add(Pair.with(Util::isOctalNumber, (String s) -> "0b" + Integer.toBinaryString((int) Util.octToDouble(s))));
        
        List<Pair<Function<String, Boolean>, Function<String, String>>> octConv = new LinkedList();
        octConv.add(Pair.with(Util::isIntegerNumber, (String s) -> "0o" + Integer.toOctalString(Integer.parseInt(s))));
        octConv.add(Pair.with(Util::isBinaryNumber, (String s) -> "0o" + Integer.toOctalString((int) Util.binToDouble(s))));
        octConv.add(Pair.with(Util::isHexNumber, (String s) -> "0o" + Integer.toOctalString((int) Util.hexToDouble(s))));       
        
        this.conversions.put("dec", decConv);
        this.conversions.put("hex", hexConv);
        this.conversions.put("bin", binConv);
        this.conversions.put("oct", octConv);
    }
    
    public Calculator getCalculator() {
        return this.calculator;
    }
    
    public LinkedList<Token> getTokens() {
        return this.tokens;
    }
    
    public boolean getError() {
        return this.error;
    }
    
    protected void setError(boolean error) {
        if (this.error != error) {
            this.error = error;
            
            this.fireErrorStateChangeEvent();
        }
    }
    
    protected Map<String, List<Pair<Function<String, Boolean>, Function<String, String>>>> getConversions() {
        return this.conversions;
    }
    
    /**
     * Lisää tapahtumankäsittelijän sille, kun lasku muttuu.
     * @param e tapahtumankäsittelijä
     */
    public void addCalculationChangeListener(CalculationStringChangeEvent e) {
        this.events.add(e);
    }
    
    /**
     * Lisää tapahtumankäsittelijän sille, kun laskun aiheuttama virhetila muuttuu.
     * @param e tapahtumankäsittelijä
     */
    public void addErrorStateChangeListener(CalculationStringErrorStateChangeEvent e) {
        this.onErrorChange.add(e);
    }
    
    /**
     * Lisää tapahtumankäsittelijän sille, kun lasku lasketaan.
     * @param e tapahtumankäsittelijä
     */
    public void addCalculationListener(CalculationStringCalculateEvent e) {
        this.onCalculate.add(e);
    }
    
    protected void fireCalculationChangeEvent() {
        this.events.stream().forEach((e) -> {
            e.onCalculationChange();
        });
    }
    
    protected void fireErrorStateChangeEvent() {
        this.onErrorChange.stream().forEach((e) -> {
            e.onErrorStateChange(this.error);
        });
    }
    
    protected void fireCalculationEvent(double val, double old) {
        this.onCalculate.stream().forEach((e) -> {
            e.onCalculate(val, old);
        });
    }
    
    /**
     * Laskee lausekkeen tuloksen.
     * @return Lausekkeen tulos
     */
    public double calculate() {
        try {
            Double old = this.calculator.getLastValue();
            
            double val = this.calculator.calculate(this.getTokens());
            this.fireCalculationEvent(val, old);
            return val;
        } catch (LaskinParseException | LaskinCalculationException e) {
            e.printStackTrace();
            this.setError(true);
        } catch (LaskinInvalidArgumentException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    
    /**
     * Siirtää lausekkeen sisäistä kursoria.
     * @param s Kursorin paikka. 0 = oikea laita, 1 = siitä yksi vasemmalle jne.
     */
    public void setCursor(int s) {
        this.cursor = s;
    }
    
    public int getCursor() {
        return this.cursor;
    }
    
    public void setSelected(int s) {
        this.selected = s;
    }
    
    public int getSelected() {
        return this.selected;
    }
    
    /**
     * Kertoo lausekkeen pituuden.
     * @return laskulausekkeen pituus
     */
    abstract public int length();
    
    /**
     * Tyhjentää lausekkeen.
     */
    abstract public void clear();
    
    abstract public boolean insertAtCursor(String str);
    abstract public boolean insertAtCursor(char c);
    abstract public void eraseAtCursor();
    abstract public void eraseAtCursor(int n);
    abstract public void replaceWith(String str);
    abstract public void convertSelected(String to);
    abstract public void negateAtCursor();
    abstract public void functionalizeSelected();
    @Override
    abstract public String toString();
}
