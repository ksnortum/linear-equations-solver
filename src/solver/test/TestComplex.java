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
        String expected;
        assert("0.0".equals(Complex.ZERO.toString()));
        assert("1.0".equals(Complex.ONE.toString()));
        assert("-2.15".equals(new Complex(-2.15, 0).toString()));
        expected = new Complex(0, 1).toString();
        System.out.println(expected); // TODO debug
        assert("i".equals(expected));
        expected = new Complex(0, -1).toString();
        System.out.println(expected); // TODO debug
        assert("-i".equals(expected));
        expected = new Complex(1, 1).toString();
        System.out.println(expected); // TODO debug
        assert("1.0+i".equals(expected));
        expected = new Complex(1, -1).toString();
        System.out.println(expected); // TODO debug
        assert("1.0-i".equals(expected));
        assert("1.0+2.0i".equals(new Complex(1, 2).toString()));
        expected = new Complex(1, -2).toString();
        System.out.println(expected); // TODO debug
        assert("1.0-2.0i".equals(expected));
    }

    private void testParse() {
        // TODO
    }

    private void testAdditionIntegers() {
        Complex actual = new Complex(1, 2);
        Complex addend = new Complex(2, 3);
        Complex expected = new Complex(3, 5);
        actual.add(addend);
        assert(expected.equals(actual));
        // addend should not be affected
        assert(addend.equals(new Complex(2, 3)));
    }

    private void testAdditionDoubles() {
        Complex actual = new Complex(0.25, 1.75);
        Complex addend = new Complex(0.25, 0.25);
        Complex expected = new Complex(0.5, 2);
        actual.add(addend);
        assert(expected.equals(actual));
        // addend should not be affected
        assert(addend.equals(new Complex(0.25, 0.25)));
    }

    private void testSubtractionDoubles() {
        Complex actual = new Complex(0.25, 1.75);
        Complex subtrahend = new Complex(0.25, 0.25);
        Complex expected = new Complex(0, 1.5);
        actual.subtract(subtrahend);
        assert(expected.equals(actual));
        // subtrahend should not be affected
        assert(subtrahend.equals(new Complex(0.25, 0.25)));
    }

    private void testMultiplicationDoubles() {
        Complex actual = new Complex(0.25, 1.75);
        Complex multiplier = new Complex(5, 2);
        Complex expected = new Complex(-2.25, 9.25);
        actual.multiply(multiplier);
        assert(expected.equals(actual));
        // multiplier should not be affected
        assert(multiplier.equals(new Complex(5, 2)));
    }

    private void testDivisionDoubles() {
        Complex actual = new Complex(0.25, 1.75);
        Complex divisor = new Complex(5, 2);
        Complex expected = new Complex(0.163793103, 0.284482759);
        actual.divide(divisor);
        assert(expected.equals(actual));
        // divisor should not be affected
        assert(divisor.equals(new Complex(5, 2)));
    }

    private void testChainAdditionAndDivision() {
        Complex actual = new Complex(0.25, 1.75);
        Complex addend = new Complex(4.2, 3.7);
        Complex divisor = new Complex(3.8, 7.5);
        actual.add(addend).divide(divisor);
        Complex expected = new Complex(0.817442354, -0.17916254);
        assert(expected.equals(actual));
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
        actual.negate();
        assert(expected.equals(actual));
    }

    private void testInverse() {
        // TODO
    }
}
