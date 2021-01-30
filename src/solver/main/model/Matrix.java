package solver.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Matrix {
    private final List<MatrixRow> matrix;
    private int lineLength = -1;

    public Matrix() {
        matrix = new ArrayList<>();
    }

    public Matrix(List<MatrixRow> matrix) {
        this.matrix = matrix;
    }

    public void add(MatrixRow row) {
        if (lineLength == -1) {
            lineLength = row.getSize();
        } else if (lineLength != row.getSize()) {
            System.err.println("Cannot add row, line length is wrong");
            return;
        }

        matrix.add(row);
    }

    public int getSize() {
        return matrix.size();
    }

    /** -1 when matrix is empty */
    public int getLineLength() {
        return lineLength;
    }

    public List<MatrixRow> getMatrix() {
        return matrix;
    }

    public MatrixRow getMatrixRow(int index) {
        if (index < 0 || index >= matrix.size()) {
            System.err.println("Matrix::getMatrixRow: Row index out of range");

            return null;
        }

        return matrix.get(index);
    }

    public double getCoefficient(int row, int column) {
        if (row < 0 || row >= getSize()) {
            Exception e = new RuntimeException(
                    String.format("Matrix::getCoefficient(): Row is out of range (%d)", row));
            e.printStackTrace();

            return Double.NaN;
        }

        if (column < 0 || column >= getLineLength()) {
            Exception e = new RuntimeException(
                    String.format("Matrix::getCoefficient(): Column is out of range (%d)", column));
            e.printStackTrace();

            return Double.NaN;
        }

        return matrix.get(row).getColumn(column);
    }

    /**
     * Multiplies source row, then add it to target row.
     * Source row should not be affected in the matrix, only the target row.
     */
    public void zeroTarget(int sourceIndex, int targetIndex, double multiplier) {
        MatrixRow newSourceRow = matrix.get(sourceIndex).multiply(multiplier);
        matrix.get(targetIndex).addFrom(newSourceRow);
    }

    /**
     * Multiplies all columns in the row indexed by the multiplier.
     * This row affects in the matrix.
     */
    public void multiplyRow(int index, double multiplier) {
        matrix.set(index, matrix.get(index).multiply(multiplier));
    }

    /**
     * Swap columns and rows.  To swap only rows, make the "to" and "from"
     * columns the same.  To swap only columns, make the "to" and "from"
     * row the same. When swapping columns, be sure to set the "from"
     * row correctly.
     * @param swap the {@link Swap} object
     */
    public void swap(Swap swap) {
        if (swap.getColFrom() != swap.getColTo()) {
            matrix.get(swap.getRowFrom()).swapColumn(swap);
        }

        if (swap.getRowFrom() != swap.getRowTo()) {
            MatrixRow temp = matrix.get(swap.getRowTo());
            matrix.set(swap.getRowTo(), matrix.get(swap.getRowFrom()));
            matrix.set(swap.getRowFrom(), temp);
        }
    }

    @Override
    public String toString() {
        return getMatrix()
                .stream()
                .map(MatrixRow::toString)
                .collect(Collectors.joining("\n"));
    }
}
