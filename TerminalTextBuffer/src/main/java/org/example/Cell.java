package org.example;

import java.util.Objects;

/**
 * Represents a single cell in the terminal buffer.
 * Contains a character and its visual attributes.
 */
public class Cell {
    private final char character;
    private final CellAttributes attributes;

    /**
     * An empty cell with default attributes.
     */
    public static final Cell EMPTY = new Cell(' ', CellAttributes.DEFAULT);

    public Cell(char character, CellAttributes attributes) {
        this.character = character;
        this.attributes = attributes != null ? attributes : CellAttributes.DEFAULT;
    }

    public char getCharacter() {
        return character;
    }

    public CellAttributes getAttributes() {
        return attributes;
    }

    public boolean isEmpty() {
        return character == ' ' || character == '\0';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return character == cell.character && Objects.equals(attributes, cell.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, attributes);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "character=" + character +
                ", attributes=" + attributes +
                '}';
    }
}
