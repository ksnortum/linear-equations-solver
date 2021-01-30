package solver.main.model;

import java.util.Objects;

/**
 * Holds info for "to" and "from" columns and rows.
 */
public class Swap {

    /** @return an empty Swap instance, to signal that no swap can be done */
    public static Swap empty() {
        return new Swap();
    }

    private final int rowFrom;
    private final int colFrom;
    private final int rowTo;
    private final int colTo;

    /** No swap constructor */
    public Swap() {
        rowFrom = 0;
        colFrom = 0;
        rowTo = 0;
        colTo = 0;
    }

    public Swap(int rowFrom, int colFrom, int rowTo, int colTo) {
        this.rowFrom = rowFrom;
        this.colFrom = colFrom;
        this.rowTo = rowTo;
        this.colTo = colTo;
    }

    public int getRowFrom() {
        return rowFrom;
    }

    public int getColFrom() {
        return colFrom;
    }

    public int getRowTo() {
        return rowTo;
    }

    public int getColTo() {
        return colTo;
    }

    /** @return {@code true} if swap is all zeros, otherwise {@code false} */
    public boolean isEmpty() {
        return rowFrom == 0 && colFrom == 0 && rowTo == 0 && colTo == 0;
    }

    @Override
    public String toString() {
        return "Swap{" +
                "rowFrom=" + rowFrom +
                ", colFrom=" + colFrom +
                ", rowTo=" + rowTo +
                ", colTo=" + colTo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Swap swap = (Swap) o;
        return getRowFrom() == swap.getRowFrom()
                && getColFrom() == swap.getColFrom()
                && getRowTo() == swap.getRowTo()
                && getColTo() == swap.getColTo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRowFrom(), getColFrom(), getRowTo(), getColTo());
    }
}
