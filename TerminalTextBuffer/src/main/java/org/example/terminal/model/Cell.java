package org.example.terminal.model;

import java.util.Objects;

/**
 * Represents a single cell in the terminal buffer.
 * Contains a character and its visual attributes.
 * Supports wide characters (CJK, emoji) that occupy 2 cells.
 */
public class Cell {
    
    private final char character;
    private final CellAttributes attributes;
    private final boolean isWide;
    private final boolean isWideContinuation;

    /**
     * An empty cell with default attributes.
     */
    public static final Cell EMPTY = new Cell(' ', CellAttributes.DEFAULT, false, false);

    public Cell(char character, CellAttributes attributes) {
        this(character, attributes, isWideCharacter(character), false);
    }
    
    public Cell(char character, CellAttributes attributes, boolean isWide, boolean isWideContinuation) {
        this.character = character;
        this.attributes = attributes != null ? attributes : CellAttributes.DEFAULT;
        this.isWide = isWide;
        this.isWideContinuation = isWideContinuation;
    }

    public char getCharacter() {
        return character;
    }

    public CellAttributes getAttributes() {
        return attributes;
    }
    
    public boolean isWide() {
        return isWide;
    }
    
    public boolean isWideContinuation() {
        return isWideContinuation;
    }

    public boolean isEmpty() {
        return (character == ' ' || character == '\0') && !isWideContinuation;
    }
    
    /**
     * Determines if a character is a wide character (occupies 2 cells).
     * Wide characters include CJK ideographs, full-width characters, and emoji.
     * 
     * @param ch the character to check
     * @return true if the character occupies 2 cells, false otherwise
     */
    public static boolean isWideCharacter(char ch) {
        // CJK Unified Ideographs
        if (ch >= 0x4E00 && ch <= 0x9FFF) return true;
        
        // Hiragana and Katakana
        if (ch >= 0x3040 && ch <= 0x30FF) return true;
        
        // Hangul
        if (ch >= 0xAC00 && ch <= 0xD7AF) return true;
        
        // Full-width forms
        if (ch >= 0xFF00 && ch <= 0xFFEF) return true;
        
        // Emoji and symbols (basic check)
        if (ch >= 0x1F300 && ch <= 0x1F9FF) return true;
        
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return character == cell.character && 
               isWide == cell.isWide && 
               isWideContinuation == cell.isWideContinuation &&
               Objects.equals(attributes, cell.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, attributes, isWide, isWideContinuation);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "character=" + character +
                ", attributes=" + attributes +
                ", isWide=" + isWide +
                ", isWideContinuation=" + isWideContinuation +
                '}';
    }
}
