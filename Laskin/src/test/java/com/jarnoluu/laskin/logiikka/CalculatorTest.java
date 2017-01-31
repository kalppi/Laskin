package com.jarnoluu.laskin.logiikka;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class CalculatorTest {
    private final Calculator calculator;
    
    public CalculatorTest() {
        this.calculator = new Calculator();
    }

    @Test
    public void testCalculation() throws Exception {
        double val = this.calculator.calculate("5*15+2");
        
        assertEquals(77.0, val, 0.00001);
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
        double val = this.calculator.calculate("2+max(2,3)");

        assertEquals(5, val, 0.00001);
    }
    
    @Test
    public void testCalculation5() throws Exception {
        double val = this.calculator.calculate(" 1+(2+   -2)");

        assertEquals(1, val, 0.00001);
    }
    
    @Test
    public void testCalculation6() throws Exception {
        double val = this.calculator.calculate("11%3+2");

        assertEquals(4, val, 0.00001);
    }
}
