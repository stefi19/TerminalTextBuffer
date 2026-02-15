package org.example;

import java.util.Objects;

/**
 * Represents the cursor position in a terminal buffer.
 * The cursor position is where the next character will be written.
 * Row and column are 0-based.
 */
public class Cursor {
    private int row;
    private int column;

    public Cursor(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Cursor() {
        this(0, 0);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Moves the cursor up by the specified number of rows.
     * Does not validate bounds - caller must ensure the position is valid.
     */
    public void moveUp(int count) {
        this.row -= count;
    }

    /**
     * Moves the cursor down by the specified number of rows.
     * Does not validate bounds - caller must ensure the position is valid.
     */
    public void moveDown(int count) {
        this.row += count;
    }

    /**
     * Moves the cursor left by the specified number of columns.
     * Does not validate bounds - caller must ensure the position is valid.
     */
    public void moveLeft(int count) {
        this.column -= count;
    }

    /**
     * Moves the cursor right by the specified number of columns.
     * Does not validate bounds - caller must ensure the position is valid.
     */
    public void moveRight(int count) {
        this.column += count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cursor cursor = (Cursor) o;
        return row == cursor.row && column == cursor.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "Cursor{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
