package solver.main.model;

import java.util.Objects;

public class Complex {
    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex(Complex complex) {
        this.real = complex.getReal();
        this.imaginary = complex.getImaginary();
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    /**
     * The {@code complex} parameter that is added to this complex number.
     * The parameter is not changed.
     * @param complex the complex number to add to this number
     * @return this complex number
     */
    public Complex add(Complex complex) {
        real += complex.getReal();
        imaginary += complex.getImaginary();

        return this;
    }

    /**
     * The {@code complex} parameter that is subtracted from this complex number.
     * The parameter is not changed. Note that order is important (this - param).
     * @param complex the complex number to subtract from this number
     * @return this complex number
     */
    public Complex subtract(Complex complex) {
        real -= complex.getReal();
        imaginary -= complex.getImaginary();

        return this;
    }

    /**
     * The {@code complex} parameter that is multiplied by this complex number.
     * The parameter is not changed.
     * @param complex the complex number to multiply by this number
     * @return this complex number
     */
    public Complex multiply(Complex complex) {
        double originalReal = real;
        real = real * complex.getReal() - imaginary * complex.getImaginary();
        imaginary = originalReal * complex.getImaginary() + complex.getReal() * imaginary;

        return this;
    }

    /**
     * The {@code complex} parameter that is divided by this complex number.
     * The parameter is not changed. Note that order is important (this / param).
     * @param complex the complex number to divide this number by
     * @return this complex number
     */
    public Complex divide(Complex complex) {
        double denominator = complex.getReal() * complex.getReal() + complex.getImaginary() * complex.getImaginary();
        double originalReal = real;
        real = (real * complex.getReal() + imaginary * complex.getImaginary()) / denominator;
        imaginary = (complex.getReal() * imaginary - originalReal * complex.getImaginary()) / denominator;

        return this;
    }

    @Override
    public String toString() {
        return String.format("Complex{real=%s, imaginary=%s", real, imaginary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        double delta = 0.000000001;
        return Math.abs(complex.getReal() - getReal()) < delta
                && Math.abs(complex.getImaginary() - getImaginary()) < delta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReal(), getImaginary());
    }
}
