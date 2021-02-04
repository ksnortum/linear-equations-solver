package solver.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    /**
     * Creates a multiplier that when multiplied by the source and added
     * to the target number, will zero the target.
     * @param source the source number
     * @param target the target number
     * @return a multiplier
     */
    public static Complex createMultiplier(Complex source, Complex target) {
        return target.negate().divide(source);
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

    public Complex getCoefficient(int row, int column) {
        if (row < 0 || row >= getSize()) {
            Exception e = new RuntimeException(
                    String.format("Matrix::getCoefficient(): Row is out of range (%d)", row));
            e.printStackTrace();

            return Complex.NaN;
        }

        if (column < 0 || column >= getLineLength()) {
            Exception e = new RuntimeException(
                    String.format("Matrix::getCoefficient(): Column is out of range (%d)", column));
            e.printStackTrace();

            return Complex.NaN;
        }

        return matrix.get(row).getColumn(column);
    }

    /**
     * Multiplies source row, then adds it to target row.
     * Source row is not affected in the matrix, only the target row.
     * @param sourceIndex index to the source row in the Matrix
     * @param targetIndex index to the target row in the Matrix
     * @param multiplier number to multiply the source row by
     */
    public void zeroTarget(int sourceIndex, int targetIndex, Complex multiplier) {
        MatrixRow multipliedSourceRow = matrix.get(sourceIndex).multiply(multiplier);
        matrix.set(targetIndex, matrix.get(targetIndex).addFrom(multipliedSourceRow));
    }

    /**
     * Multiplies all columns in the row indexed by the multiplier.
     * This row is affected in the matrix.
     * @param index the index of the row to be multiplied
     * @param multiplier the number to multiply by
     */
    public void multiplyRow(int index, Complex multiplier) {
        matrix.set(index, matrix.get(index).multiply(multiplier));
    }

    /**
     * Swap columns and rows.  To swap entire rows, make the "to" and "from"
     * columns the same.  To swap only columns, make the "to" and "from"
     * row the same. When swapping columns from different rows, be sure
     * to set the "from" row correctly.
     * @param swap the {@link Swap} object
     */
    public void swap(Swap swap) {
        if (swap.isEmpty()) {
            return;
        }

        int rowFrom = swap.getRowFrom();
        int colFrom = swap.getColFrom();
        int rowTo = swap.getRowTo();
        int colTo = swap.getColTo();

        // Column and row swap
        if (colFrom != colTo && rowFrom != rowTo) {
            Complex temp = matrix.get(rowTo).getColumn(colTo);
            matrix.get(rowTo).setColumn(colTo, matrix.get(rowFrom).getColumn(colFrom));
            matrix.get(rowFrom).setColumn(colFrom, temp);
        } else

            // Column only swap
            if (colFrom != colTo) {
                matrix.get(rowFrom).swapColumn(swap);
        } else

            // Row only swap
            if (rowFrom != rowTo) {
                MatrixRow temp = matrix.get(rowTo);
                matrix.set(rowTo, matrix.get(rowFrom));
                matrix.set(rowFrom, temp);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix other = (Matrix) o;
        return getLineLength() == other.getLineLength() && getMatrix().equals(other.getMatrix());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMatrix(), getLineLength());
    }

    @Override
    public String toString() {
        return getMatrix()
                .stream()
                .map(MatrixRow::toString)
                .collect(Collectors.joining("\n"));
    }
}
