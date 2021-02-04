package solver.test;

import solver.main.control.Executor;
import solver.main.logic.EquationSolver;
import solver.main.model.Complex;
import solver.main.model.Matrix;
import solver.main.model.MatrixRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestEquationSolver {
    private Matrix matrix;
    private List<Complex> row;

    public static void main(String[] args) {
        new TestEquationSolver().run();
    }

    private void run() {
        setup();
        testSolveSolutionReal();
        System.out.println("***"); // TODO, debug
        setup();
        testSolveNoSolutionReal();
        System.out.println("***"); // TODO debug
        setup();
        testSolveSolutionComplex();

        System.out.printf("All tests in %s have been completed%n", getClass());
    }

    private void setup() {
        matrix = new Matrix();
        row = new ArrayList<>();
    }

    /*
    3 3
1 1 2 9
2 4 -3 1
3 6 -5 0
     */

    private void testSolveSolutionReal() {
        int numberOfVariables = 3;
        row = Arrays.asList(new Complex(1), new Complex(1), new Complex(2), new Complex(9));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(2), new Complex(4), new Complex(-3), new Complex(1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(3), new Complex(6), new Complex(-5), new Complex(0));
        matrix.add(new MatrixRow(row));
        System.out.printf("Starting matrix:%n%s%n%n", matrix); // TODO debug

        EquationSolver solver = new EquationSolver();
        Executor.SolutionState actual = solver.solve(matrix, numberOfVariables);
        Executor.SolutionState expected = Executor.SolutionState.SOLUTION;
        assert(expected == actual);
    }

    /*
    3 4 ? s/b 4 3? numberOfVariables is 3
0 1 2 9
0 1 3 1
1 0 6 0
2 0 2 0
     */
    private void testSolveNoSolutionReal() {
        int numberOfVariables = 3;
        row = Arrays.asList(new Complex(0), new Complex(1), new Complex(2), new Complex(9));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(0), new Complex(1), new Complex(3), new Complex(1));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(1), new Complex(0), new Complex(6), new Complex(0));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(2), new Complex(0), new Complex(2), new Complex(0));
        matrix.add(new MatrixRow(row));
        System.out.printf("Starting matrix:%n%s%n%n", matrix); // TODO debug

        EquationSolver solver = new EquationSolver();
        Executor.SolutionState actual = solver.solve(matrix, numberOfVariables);
        Executor.SolutionState expected = Executor.SolutionState.NO_SOLUTION;
        assert(expected == actual);
    }
    /*
    3 3
1+2i -1.5-1.1i 2.12 91+5i
-1+3i 1.2+3.5i -3.3 1+15i
12.31 1.3-5i 12.3i -78.3i
     */

    private void testSolveSolutionComplex() {
        int numberOfVariables = 3;
        row = Arrays.asList(new Complex(1, 2), new Complex(-1.5, -1.1),
                new Complex(2.12), new Complex(91, 5));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(-1, 3), new Complex(1.2, 3.5),
                new Complex(-3.3), new Complex(1, 15));
        matrix.add(new MatrixRow(row));
        row = Arrays.asList(new Complex(12.31), new Complex(1.3, -5),
                new Complex(0, 12.31), new Complex(0, -78.3));
        matrix.add(new MatrixRow(row));
        System.out.printf("Starting matrix:%n%s%n%n", matrix); // TODO debug

        EquationSolver solver = new EquationSolver();
        Executor.SolutionState actual = solver.solve(matrix, numberOfVariables);
        Executor.SolutionState expected = Executor.SolutionState.SOLUTION;
        assert(expected == actual);
    }
}
