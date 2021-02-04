package solver.main.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a complex number.  Immutable.
 */
public class Complex {
    public enum Type { REGULAR, NAN }

    public static final Complex NaN = new Complex(0, 0, Type.NAN);
    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);

    private static final String RE_UNSIGNED_DECIMAL = "\\d+(\\.\\d+)?";
    private static final String RE_COMPLEX_NUMBER =
            "(?<realPart>" +
              "-?" +
              RE_UNSIGNED_DECIMAL +
            ")" +
            "(?<imaginaryPart>" +
              "[-+]" +
              RE_UNSIGNED_DECIMAL +
            ")" +
            "i";
    private static final Pattern COMPLEX_NUMBER = Pattern.compile(RE_COMPLEX_NUMBER);

    private static final String RE_IMAGINARY_IS_ONE=
            "(?<realPart>" +
              "-?" +
              RE_UNSIGNED_DECIMAL +
            ")" +
            "(?<sign>[-+])" +
            "i";
    private static final Pattern IMAGINARY_IS_ONE = Pattern.compile(RE_IMAGINARY_IS_ONE);

    private static final String RE_IMAGINARY_ONLY = "(?<imaginaryPart>-?" + RE_UNSIGNED_DECIMAL + ")i";
    private static final Pattern IMAGINARY_ONLY = Pattern.compile(RE_IMAGINARY_ONLY);

    private static final int PLACES_TO_ROUND = 9;
    private static final double EQUALITY_DELTA = 0.000000001;

    public static Complex parse(String in) {
        if ("i".equals(in)) {
            return new Complex(0, 1);
        } else if ("-i".equals(in)) {
            return new Complex(0, -1);
        } else if (!in.endsWith("i")) {
            return new Complex(Double.parseDouble(in), 0);
        }

        // isn't i or -i and ends with "i"
        // look for n+i or n-i
        Matcher matcher = IMAGINARY_IS_ONE.matcher(in);

        if (matcher.matches()) {
            String realPart = matcher.group("realPart");
            String sign = matcher.group("sign");
            double real = Double.parseDouble(realPart);
            double imaginary = 1.0;

            if ("-".equals(sign)) {
                imaginary = -1.0;
            }

            return new Complex(real, imaginary);
        }

        // look for imaginary part <> 1 or -1
        matcher = IMAGINARY_ONLY.matcher(in);

        if (matcher.matches()) {
            String imaginaryPart = matcher.group("imaginaryPart");
            double real = 0.0;
            double imaginary = Double.parseDouble(imaginaryPart);

            return new Complex(real, imaginary);
        }

        // both real and imaginary parts
        matcher = COMPLEX_NUMBER.matcher(in);

        if (!matcher.matches()) {
            System.err.printf("Complex::parse, can't match complex number (%s)%n", in);

            return Complex.NaN;
        }

        String realPart = matcher.group("realPart");
        double real = Double.parseDouble(realPart);
        String imaginaryPart = matcher.group("imaginaryPart");
        double imaginary = Double.parseDouble(imaginaryPart);

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

    public Complex(double real) {
        this(real, 0, Type.REGULAR);
    }

    public Complex(Complex complex) {
        this(complex.getReal(), complex.getImaginary(), complex.getType());
    }

    private static double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(PLACES_TO_ROUND, RoundingMode.HALF_UP);

        return bd.doubleValue();
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

    public Complex getRounded() {
        double newReal = round(real);
        double newImaginary = round(imaginary);

        return new Complex(newReal, newImaginary);
    }

    /**
     * The {@code complex} parameter that is added to this complex number.
     * The parameter is not changed.
     * @param complex the complex number to add to this number
     * @return a new complex number
     */
    public Complex add(Complex complex) {
        return new Complex(real + complex.getReal(), imaginary + complex.getImaginary());
    }

    /**
     * The {@code complex} parameter that is subtracted from this complex number.
     * The parameter is not changed. Note that order is important (this - param).
     * @param complex the complex number to subtract from this number
     * @return a new complex number
     */
    public Complex subtract(Complex complex) {
        return new Complex(real - complex.getReal(), imaginary - complex.getImaginary());
    }

    /**
     * The {@code complex} parameter that is multiplied by this complex number.
     * The parameter is not changed.
     * @param complex the complex number to multiply by this number
     * @return a new complex number
     */
    public Complex multiply(Complex complex) {
        double newReal = real * complex.getReal() - imaginary * complex.getImaginary();
        double newImaginary = real * complex.getImaginary() + complex.getReal() * imaginary;

        return new Complex(newReal, newImaginary);
    }

    /**
     * The {@code complex} parameter that is divided by this complex number.
     * The parameter is not changed. Note that order is important (this / param).
     * @param complex the complex number to divide this number by
     * @return a new complex number, divided
     */
    public Complex divide(Complex complex) {
        double denominator = complex.getReal() * complex.getReal() + complex.getImaginary() * complex.getImaginary();

        if (denominator == 0.0) {
            System.err.println("Complex::divide, attempted division by zero");

            return Complex.NaN;
        }

        double newReal = (real * complex.getReal() + imaginary * complex.getImaginary()) / denominator;
        double newImaginary = (complex.getReal() * imaginary - real * complex.getImaginary()) / denominator;

        return new Complex(newReal, newImaginary);
    }

    /**
     * Negate this complex number.
     * @return a new number negated
     */
    public Complex negate() {
        return new Complex(-real, -imaginary);
    }

    /**
     * Invert this complex number
     * @return a new number, inverted
     */
    // inverse = conjugate / a^2 - b^2, conjugate = a - bi
    public Complex inverse() {
        double denominator = real * real - imaginary * imaginary * -1; // i^2 = -1

        if (round(denominator) == 0.0) {
            System.err.println("Complex::inverse, attempt to divide by zero");

            return Complex.NaN;
        }

        return new Complex(real / denominator, imaginary / denominator * -1);
    }

    public boolean isZero() {
        return round(real) == 0.0 && round(imaginary) == 0.0 && type != Type.NAN;
    }

    @Override
    public String toString() {
        double roundedReal = round(real);
        double roundedImaginary = round(imaginary);

        if (type == Type.NAN) {
            return "NaN";
        } else if (roundedReal == 0.0 && roundedImaginary == 0.0) {
            return "0";
        } else if (roundedReal == 0.0 && roundedImaginary == 1.0) {
            return "i";
        } else if (roundedReal == 0.0 && roundedImaginary == -1.0) {
            return "-i";
        }

        StringBuilder sb = new StringBuilder();

        if (roundedReal != 0.0) {
            sb.append(evenNumbers(roundedReal));
        }

        if (roundedImaginary != 0.0) {
            if (roundedReal != 0.0 && roundedImaginary > 0.0) {
                sb.append('+');
            }

            if (roundedImaginary == 1.0) {
                sb.append('i');
            } else if (roundedImaginary == -1.0) {
                sb.append("-i");
            } else {
                sb.append(evenNumbers(roundedImaginary)).append('i');
            }
        }

        return sb.toString();
    }

    private String evenNumbers(double in) {
        String out = String.valueOf(in);

        if (out.endsWith(".0")) {
            out = out.substring(0, out.length() - 2);
        }

        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        double delta = 0.000000001;
        if (getType() == Type.NAN || complex.getType() == Type.NAN) return false;
        return Math.abs(complex.getReal() - getReal()) < EQUALITY_DELTA
                && Math.abs(complex.getImaginary() - getImaginary()) < EQUALITY_DELTA;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReal(), getImaginary(), getType());
    }
}
