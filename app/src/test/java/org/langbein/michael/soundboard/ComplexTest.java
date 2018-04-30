package org.langbein.michael.soundboard;

import org.junit.Test;
import org.langbein.michael.soundboard.utils.Complex;

import static junit.framework.Assert.assertTrue;

public class ComplexTest {

    @Test
    public void addTest() {
        Complex a = new Complex(1, 0);
        Complex b = new Complex(0, 1);
        Complex c = Complex.plus(a, b);
        assertTrue(c.getReal() == 1);
        assertTrue(c.getCompl() == 1);
    }

    @Test
    public void multTest() {
        Complex a = new Complex(1, 0);
        Complex b = new Complex(0, 1);
        Complex c = Complex.times(a, b);
        assertTrue(c.getReal() == 0);
        assertTrue(c.getCompl() == 1);
    }

    @Test
    public void expTest() {
        Complex a = new Complex(1, 0);
        Complex e = Complex.exp(a);
        assertTrue(Math.abs(Math.exp(1.0) - e.getReal()) < 0.01);

        Complex b = new Complex(0, 2.0*Math.PI);
        Complex f = Complex.exp(b);
        assertTrue(Math.abs(1 - f.getReal()) < 0.01);
        assertTrue(Math.abs(0 - f.getCompl()) <  0.01);
    }

}
