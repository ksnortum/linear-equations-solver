package solver.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MatrixRow {
    private final List<Double> row;

    public MatrixRow() {
        row = new ArrayList<>();
    }

    public MatrixRow(List<Double> row) {
        this.row = row;
    }

    public MatrixRow(MatrixRow matrixRow) {
        row = new ArrayList<>(matrixRow.getRow());
    }

    public void add(double d) {
        row.add(d);
    }

    public List<Double> getRow() {
        return row;
    }

    public int getSize() {
        return row.size();
    }

    public double getColumn(int index) {
        if (index < 0 || index >= row.size()) {
            System.err.println("MatrixRow::getColumn(): Index out of range");

            return Double.NaN;
        }

        return row.get(index);
    }

    /**
     * @param multiplier the number to multiply each element in the row by
     * @return a new MatrixRow, with the multiplied elements
     */
    public MatrixRow multiply(double multiplier) {
        List<Double> affected = new ArrayList<>(row);

        for (int i = 0; i < row.size(); i++) {
            affected.set(i, multiplier * row.get(i));
        }

        return new MatrixRow(affected);
    }

    /**
     * Add each element from the affecting row to this row.
     *
     * @param affectingRow the MatrixRow to get the addends from
     * @return this row with its elements added to
     */
    public MatrixRow addFrom(MatrixRow affectingRow) {
        if (row.size() != affectingRow.getSize()) {
            System.err.println("MatrixRow::addTo(): Rows are different sizes");

            return null;
        }

        for (int i = 0; i < row.size(); i++) {
            row.set(i, affectingRow.getColumn(i) + row.get(i));
        }

        return this;
    }

    public MatrixRow swapColumn(Swap swap) {
        if (swap.getColFrom() != swap.getColTo()) {
            double temp = row.get(swap.getColFrom());
            row.set(swap.getColFrom(), row.get(swap.getColTo()));
            row.set(swap.getColTo(), temp);
        }

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatrixRow matrixRow = (MatrixRow) o;
        return getRow().equals(matrixRow.getRow());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow());
    }

    @Override
    public String toString() {
        return getRow()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }
}
