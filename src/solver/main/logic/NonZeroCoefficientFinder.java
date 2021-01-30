package solver.main.logic;

import solver.main.model.Matrix;
import solver.main.model.Swap;

public class NonZeroCoefficientFinder {
    public Swap findNonZeroCoefficient(Matrix matrix, int rowTarget, int colTarget) {
        int startingRow = rowTarget;
        int startingCol = colTarget;
        int constantColumn = matrix.getLineLength() - 1;

        // check max rows and max columns, but not constant column (last)
        while (startingRow < matrix.getSize() && startingCol < constantColumn) {
            int row = startingRow;
            int col = startingCol;

            // Search down from this starting row/col
            while (row < matrix.getSize() && matrix.getCoefficient(row, col) == 0) {
                row++;
            }

            // Found non-zero coefficient while searching down the row
            if (row < matrix.getSize()) {
                return new Swap(row, col, rowTarget, colTarget);
            }

            // Search to the right
            row = startingRow;
            col++;

            while (col < constantColumn && matrix.getCoefficient(row, col) == 0) {
                col++;
            }

            // Found non-zero coefficient while searching to the right
            if (col < constantColumn) {
                return new Swap(row, col, rowTarget, colTarget);
            }

            // Neither searching down nor to the right found a non-zero coefficient
            startingRow++;
            startingCol++;
        }

        // Signal that you can't swap
        return Swap.empty();
    }
}
