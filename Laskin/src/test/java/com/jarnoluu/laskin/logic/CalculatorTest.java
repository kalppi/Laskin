package com.jarnoluu.laskin.logic;

import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.logic.Calculator;
import com.jarnoluu.laskin.exceptions.LaskinInvalidArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class CalculatorTest {
    private final Calculator calculator;
    
    public CalculatorTest() throws Exception {
        this.calculator = new Calculator();
    }

    @Test
    public void testCalculation() throws Exception {
        double val = this.calculator.calculate("5*15+2");
        
        assertEquals(77.0, val, 0.00001);
    }
    
    @Test(expected=LaskinInvalidArgumentException.class)
    public void testCalculationEmpty() throws Exception {
        this.calculator.calculate("");
    }
    
    @Test
    public void testCalculation2() throws Exception {
        double val = this.calculator.calculate("10+(2^10)-2");

        assertEquals(1032, val, 0.00001);
    }
    
    @Test
    public void testCalculation3() throws Exception {
        double val = this.calculator.calculate("10+(2^10-4)");

        assertEquals(1030, val, 0.00001);
    }
    
    @Test
    public void testCalculation4() throws Exception {
        double val = this.calculator.calculate(" 1+(2+   -2)");

        assertEquals(1, val, 0.00001);
    }
    
    @Test
    public void testCalculation5() throws Exception {
        double val = this.calculator.calculate("11%3+2");

        assertEquals(4, val, 0.00001);
    }
    
    @Test
    public void testCalculationBases() throws Exception {
        double val = this.calculator.calculate("0xff+0o12+0b101");

        assertEquals(270, val, 0.00001);
    }
    
    @Test
    public void testCalculationFunctions1() throws Exception {
        double val = this.calculator.calculate("1+abs(-1)");

        assertEquals(2, val, 0.00001);
    }
    
    @Test
    public void testCalculationFunctions2() throws Exception {
        double val = this.calculator.calculate("1+max(1, 2)");

        assertEquals(3, val, 0.00001);
    }
    
    @Test
    public void testFormatting() {
        assertEquals(Util.formatValue(2.23232512), "2.23233");
    }
}
