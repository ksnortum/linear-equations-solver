package solver.main.logic;

import solver.main.control.Executor;
import solver.main.model.Complex;
import solver.main.model.Matrix;
import solver.main.model.Swap;

import java.util.Stack;

public class EquationSolver {
    private final Stack<Swap> swaps = new Stack<>();
    private final NonZeroCoefficientFinder finder = new NonZeroCoefficientFinder();
    private final SolutionChecker checker = new SolutionChecker();

    public Executor.SolutionState solve(Matrix matrix, int numberOfVariables) {
        zeroCoefficientsBelow(matrix);
        Executor.SolutionState state = checker.checkForSolution(matrix, numberOfVariables);

        if (state != Executor.SolutionState.SOLUTION) {
            return state;
        }

        createUnitDiagonal(matrix);
        zeroCoefficientsAbove(matrix);
        undoSwaps(matrix);

        return Executor.SolutionState.SOLUTION;
    }

    private void zeroCoefficientsBelow(Matrix matrix) {

        // loop through all except the last, as you need to compare the row below
        // TODO, we are doing too much in this for-loop
        for (int sourceRow = 0, column = 0;
                 sourceRow < matrix.getSize() - 1 && column < matrix.getLineLength() - 1;
                 sourceRow++, column++) {

            Complex sourceCoefficient = matrix.getCoefficient(sourceRow, column);

            // Avoid dividing by zero
            if (sourceCoefficient.isZero()) {

                // search for non-zero coefficient
                Swap swap = finder.findNonZeroCoefficient(matrix, sourceRow, column);

                // if no non-zero coefficient found, exit for-loop
                if (swap.isEmpty()) {
                    break;
                }

                printSwapDebugging(swap);
                matrix.swap(swap);
                swaps.push(swap);

                // SourceCoefficient has changed after the swap, so get it again
                sourceCoefficient = matrix.getCoefficient(sourceRow, column);

                // We swapped to get a non-zero coefficient, so something's wrong if it's zero still
                if (sourceCoefficient.isZero()) {
                    System.err.println("Coefficient is zero");
                    break;
                }
            }

            // Loop through all equations below the current one, zeroing them
            for (int targetRow = sourceRow + 1; targetRow < matrix.getSize(); targetRow++) {
                Complex targetCoefficient = matrix.getCoefficient(targetRow, column);

                if (!targetCoefficient.isZero()) {
                    Complex multiplier = targetCoefficient.negate().divide(sourceCoefficient);
                    System.out.printf("%s * R%d + R%d -> R%d%n",
                            multiplier, sourceRow + 1, targetRow + 1, targetRow + 1);
                    matrix.zeroTarget(sourceRow, targetRow, multiplier);
                }
            }
        }
    }

    private void printSwapDebugging(Swap swap) {
        if (swap.getRowFrom() != swap.getRowTo()) {
            System.out.printf("R%d <-> R%d%n", swap.getRowFrom() + 1, swap.getRowTo() + 1);

        }

        if (swap.getColFrom() != swap.getColTo()) {
            System.out.printf("C%d <-> C%d%n", swap.getColFrom() + 1, swap.getColTo() + 1);
        }
    }

    private void zeroCoefficientsAbove(Matrix matrix) {
        int column = 1;

        // Loop through all rows except the first, as you need to examine the row above
        for (int sourceRow = 1; sourceRow < matrix.getSize(); sourceRow++) {
            Complex sourceCoefficient = matrix.getCoefficient(sourceRow, column);

            // Avoid dividing by zero
            if (!sourceCoefficient.isZero()) {

                // Loop through all equations above the current one
                for (int targetRow = sourceRow - 1; targetRow >= 0; targetRow--) {
                    Complex targetCoefficient = matrix.getCoefficient(targetRow, column);

                    if (!targetCoefficient.isZero()) {
                        Complex multiplier = targetCoefficient.negate().divide(sourceCoefficient);
                        System.out.printf("%s * R%d + R%d -> R%d%n",
                                multiplier, sourceRow + 1, targetRow + 1, targetRow + 1);
                        matrix.zeroTarget(sourceRow, targetRow, multiplier);
                    }
                }
            }

            column++;
        }
    }

    private void createUnitDiagonal(Matrix matrix) {
        int column = 0;

        for (int row = 0; row < matrix.getSize(); row++) {
            Complex coefficient = matrix.getCoefficient(row, column);

            if (!coefficient.isZero()) {
                if (!coefficient.equals(Complex.ONE)) {
                    Complex multiplier = coefficient.inverse();
                    System.out.printf("%s * R%d -> R%d%n", multiplier, row + 1, row + 1);
                    matrix.multiplyRow(row, multiplier);
                }
            }

            column++;
        }
    }

    private void undoSwaps(Matrix matrix) {
        // TODO, no-op at the moment
    }
}
