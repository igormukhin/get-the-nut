package de.igormukhin.getthenut;

import java.io.Serializable;

public class Pos implements Serializable {

    private final int row;
    private final int col;

    private Pos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Pos of(int row, int col) {
        return new Pos(row, col);
    }

    public int row() {
        return this.row;
    }

    public int col() {
        return this.col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pos pos = (Pos) o;

        if (row != pos.row) return false;
        return col == pos.col;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public String toString() {
        return "Pos{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
