package solver.main.logic;

import solver.main.control.Executor;
import solver.main.model.Matrix;

public class SolutionChecker {
    public Executor.SolutionState checkForSolution(Matrix matrix, int numberOfVariables) {
        Executor.SolutionState state = checkForNonZeroConstants(matrix);

        if (state != Executor.SolutionState.SOLUTION) {
            return state;
        }

        return checkSignificantEquationsAndVariables(matrix, numberOfVariables);
    }

    private Executor.SolutionState checkForNonZeroConstants(Matrix matrix) {
        final int constantIndex = matrix.getLineLength() - 1;
        boolean thereIsASolution = true;

        // loop through rows from bottom up
        for (int row = matrix.getSize() - 1; row >= 0; row--) {
            boolean allZeroColumns = true;

            // loop through columns to see if they're all zero, except constant
            for (int col = 0; col < constantIndex; col++) {
                if (matrix.getCoefficient(row, col) != 0.0) {
                    allZeroColumns = false;
                    break;
                }
            }

            if (allZeroColumns && matrix.getCoefficient(row, constantIndex) != 0.0) {
                thereIsASolution = false;
                break;
            }
        }

        if (thereIsASolution) {
            return Executor.SolutionState.SOLUTION;
        }

        return Executor.SolutionState.NO_SOLUTION;
    }

    private Executor.SolutionState checkSignificantEquationsAndVariables(Matrix matrix, int numberOfVariables) {
        int significantEquations = 0;

        for (int row = 0; row < matrix.getSize(); row++) {
            boolean equationIsSignificant = false;

            for (int col = 0; col < matrix.getLineLength(); col++) {
                if (matrix.getCoefficient(row, col) != 0.0) {
                    equationIsSignificant = true;
                    break;
                }
            }

            if (equationIsSignificant) {
                significantEquations++;
            }
        }

        if (significantEquations == numberOfVariables) {
            return Executor.SolutionState.SOLUTION;
        }

        // Equations < variables.
        // The situation where the number of significant equations is greater than
        // the number of variables is handled in the method checkForNonZeroConstants()
        return Executor.SolutionState.INFINITE_SOLUTIONS;
    }
}
