package solver.test;

import solver.main.logic.NonZeroCoefficientFinder;
import solver.main.model.Complex;
import solver.main.model.Matrix;
import solver.main.model.MatrixRow;
import solver.main.model.Swap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestNonZeroCoefficientFinder {
    private Matrix matrix;
    private List<Complex> row;
    private final NonZeroCoefficientFinder finder = new NonZeroCoefficientFinder();

    public static void main(String[] args) {
        new TestNonZeroCoefficientFinder().run();
    }

    private void run() {
        setup();
        testWhenSwappingRow();
        setup();
        testWhenSwappingColumn();
        setup();
        testWhenSwappingRowAndColumn();
        setup();
        testWhenNoSwapIsPossible();

        System.out.printf("All tests in %s have been completed%n", getClass());
    }

    private void setup() {
        matrix = new Matrix();
        row = new ArrayList<>();
    }

    private void testWhenSwappingRow() {
        row = Arrays.asList(Complex.ZERO, Complex.ONE, new Complex(2), new Complex(3), new Complex(4));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(Complex.ONE, new Complex(2), new Complex(3), new Complex(4),
                new Complex(5));
        matrix.add(new MatrixRow(row));
        Swap expected = new Swap(1, 0, 0, 0);
        Swap actual = finder.findNonZeroCoefficient(matrix, 0, 0);
        assert(expected.equals(actual));
    }

    private void testWhenSwappingColumn() {
        row = Arrays.asList(Complex.ZERO, Complex.ONE, new Complex(2), new Complex(3), new Complex(4));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(Complex.ZERO, new Complex(2), new Complex(3), new Complex(4),
                new Complex(5));
        matrix.add(new MatrixRow(row));
        Swap expected = new Swap(0, 1, 0, 0);
        Swap actual = finder.findNonZeroCoefficient(matrix, 0, 0);
        assert(expected.equals(actual));
    }

    private void testWhenSwappingRowAndColumn() {
        row = Arrays.asList(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO);
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(Complex.ZERO, new Complex(2), new Complex(3), new Complex(4),
                new Complex(5));
        matrix.add(new MatrixRow(row));
        Swap expected = new Swap(1, 1, 0, 0);
        Swap actual = finder.findNonZeroCoefficient(matrix, 0, 0);
        assert(expected.equals(actual));
    }

    private void testWhenNoSwapIsPossible() {
        row = Arrays.asList(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO);
        matrix.add(new MatrixRow(row));
        matrix.add(new MatrixRow(row));
        Swap expected = Swap.empty();
        Swap actual = finder.findNonZeroCoefficient(matrix, 0, 0);
        assert(expected.equals(actual));
    }

}