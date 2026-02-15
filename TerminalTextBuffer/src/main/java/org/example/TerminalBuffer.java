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

    // Editing operations

    /**
     * Writes text at the current cursor position, overwriting existing content.
     * The text is written with the current attributes.
     * The cursor moves to the position after the last written character.
     * If the text extends beyond the line width, it is truncated.
     */
    public void writeText(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        
        int row = cursor.getRow();
        int col = cursor.getColumn();
        List<Cell> line = screen.get(row);
        
        for (int i = 0; i < text.length() && col + i < width; i++) {
            char ch = text.charAt(i);
            line.set(col + i, new Cell(ch, currentAttributes));
        }
        
        // Move cursor to the position after the last written character
        cursor.setColumn(Math.min(width - 1, col + text.length()));
    }

    /**
     * Inserts text at the current cursor position, shifting existing content to the right.
     * Content that is pushed beyond the line width wraps to the next line.
     * The text is written with the current attributes.
     * The cursor moves to the position after the last written character.
     */
    public void insertText(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        
        int row = cursor.getRow();
        int col = cursor.getColumn();
        List<Cell> line = screen.get(row);
        
        // Insert characters one by one
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            
            if (col + i < width) {
                // Insert character at current position
                line.add(col + i, new Cell(ch, currentAttributes));
                
                // If line exceeds width, handle wrapping
                if (line.size() > width) {
                    Cell wrappedCell = line.remove(width);
                    
                    // If there's a next line, insert the wrapped cell at the beginning
                    if (row + 1 < height) {
                        List<Cell> nextLine = screen.get(row + 1);
                        nextLine.add(0, wrappedCell);
                        if (nextLine.size() > width) {
                            nextLine.remove(width);
                        }
                    }
                }
            }
        }
        
        // Move cursor to the position after the last written character
        cursor.setColumn(Math.min(width - 1, col + text.length()));
    }

    /**
     * Fills the specified line with the given character using current attributes.
     * 
     * @param row the row to fill (0-based)
     * @param ch the character to fill with (or ' ' for empty)
     * @throws IllegalArgumentException if row is out of bounds
     */
    public void fillLine(int row, char ch) {
        if (row < 0 || row >= height) {
            throw new IllegalArgumentException("Row " + row + " is out of bounds");
        }
        
        List<Cell> line = screen.get(row);
        line.clear();
        Cell fillCell = new Cell(ch, currentAttributes);
        for (int i = 0; i < width; i++) {
            line.add(fillCell);
        }
    }

    /**
     * Inserts an empty line at the bottom of the screen.
     * The top line is moved to scrollback if scrollback is enabled.
     */
    public void insertEmptyLineAtBottom() {
        // Move top line to scrollback
        List<Cell> topLine = screen.remove(0);
        scrollback.add(topLine);
        
        // Trim scrollback if it exceeds the maximum
        if (scrollback.size() > maxScrollbackLines) {
            scrollback.remove(0);
        }
        
        // Add empty line at bottom
        screen.add(createEmptyLine());
        
        // Adjust cursor if it was on the top line
        if (cursor.getRow() > 0) {
            cursor.setRow(cursor.getRow() - 1);
        }
    }

    /**
     * Clears the entire screen, filling it with empty cells.
     * Scrollback is preserved, and cursor is reset to (0, 0).
     */
    public void clearScreen() {
        screen.clear();
        for (int i = 0; i < height; i++) {
            screen.add(createEmptyLine());
        }
        cursor.setPosition(0, 0);
    }

    /**
     * Clears the entire screen and scrollback buffer.
     * Cursor is reset to (0, 0).
     */
    public void clearAll() {
        clearScreen();
        scrollback.clear();
    }

    // Content access

    /**
     * Gets the character at the specified position.
     * Can access both screen and scrollback.
     * 
     * @param row the row (0-based, negative values access scrollback)
     * @param column the column (0-based)
     * @return the character at the position
     * @throws IllegalArgumentException if position is out of bounds
     */
    public char getCharAt(int row, int column) {
        if (column < 0 || column >= width) {
            throw new IllegalArgumentException("Column " + column + " is out of bounds");
        }
        
        List<Cell> line = getLineAt(row);
        return line.get(column).getCharacter();
    }

    /**
     * Gets the cell attributes at the specified position.
     * Can access both screen and scrollback.
     * 
     * @param row the row (0-based, negative values access scrollback)
     * @param column the column (0-based)
     * @return the cell attributes at the position
     * @throws IllegalArgumentException if position is out of bounds
     */
    public CellAttributes getAttributesAt(int row, int column) {
        if (column < 0 || column >= width) {
            throw new IllegalArgumentException("Column " + column + " is out of bounds");
        }
        
        List<Cell> line = getLineAt(row);
        return line.get(column).getAttributes();
    }

    /**
     * Gets a line as a string.
     * Can access both screen and scrollback.
     * 
     * @param row the row (0-based, negative values access scrollback from the end)
     * @return the line as a string
     * @throws IllegalArgumentException if row is out of bounds
     */
    public String getLineAsString(int row) {
        List<Cell> line = getLineAt(row);
        StringBuilder sb = new StringBuilder(width);
        for (Cell cell : line) {
            sb.append(cell.getCharacter());
        }
        return sb.toString();
    }

    /**
     * Helper method to get a line from screen or scrollback.
     * 
     * @param row the row (0-based for screen, negative for scrollback)
     * @return the line
     * @throws IllegalArgumentException if row is out of bounds
     */
    private List<Cell> getLineAt(int row) {
        if (row >= 0 && row < height) {
            // Access screen
            return screen.get(row);
        } else if (row < 0) {
            // Access scrollback (negative indices)
            int scrollbackIndex = scrollback.size() + row;
            if (scrollbackIndex >= 0 && scrollbackIndex < scrollback.size()) {
                return scrollback.get(scrollbackIndex);
            }
        }
        throw new IllegalArgumentException("Row " + row + " is out of bounds");
    }

    /**
     * Gets the entire screen content as a string.
     * Lines are separated by newline characters.
     * 
     * @return the screen content as a string
     */
    public String getScreenAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            if (i > 0) {
                sb.append('\n');
            }
            sb.append(getLineAsString(i));
        }
        return sb.toString();
    }

    /**
     * Gets the entire screen and scrollback content as a string.
     * Lines are separated by newline characters.
     * Scrollback lines appear first, followed by screen lines.
     * 
     * @return the complete buffer content as a string
     */
    public String getAllContentAsString() {
        StringBuilder sb = new StringBuilder();
        
        // Add scrollback content
        for (int i = 0; i < scrollback.size(); i++) {
            if (i > 0) {
                sb.append('\n');
            }
            sb.append(getLineAsString(-scrollback.size() + i));
        }
        
        // Add screen content
        if (!scrollback.isEmpty() && height > 0) {
            sb.append('\n');
        }
        
        for (int i = 0; i < height; i++) {
            if (i > 0) {
                sb.append('\n');
            }
            sb.append(getLineAsString(i));
        }
        
        return sb.toString();
    }
}
