package org.langbein.michael.soundboard.utils;

/**
 * Created by michael on 17.02.18.
 */

public class Complex {

    private double real;
    private double compl;

    public Complex(double r, double c) {
        real = r;
        compl = c;
    }

    @Override
    public String toString() {
        return "(" + real + " i*" + compl + ")";
    }

    public static Complex times(Complex first, Complex other) {
        return new Complex( first.getReal() * other.getReal() - first.getCompl() * other.getCompl(),
                            first.getReal() * other.getCompl() + first.getCompl() * other.getReal());
    }

    public static Complex times(Complex first, double other) {
        return new Complex(first.getReal() * other, first.getCompl() * other);
    }

    public static Complex plus(Complex first, Complex other) {
        return new Complex( first.getReal() + other.getReal(),
                            first.getCompl() + other.getCompl());
    }

    public static Complex minus(Complex first, Complex other) {
        return new Complex( first.getReal() - other.getReal(),
                            first.getCompl() - other.getCompl());
    }

    public Complex conjugate() {
        return new Complex(real, - compl);
    }

    public double getReal(){
        return real;
    }

    public double getCompl() {
        return compl;
    }

    public double getPower() {
        return real*real + compl*compl;
    }

    public static Complex exp(Complex c) {
        double fac1 = Math.exp(c.getReal());
        Complex fac2 = new Complex(Math.cos(c.getCompl()), Math.sin(c.getCompl()));
        Complex result = times(fac2, fac1);
        return result;
    }

    public static Complex[] transformToComplex(short[] shortArray) {
        Complex[] result = new Complex[shortArray.length];
        for(int n = 0; n<shortArray.length; n++){
            result[n] = new Complex(shortArray[n], 0);
        }
        return result;
    }
}
