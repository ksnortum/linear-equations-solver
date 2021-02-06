package solver.main.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a complex number.  Immutable (I think).
 */
public class Complex {
    public enum Type { REGULAR, NAN }

    public static final Complex NaN = new Complex(BigDecimal.ZERO, BigDecimal.ZERO, Type.NAN);
    public static final Complex ZERO = new Complex(BigDecimal.ZERO, BigDecimal.ZERO);
    public static final Complex ONE = new Complex(BigDecimal.ONE, BigDecimal.ZERO);
    public static final Complex NEGATIVE_ONE = new Complex(new BigDecimal("-1"), BigDecimal.ZERO);

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

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;
    private static final int PLACES_TO_ROUND = 4;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    private static final int DIVIDE_SCALE = 128;

    public static Complex parse(String in) {
        if ("i".equals(in)) {
            return new Complex(BigDecimal.ZERO, BigDecimal.ONE);
        } else if ("-i".equals(in)) {
            return new Complex(BigDecimal.ZERO, new BigDecimal("-1"));
        } else if (!in.endsWith("i")) {
            return new Complex(new BigDecimal(in, MATH_CONTEXT));
        }

        // isn't i or -i and ends with "i"
        // look for n+i or n-i
        Matcher matcher = IMAGINARY_IS_ONE.matcher(in);

        if (matcher.matches()) {
            String realPart = matcher.group("realPart");
            String sign = matcher.group("sign");
            String imaginaryPart = "1";

            if ("-".equals(sign)) {
                imaginaryPart = "-1";
            }

            return new Complex(new BigDecimal(realPart, MATH_CONTEXT), new BigDecimal(imaginaryPart, MATH_CONTEXT));
        }

        // look for imaginary part <> 1 or -1
        matcher = IMAGINARY_ONLY.matcher(in);

        if (matcher.matches()) {
            String imaginaryPart = matcher.group("imaginaryPart");

            return new Complex(BigDecimal.ZERO, new BigDecimal(imaginaryPart, MATH_CONTEXT));
        }

        // both real and imaginary parts
        matcher = COMPLEX_NUMBER.matcher(in);

        if (!matcher.matches()) {
            Exception e = new RuntimeException(String.format("Can't match complex number (%s)%n", in));
            e.printStackTrace();

            return Complex.NaN;
        }

        String realPart = matcher.group("realPart");
        String imaginaryPart = matcher.group("imaginaryPart");

        return new Complex(new BigDecimal(realPart, MATH_CONTEXT), new BigDecimal(imaginaryPart, MATH_CONTEXT));
    }

    private final BigDecimal real;
    private final BigDecimal imaginary;
    private final Type type;

    public Complex(BigDecimal real, BigDecimal imaginary, Type type) {
        this.real = real;
        this.imaginary = imaginary;
        this.type = type;
    }

    public Complex(BigDecimal real, BigDecimal imaginary) {
        this(real, imaginary, Type.REGULAR);
    }

    public Complex(BigDecimal real) {
        this(real, BigDecimal.ZERO, Type.REGULAR);
    }

    public Complex(double real, double imaginary) {
        this(new BigDecimal(String.valueOf(real), MATH_CONTEXT),
                new BigDecimal(String.valueOf(imaginary), MATH_CONTEXT),
                Type.REGULAR);
    }

    public Complex(double real) {
        this(new BigDecimal(String.valueOf(real), MATH_CONTEXT), BigDecimal.ZERO, Type.REGULAR);
    }

    public Complex(Complex complex) {
        this(complex.getReal(), complex.getImaginary(), complex.getType());
    }

    public BigDecimal getReal() {
        return real;
    }

    public BigDecimal getImaginary() {
        return imaginary;
    }

    public Type getType() {
        return type;
    }

    /**
     * The {@code complex} parameter that is added to this complex number.
     * The parameter is not changed.
     * @param complex the complex number to add to this number
     * @return a new complex number
     */
    public Complex add(Complex complex) {
        return new Complex(real.add(complex.getReal()), imaginary.add(complex.getImaginary()));
    }

    /**
     * The {@code complex} parameter that is subtracted from this complex number.
     * The parameter is not changed. Note that order is important (this - param).
     * @param complex the complex number to subtract from this number
     * @return a new complex number
     */
    public Complex subtract(Complex complex) {
        return new Complex(real.subtract(complex.getReal()), imaginary.subtract(complex.getImaginary()));
    }

    /**
     * The {@code complex} parameter that is multiplied by this complex number.
     * The parameter is not changed.
     * @param complex the complex number to multiply by this number
     * @return a new complex number
     */
    public Complex multiply(Complex complex) {
        // newReal = real * complex.getReal() - imaginary * complex.getImaginary()
        BigDecimal newReal = (real.multiply(complex.getReal())).subtract(imaginary.multiply(complex.getImaginary()));

        // newImaginary = real * complex.getImaginary() + * imaginary * complex.getReal()
        BigDecimal newImaginary = (real.multiply(complex.getImaginary())).add(imaginary.multiply(complex.getReal()));

        return new Complex(newReal, newImaginary);
    }

