package solver.test;

import solver.main.model.Complex;

public class TestComplex {
    public static void main(String[] args) {
        new TestComplex().run();
    }

    private void run() {
        setup();
        testConstructors();
        testToString();
        testParse();
        testAdditionIntegers();
        testAdditionDoubles();
        testSubtractionDoubles();
        testMultiplicationDoubles();
        testDivisionDoubles();
        testChainAdditionAndDivision();
        testNaN();
        testNegate();
        testInverse();

        System.out.printf("All tests in %s have been completed%n", getClass());
    }

    private void setup() {
    }

    private void testConstructors() {
        Complex actual = new Complex(1, 2);
        assert(actual.getReal() == 1 && actual.getImaginary() == 2);
        Complex copy = new Complex(actual);
        assert(actual != copy && actual.equals(copy)); // Warning OK
    }

    private void testToString() {
        String actual;
        assert("0".equals(Complex.ZERO.toString()));
        assert("1".equals(Complex.ONE.toString()));
        assert("-2.15".equals(new Complex(-2.15, 0).toString()));

        actual = new Complex(0, 1).toString();
        assert("i".equals(actual));
        actual = new Complex(0, -1).toString();
        assert("-i".equals(actual));

        actual = new Complex(0, 2).toString();
        assert("2i".equals(actual));
        actual = new Complex(0, -2).toString();
        assert("-2i".equals(actual));

        actual = new Complex(0, 2.5).toString();
        assert("2.5i".equals(actual));
        actual = new Complex(0, -2.5).toString();
        assert("-2.5i".equals(actual));

        actual = new Complex(1, 1).toString();
        assert("1+i".equals(actual));
        actual = new Complex(1, -1).toString();
        assert("1-i".equals(actual));

        actual = new Complex(1, 2).toString();
        assert("1+2i".equals(actual));
        actual = new Complex(1, -2).toString();
        assert("1-2i".equals(actual));

        actual = new Complex(1.5, 2.5).toString();
        assert("1.5+2.5i".equals(actual));
        actual = new Complex(1.5, -2.5).toString();
        assert("1.5-2.5i".equals(actual));
    }

    private void testParse() {
        Complex expected;
        Complex actual;
        assert(Complex.ZERO.equals(Complex.parse("0.0")));
        assert(Complex.ONE.equals(Complex.parse("1.0")));
        expected = new Complex(-2.15, 0);
        assert(expected.equals(Complex.parse("-2.15")));
        expected = new Complex(0, 1);
        assert(expected.equals(Complex.parse("i")));
        expected = new Complex(0, -1);
        assert(expected.equals(Complex.parse("-i")));

        expected = new Complex(0, 2);
        actual = Complex.parse("2.0i");
        assert(expected.equals(actual));

        expected = new Complex(1, 1);
        actual = Complex.parse("1.0+i");
        assert(expected.equals(actual));

        expected = new Complex(1, -1);
        actual = Complex.parse("1.0-i");
        assert(expected.equals(actual));

        expected = new Complex(1, 2);
        actual = Complex.parse("1.0+2.0i");
        assert(expected.equals(actual));

        expected = new Complex(1, -2);
        actual = Complex.parse("1.0-2.0i");
        assert(expected.equals(actual));
    }

    private void testAdditionIntegers() {
        Complex actual = new Complex(1, 2);
        Complex addend = new Complex(2, 3);
        Complex expected = new Complex(3, 5);
        assert(expected.equals(actual.add(addend)));
        // addend should not be affected
        assert(addend.equals(new Complex(2, 3)));
    }

    private void testAdditionDoubles() {
        Complex actual = new Complex(0.25, 1.75);
        Complex addend = new Complex(0.25, 0.25);
        Complex expected = new Complex(0.5, 2);
        assert(expected.equals(actual.add(addend)));
        // addend should not be affected
        assert(addend.equals(new Complex(0.25, 0.25)));
    }

    private void testSubtractionDoubles() {
        Complex actual = new Complex(0.25, 1.75);
        Complex subtrahend = new Complex(0.25, 0.25);
        Complex expected = new Complex(0, 1.5);
        assert(expected.equals(actual.subtract(subtrahend)));
        // subtrahend should not be affected
        assert(subtrahend.equals(new Complex(0.25, 0.25)));
    }

    private void testMultiplicationDoubles() {
        Complex actual = new Complex(0.25, 1.75);
        Complex multiplier = new Complex(5, 2);
        Complex expected = new Complex(-2.25, 9.25);
        assert(expected.equals(actual.multiply(multiplier)));
        // multiplier should not be affected
        assert(multiplier.equals(new Complex(5, 2)));
    }

    private void testDivisionDoubles() {
        Complex actual = new Complex(0.25, 1.75);
        Complex divisor = new Complex(5, 2);
        Complex expected = new Complex(0.163793103, 0.284482759);
        assert(expected.equals(actual.divide(divisor)));
        // divisor should not be affected
        assert(divisor.equals(new Complex(5, 2)));
    }

    private void testChainAdditionAndDivision() {
        Complex actual = new Complex(0.25, 1.75);
        Complex addend = new Complex(4.2, 3.7);
        Complex divisor = new Complex(3.8, 7.5);
        Complex sumOfDivision = actual.add(addend).divide(divisor);
        Complex expected = new Complex(0.817442354, -0.17916254);
        assert(expected.equals(sumOfDivision));
        assert(addend.equals(new Complex(4.2, 3.7)));
        assert(divisor.equals(new Complex(3.8, 7.5)));
    }

    private void testNaN() {
        Complex notANumber = new Complex(0, 0, Complex.Type.NAN);
        assert(notANumber.toString().equals(Complex.NaN.toString()));
    }

    private void testNegate() {
        Complex actual = new Complex(0.25, 1.75);
        Complex expected = new Complex(-0.25, -1.75);
        assert(expected.equals(actual.negate()));
    }

    private void testInverse() {
        Complex expected = new Complex(0.1, -0.2);
        Complex actual = new Complex(2, 4);
        assert(expected.equals(actual.inverse()));
    }
}
