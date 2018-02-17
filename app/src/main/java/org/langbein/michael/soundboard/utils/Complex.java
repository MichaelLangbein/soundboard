package org.langbein.michael.soundboard.utils;

/**
 * Created by michael on 17.02.18.
 */

class Complex {

    private double real;
    private double compl;

    public Complex(double r, double c) {
        real = r;
        compl = c;
    }

    public Complex times(Complex other) { // @TODO: das hier stimmt nicht...
        return new Complex(real * other.getReal(), compl * other.getCompl());
    }

    public Complex plus(Complex other) {
        return new Complex(real + other.getReal(), compl + other.getCompl());
    }

    public Complex minus(Complex other) {
        return new Complex(real - other.getReal(), compl - other.getCompl());
    }

    public Complex conjugate() {
        return new Complex(real, - compl);
    }

    public Complex scale(double v) {
        return new Complex(real * v, compl * v);
    }

    public double getReal(){
        return real;
    }

    public double getCompl() {
        return compl;
    }
}