    /**
     * The {@code complex} parameter that is divided by this complex number.
     * The parameter is not changed. Note that order is important (this / param).
     * @param complex the complex number to divide this number by
     * @return a new complex number, divided
     */
    public Complex divide(Complex complex) {
        // denominator = complex.getReal() * complex.getReal() + complex.getImaginary() * complex.getImaginary()
        BigDecimal denominator = (complex.getReal().multiply(complex.getReal()))
                .add(complex.getImaginary().multiply(complex.getImaginary()));
        BigDecimal denominatorToCompare = denominator.setScale(PLACES_TO_ROUND, ROUNDING_MODE);
        BigDecimal zeroToCompare = BigDecimal.ZERO.setScale(PLACES_TO_ROUND, ROUNDING_MODE);


        if (denominatorToCompare.equals(zeroToCompare)) {
            Exception e = new RuntimeException("Attempted division by zero");
            e.printStackTrace();

            return Complex.NaN;
        }

        // newReal = (real * complex.getReal() + imaginary * complex.getImaginary()) / denominator
        BigDecimal newReal = ((real.multiply(complex.getReal()))
                .add(imaginary.multiply(complex.getImaginary())))
                .divide(denominator, DIVIDE_SCALE, ROUNDING_MODE);

        // newImaginary = (complex.getReal() * imaginary - real * complex.getImaginary()) / denominator
        BigDecimal newImaginary = ((complex.getReal().multiply(imaginary))
                .subtract(real.multiply(complex.getImaginary())))
                .divide(denominator, DIVIDE_SCALE, ROUNDING_MODE);

        return new Complex(newReal, newImaginary);
    }

    /**
     * Negate this complex number.
     * @return a new number negated
     */
    public Complex negate() {
        return new Complex(real.negate(), imaginary.negate());
    }

    /**
     * Invert this complex number
     * @return a new number, inverted
     */
    // inverse = conjugate / a^2 - b^2, conjugate = a - bi
    public Complex inverse() {
        BigDecimal denominator = (real.multiply(real))
                .subtract(imaginary.multiply(imaginary).multiply(new BigDecimal("-1"))); // i^2 = -1

        if (denominator.equals(BigDecimal.ZERO)) {
            Exception e = new RuntimeException("Attempt to divide by zero");
            e.printStackTrace();

            return Complex.NaN;
        }

        return new Complex(real.divide(denominator, DIVIDE_SCALE, ROUNDING_MODE),
                imaginary.divide(denominator, DIVIDE_SCALE, ROUNDING_MODE)
                        .multiply(new BigDecimal("-1")));
    }

    public boolean realIsZero() {
        BigDecimal realToCompare = real.setScale(PLACES_TO_ROUND, ROUNDING_MODE);
        BigDecimal zeroToCompare = BigDecimal.ZERO.setScale(PLACES_TO_ROUND, ROUNDING_MODE);

        return realToCompare.equals(zeroToCompare);
    }

    public boolean imaginaryIsZero() {
        BigDecimal imaginaryToCompare = imaginary.setScale(PLACES_TO_ROUND, ROUNDING_MODE);
        BigDecimal zeroToCompare = BigDecimal.ZERO.setScale(PLACES_TO_ROUND, ROUNDING_MODE);

        return imaginaryToCompare.equals(zeroToCompare);
    }

    public boolean isZero() {
        return realIsZero() && imaginaryIsZero() && type != Type.NAN;
    }

    @Override
    public String toString() {
        if (type == Type.NAN) {
            return "NaN";
        }

        if (realIsZero()) {
            if (imaginaryIsZero()) {
                return "0";
            }

            if (imaginary.equals(BigDecimal.ONE)) {
                return "i";
            }

            if (imaginary.equals(new BigDecimal("-1"))) {
                return "-i";
            }
        }

        StringBuilder sb = new StringBuilder();

        if (!realIsZero()) {
            sb.append(removeTrailingZeros(real));
        }

        if (!imaginaryIsZero()) {
            // (x.compareTo(y) <op> 0), where <op> is one of the six comparison operators.
            if (!realIsZero() && imaginary.compareTo(BigDecimal.ZERO) > 0) {
                sb.append('+');
            }

            if (imaginary.equals(BigDecimal.ONE)) {
                sb.append('i');
            } else if (imaginary.equals(new BigDecimal("-1"))) {
                sb.append("-i");
            } else {
                sb.append(removeTrailingZeros(imaginary)).append('i');
            }
        }

        return sb.toString();
    }

    // Two RE matches, (\\d+)(\\.0+), and (\\d+\\.\\d+)(0+)
    private String removeTrailingZeros(BigDecimal in) {
        String out = in.setScale(PLACES_TO_ROUND, ROUNDING_MODE).toString();

        if (!out.contains(".") || out.charAt(out.length() - 1) != '0') {
            return out;
        }

        int index = out.length() - 2;

        while (out.charAt(index) != '.' && out.charAt(index) == '0') {
            index--;
        }

        if (out.charAt(index) == '.') {
            index--;
        }

        return out.substring(0, index + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getType() == Type.NAN) return false;
        if (o == null || getClass() != o.getClass()) return false;
        Complex other = (Complex) o;
        if (other.getType() == Type.NAN) return false;
        var realCompare = getReal().setScale(PLACES_TO_ROUND, ROUNDING_MODE);
        var otherRealCompare = other.getReal().setScale(PLACES_TO_ROUND, ROUNDING_MODE);
        var imaginaryCompare = getImaginary().setScale(PLACES_TO_ROUND, ROUNDING_MODE);
        var otherImaginaryCompare = other.getImaginary().setScale(PLACES_TO_ROUND, ROUNDING_MODE);

        return realCompare.equals(otherRealCompare) && imaginaryCompare.equals(otherImaginaryCompare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReal(), getImaginary(), getType());
    }
}
