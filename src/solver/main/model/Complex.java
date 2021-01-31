package solver.main.model;

import java.util.Objects;

public class Complex {
    public enum Type { REGULAR, NAN }

    public static final Complex NaN = new Complex(0, 0, Type.NAN);
    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);

    public static Complex parse(String in) {
        if ( (!in.contains("+") && !in.contains("-")) || !in.endsWith("i")) {
            return Complex.NaN;
        }

        in = in.substring(0, in.length() - 1); // Strip off "i"
        int plusPosition = in.indexOf('+');
        int minusPosition = in.indexOf('-');
        int position = plusPosition == -1 ? minusPosition : plusPosition;
        double real = Double.parseDouble(in.substring(0, position));
        double imaginary = Double.parseDouble(in.substring(position));

        return new Complex(real, imaginary);
    }

    private double real;
    private double imaginary;
    private final Type type;

    public Complex(double real, double imaginary, Type type) {
        this.real = real;
        this.imaginary = imaginary;
        this.type = type;
    }

    public Complex(double real, double imaginary) {
        this(real, imaginary, Type.REGULAR);
    }

    public Complex(Complex complex) {
        this(complex.getReal(), complex.getImaginary(), complex.getType());
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public Type getType() {
        return type;
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

        if (denominator == 0.0) {
            return Complex.NaN;
        }

        double originalReal = real;
        real = (real * complex.getReal() + imaginary * complex.getImaginary()) / denominator;
        imaginary = (complex.getReal() * imaginary - originalReal * complex.getImaginary()) / denominator;

        return this;
    }

    /**
     * Negative this complex number.
     * @return this number negated
     */
    public Complex negate() {
        real = -real;
        imaginary = -imaginary;

        return this;
    }

    public Complex inverse() {
        if (real == 0.0 || imaginary == 0.0) {
            return Complex.NaN;
        }

        real = 1 / real;
        imaginary = 1 /imaginary;

        return this;
    }

    public boolean isZero() {
        return real == 0.0 && imaginary == 0.0 && type != Type.NAN;
    }

    @Override
    public String toString() {
        if (type == Type.NAN) {
            return "NaN";
        }

        if (real == 0.0 && imaginary == 0.0) {
            return "0.0";
        }

        StringBuilder sb = new StringBuilder();

        if (real != 0.0) {
            sb.append(String.valueOf(real));
        }

        if (imaginary != 0.0) {
            if ((imaginary != 1.0 || real != 0.0) && imaginary >= 0.0) {
                sb.append('+');
            }

            if (imaginary == -1.0) {
                sb.append('-');
            } else if (imaginary != 1.0) {
                sb.append(String.valueOf(imaginary));
            }

            sb.append('i');
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        double delta = 0.000000001;
        if (getType() == Type.NAN || complex.getType() == Type.NAN) return false;
        return Math.abs(complex.getReal() - getReal()) < delta
                && Math.abs(complex.getImaginary() - getImaginary()) < delta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReal(), getImaginary(), getType());
    }
}
