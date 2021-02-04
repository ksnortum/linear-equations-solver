package solver.test;

import solver.main.model.Complex;
import solver.main.model.Matrix;
import solver.main.model.MatrixRow;
import solver.main.model.Swap;

import java.util.Arrays;
import java.util.List;

public class TestMatrix {
    private List<Complex> row;
    private Matrix matrix;
    private Matrix expected;

    public static void main(String[] args) {
        new TestMatrix().run();
    }

    private void run() {
        testCreateMultiplier();
        setup();
        testZeroTarget();
        setup();
        testMultiply();
        setup();
        testRowAndColumnSwap();
        setup();
        testRowOnlySwap();
        setup();
        testColumnOnlySwap();

        System.out.printf("All tests in %s have been completed%n", getClass());
    }

    private void setup() {
        matrix = new Matrix();
        expected = new Matrix();
    }

    private void testCreateMultiplier() {
        Complex source = new Complex(2.7, 1.5);
        Complex target = new Complex(8.5, 3.4);
        Complex expected = new Complex(-2.940251572, 0.374213836);
        Complex actual = Matrix.createMultiplier(source, target);
        assert(expected.equals(actual));
    }

    private void testZeroTarget() {
        row = Arrays.asList(new Complex(2.7, 1.5), new Complex(3.2, 2.1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(8.5, 3.4), new Complex(9.1, 7.3));
        matrix.add(new MatrixRow(row));

        expected = new Matrix();
        row = Arrays.asList(new Complex(2.7, 1.5), new Complex(3.2, 2.1));
        expected.add(new MatrixRow(row));
        row = Arrays.asList(Complex.ZERO, new Complex(-1.094654088, 2.322955975));
        expected.add(new MatrixRow(row));

        int sourceIndex = 0;
        int targetIndex = 1;
        Complex sourceCoefficient = new Complex(matrix.getCoefficient(sourceIndex, 0));
        Complex targetCoefficient = new Complex(matrix.getCoefficient(targetIndex, 0));
        Complex complexMultiplier = Matrix.createMultiplier(sourceCoefficient, targetCoefficient);
        matrix.zeroTarget(sourceIndex, targetIndex, complexMultiplier);
        assert(expected.equals(matrix));
    }

    private void testMultiply() {
        row = Arrays.asList(new Complex(2.7, 1.5), new Complex(3.2, 2.1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(8.5, 3.4), new Complex(9.1, 7.3));
        matrix.add(new MatrixRow(row));
        matrix.multiplyRow(0, new Complex(5.3, 2.9));

        row = Arrays.asList(new Complex(9.96, 15.78), new Complex(10.87, 20.41));
        expected.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(8.5, 3.4), new Complex(9.1, 7.3));
        expected.add(new MatrixRow(row));
        assert(expected.equals(matrix));
    }

    private void testRowAndColumnSwap() {
        row = Arrays.asList(new Complex(2.7, 1.5), new Complex(3.2, 2.1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(8.5, 3.4), new Complex(9.1, 7.3));
        matrix.add(new MatrixRow(row));
        matrix.swap(new Swap(1,1,0,0));

        row = Arrays.asList(new Complex(9.1, 7.3), new Complex(3.2, 2.1));
        expected.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(8.5, 3.4), new Complex(2.7, 1.5));
        expected.add(new MatrixRow(row));
        assert(expected.equals(matrix));
    }

    private void testRowOnlySwap() {
        row = Arrays.asList(new Complex(2.7, 1.5), new Complex(3.2, 2.1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(8.5, 3.4), new Complex(9.1, 7.3));
        matrix.add(new MatrixRow(row));
        matrix.swap(new Swap(1,0,0,0));

        row = Arrays.asList(new Complex(8.5, 3.4), new Complex(9.1, 7.3));
        expected.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(2.7, 1.5), new Complex(3.2, 2.1));
        expected.add(new MatrixRow(row));
        assert(expected.equals(matrix));
    }

    private void testColumnOnlySwap() {
        row = Arrays.asList(new Complex(2.7, 1.5), new Complex(3.2, 2.1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(8.5, 3.4), new Complex(9.1, 7.3));
        matrix.add(new MatrixRow(row));
        matrix.swap(new Swap(0,1,0,0));

        row = Arrays.asList(new Complex(3.2, 2.1), new Complex(2.7, 1.5));
        expected.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(8.5, 3.4), new Complex(9.1, 7.3));
        expected.add(new MatrixRow(row));
        assert(expected.equals(matrix));
    }
}
