package solver.test;

import solver.main.model.Complex;
import solver.main.model.MatrixRow;
import solver.main.model.Swap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestMatrixRow {
    private List<Complex> row;
    private MatrixRow matrixRow;
    private MatrixRow expected;
    private MatrixRow actual;

    public static void main(String[] args) {
        new TestMatrixRow().run();
    }

    private void run() {
        setup();
        testConstructors();
        setup();
        testMultiply();
        setup();
        testAddFrom();
        setup();
        testSwap();

        System.out.printf("All tests in %s have been completed%n", getClass());
    }

    private void setup() {
        row = new ArrayList<>();
        matrixRow = new MatrixRow();
        expected = new MatrixRow();
        actual = new MatrixRow();
    }

    private void testConstructors() {
        row = Arrays.asList(Complex.ONE, new Complex(2), new Complex(3), new Complex(4));
        List<Complex> rowCopy = Arrays.asList(Complex.ONE, new Complex(2), new Complex(3),
                new Complex(4));
        matrixRow = new MatrixRow(row);
        MatrixRow matrixRowCopy = new MatrixRow(rowCopy);
        row.set(0, new Complex(4));
        // Changing the List this MatrixRow was created with should not change the MatrixRow itself
        assert(matrixRow.equals(matrixRowCopy));

        MatrixRow matrixRow1 = new MatrixRow(matrixRow);
        matrixRow = matrixRow.multiply(new Complex(2));
        // Copy constructor object should not change when the MatrixRow used to create it changes
        assert(matrixRow1.equals(matrixRowCopy));
    }

    private void testMultiply() {
        // Test integers
        row = Arrays.asList(Complex.ONE, new Complex(2), new Complex(3), new Complex(4));
        matrixRow = new MatrixRow(row);
        row = Arrays.asList(new Complex(2), new Complex(4), new Complex(6), new Complex(8));
        expected = new MatrixRow(row);
        actual = matrixRow.multiply(new Complex(2));
        assert(actual != matrixRow);
        assert(expected.equals(actual));

        // Test Complex
        row = Arrays.asList(new Complex(2, 1), new Complex(3, 2));
        matrixRow = new MatrixRow(row);
        row = Arrays.asList(new Complex(8, 9), new Complex(11, 16));
        expected = new MatrixRow(row);
        actual = matrixRow.multiply(new Complex(5, 2));
        assert(actual != expected && expected.equals(actual));

        // Test Complex with doubles
        row = Arrays.asList(new Complex(2.7, 1.5), new Complex(3.2, 2.1));
        matrixRow = new MatrixRow(row);
        row = Arrays.asList(new Complex(9.96, 15.78), new Complex(10.87, 20.41));
        expected = new MatrixRow(row);
        actual = matrixRow.multiply(new Complex(5.3, 2.9));
        assert(actual != expected && expected.equals(actual));
    }

    private void testAddFrom() {
        row = Arrays.asList(new Complex(2.7, 1.5), new Complex(-3.2, 2.1));
        matrixRow = new MatrixRow(row);
        row = Arrays.asList(new Complex(8.5, -3.4), new Complex(9.1, 7.3));
        MatrixRow affectingRow = new MatrixRow(row);
        row = Arrays.asList(new Complex(11.2, -1.9), new Complex(5.9, 9.4));
        expected = new MatrixRow(row);
        assert(expected.equals(matrixRow.addFrom(affectingRow)));
    }

    private void testSwap() {
        row = Arrays.asList(new Complex(2.7, 1.5), new Complex(3.2, 2.1),
                new Complex(5.5, 7.9));
        matrixRow = new MatrixRow(row);
        Swap swap = new Swap(0, 0, 0, 2);
        row = Arrays.asList(new Complex(5.5, 7.9), new Complex(3.2, 2.1),
                new Complex(2.7, 1.5));
        expected = new MatrixRow(row);
        assert(expected.equals(matrixRow.swapColumn(swap)));
    }
}
