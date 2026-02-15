package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * TerminalBuffer is the core data structure that terminal emulators use to store and manipulate displayed text.
 * 
 * The buffer consists of:
 * - Screen: the last N lines that fit the screen dimensions (e.g., 80×24). This is the editable part.
 * - Scrollback: lines that scrolled off the top of the screen, preserved for history and unmodifiable.
 * - Cursor: tracks where the next character will be written.
 * 
 * The buffer maintains current attributes (foreground, background, style) that are applied to newly written text.
 */
public class TerminalBuffer {
    private final int width;
    private final int height;
    private final int maxScrollbackLines;
    
    private final List<List<Cell>> screen;
    private final List<List<Cell>> scrollback;
    private final Cursor cursor;
    private CellAttributes currentAttributes;

    /**
     * Creates a new terminal buffer with the specified dimensions and scrollback size.
     * 
     * @param width the width of the screen in columns
     * @param height the height of the screen in rows
     * @param maxScrollbackLines maximum number of lines to keep in scrollback history
     */
    public TerminalBuffer(int width, int height, int maxScrollbackLines) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        if (maxScrollbackLines < 0) {
            throw new IllegalArgumentException("Max scrollback lines must be non-negative");
        }
        
        this.width = width;
        this.height = height;
        this.maxScrollbackLines = maxScrollbackLines;
        this.screen = new ArrayList<>(height);
        this.scrollback = new ArrayList<>();
        this.cursor = new Cursor();
        this.currentAttributes = CellAttributes.DEFAULT;
        
        // Initialize screen with empty lines
        for (int i = 0; i < height; i++) {
            screen.add(createEmptyLine());
        }
    }

    /**
     * Creates an empty line filled with empty cells.
     */
    private List<Cell> createEmptyLine() {
        List<Cell> line = new ArrayList<>(width);
        for (int i = 0; i < width; i++) {
            line.add(Cell.EMPTY);
        }
        return line;
    }

    // Getters for configuration
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxScrollbackLines() {
        return maxScrollbackLines;
    }

    public int getScrollbackSize() {
        return scrollback.size();
    }

    // Attribute management

    /**
     * Gets the current cell attributes that will be applied to newly written text.
     */
    public CellAttributes getCurrentAttributes() {
        return currentAttributes;
    }

    /**
     * Sets the current cell attributes that will be applied to newly written text.
     */
    public void setCurrentAttributes(CellAttributes attributes) {
        this.currentAttributes = attributes != null ? attributes : CellAttributes.DEFAULT;
    }

    // Cursor management

    /**
     * Gets the current cursor position (0-based row and column).
     */
    public Cursor getCursor() {
        return cursor;
    }

    /**
     * Sets the cursor position. The position must be within screen bounds.
     * 
     * @throws IllegalArgumentException if the position is out of bounds
     */
    public void setCursorPosition(int row, int column) {
        if (row < 0 || row >= height || column < 0 || column >= width) {
            throw new IllegalArgumentException(
                String.format("Cursor position (%d, %d) is out of bounds (0-based, screen: %d×%d)", 
                    row, column, width, height)
            );
        }
        cursor.setPosition(row, column);
    }

    /**
     * Moves the cursor up by the specified number of rows.
     * The cursor will not move outside screen bounds.
     */
    public void moveCursorUp(int count) {
        int newRow = Math.max(0, cursor.getRow() - count);
        cursor.setRow(newRow);
    }

    /**
     * Moves the cursor down by the specified number of rows.
     * The cursor will not move outside screen bounds.
     */
    public void moveCursorDown(int count) {
        int newRow = Math.min(height - 1, cursor.getRow() + count);
        cursor.setRow(newRow);
    }

    /**
     * Moves the cursor left by the specified number of columns.
     * The cursor will not move outside screen bounds.
     */
    public void moveCursorLeft(int count) {
        int newColumn = Math.max(0, cursor.getColumn() - count);
        cursor.setColumn(newColumn);
    }

    /**
     * Moves the cursor right by the specified number of columns.
     * The cursor will not move outside screen bounds.
     */
    public void moveCursorRight(int count) {
        int newColumn = Math.min(width - 1, cursor.getColumn() + count);
        cursor.setColumn(newColumn);
    }

    // Editing operations (to be implemented in next steps)
    
    // Content access (to be implemented in next steps)
}
