package solver.test;

import solver.main.logic.SolutionChecker;
import solver.main.model.Complex;
import solver.main.model.Matrix;
import solver.main.model.MatrixRow;
import solver.main.control.Executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSolutionChecker  {
    private Matrix matrix;
    private List<Complex> row;
    private final SolutionChecker checker = new SolutionChecker();

    public static void main(String[] args) {
        new TestSolutionChecker().run();
    }

    private void run() {
        setup();
        testWhenThereIsASolution();
        setup();
        testWhenZeroRowButNonZeroConstant();
        setup();
        testWhenExtraZeroRow();
        setup();
        testWhenExtraZeroColumn();
        setup();
        testWhenEquationsLessThanVariables();

        System.out.printf("All tests in %s have been completed%n", getClass());
    }

    private void setup() {
        matrix = new Matrix();
        row = new ArrayList<>();
    }

    private void testWhenThereIsASolution() {
        row = Arrays.asList(new Complex(4), new Complex(2), new Complex(-1), new Complex(1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(5), new Complex(3), new Complex(-2), new Complex(2));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(3), new Complex(2), new Complex(-3), Complex.ZERO);
        matrix.add(new MatrixRow(row));
        Executor.SolutionState expected = Executor.SolutionState.SOLUTION;
        Executor.SolutionState actual = checker.checkForSolution(matrix, 3);
        assert(expected == actual);
    }

    private void testWhenZeroRowButNonZeroConstant() {
        row = Arrays.asList(new Complex(4), new Complex(2), new Complex(-1), new Complex(1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(5), new Complex(3), new Complex(-2), new Complex(2));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE);
        matrix.add(new MatrixRow(row));
        Executor.SolutionState expected = Executor.SolutionState.NO_SOLUTION;
        Executor.SolutionState actual = checker.checkForSolution(matrix, 3);
        assert(expected == actual);
    }

    private void testWhenExtraZeroRow() {
        row = Arrays.asList(new Complex(4), new Complex(2), new Complex(-1), new Complex(1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(5), new Complex(3), new Complex(-2), new Complex(2));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(3), new Complex(2), new Complex(-3), Complex.ZERO);
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO);
        matrix.add(new MatrixRow(row));
        Executor.SolutionState expected = Executor.SolutionState.SOLUTION;
        Executor.SolutionState actual = checker.checkForSolution(matrix, 3);
        assert(expected == actual);
    }

    private void testWhenExtraZeroColumn() {
        row = Arrays.asList(new Complex(4), new Complex(2), new Complex(-1), Complex.ZERO, Complex.ONE);
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(5), new Complex(3), new Complex(-2), Complex.ZERO,
                new Complex(2));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(3), new Complex(2), new Complex(-3), Complex.ZERO, Complex.ZERO);
        matrix.add(new MatrixRow(row));
        Executor.SolutionState expected = Executor.SolutionState.SOLUTION;
        Executor.SolutionState actual = checker.checkForSolution(matrix, 3);
        assert(expected == actual);
    }

    private void testWhenEquationsLessThanVariables() {
        row = Arrays.asList(new Complex(4), new Complex(2), new Complex(-1), new Complex(1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(5), new Complex(3), new Complex(-2), new Complex(2));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO);
        matrix.add(new MatrixRow(row));
        Executor.SolutionState expected = Executor.SolutionState.INFINITE_SOLUTIONS;
        Executor.SolutionState actual = checker.checkForSolution(matrix, 3);
        assert(expected == actual);
    }
}