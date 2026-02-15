package org.example;

import org.example.terminal.buffer.TerminalBuffer;
import org.example.terminal.model.Cell;
import org.example.terminal.model.CellAttributes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for wide character support (CJK, emoji, etc.).
 */
class WideCharacterTest {
    
    @Test
    void testIsWideCharacter() {
        // CJK Unified Ideographs
        assertTrue(Cell.isWideCharacter('\u4E2D')); // 中
        assertTrue(Cell.isWideCharacter('\u6587')); // 文
        
        // Hiragana and Katakana
        assertTrue(Cell.isWideCharacter('\u3042')); // あ
        assertTrue(Cell.isWideCharacter('\u30AB')); // カ
        
        // Hangul
        assertTrue(Cell.isWideCharacter('\uD55C')); // 한
        assertTrue(Cell.isWideCharacter('\uAE00')); // 글
        
        // Full-width forms
        assertTrue(Cell.isWideCharacter('\uFF21')); // Ａ (full-width)
        
        // Regular ASCII should not be wide
        assertFalse(Cell.isWideCharacter('A'));
        assertFalse(Cell.isWideCharacter('Z'));
        assertFalse(Cell.isWideCharacter('0'));
        assertFalse(Cell.isWideCharacter(' '));
    }
    
    @Test
    void testCellWideCharacterFlag() {
        Cell wideCell = new Cell('\u4E2D', CellAttributes.DEFAULT);
        assertTrue(wideCell.isWide());
        assertFalse(wideCell.isWideContinuation());
        
        Cell normalCell = new Cell('A', CellAttributes.DEFAULT);
        assertFalse(normalCell.isWide());
        assertFalse(normalCell.isWideContinuation());
        
        Cell continuation = new Cell(' ', CellAttributes.DEFAULT, false, true);
        assertFalse(continuation.isWide());
        assertTrue(continuation.isWideContinuation());
    }
    
    @Test
    void testWriteWideCharacters() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        // Write a wide character
        buffer.setCursorPosition(0, 0);
        buffer.writeText("\u4E2D"); // Chinese character
        
        // Should occupy 2 cells
        Cell cell0 = buffer.getCellAt(0, 0);
        assertEquals('\u4E2D', cell0.getCharacter());
        assertTrue(cell0.isWide());
        
        Cell cell1 = buffer.getCellAt(0, 1);
        assertTrue(cell1.isWideContinuation());
        
        // Cursor should advance by 2
        assertEquals(2, buffer.getCursor().getColumn());
    }
    
    @Test
    void testWideCharacterAtEndOfLine() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        // Try to write wide character at column 9 (last column)
        buffer.setCursorPosition(0, 9);
        buffer.writeText("\u4E2D");
        
        // Should not write the character (not enough space)
        Cell cell = buffer.getCellAt(0, 9);
        assertTrue(cell.isEmpty() || cell.getCharacter() == ' ');
    }
    
    @Test
    void testMixedWideAndNormalCharacters() {
        TerminalBuffer buffer = new TerminalBuffer(20, 5, 100);
        
        buffer.setCursorPosition(0, 0);
        buffer.writeText("ABC\u4E2D\u6587XYZ"); // ABC中文XYZ
        
        // Check positions
        assertEquals('A', buffer.getCellAt(0, 0).getCharacter());
        assertEquals('B', buffer.getCellAt(0, 1).getCharacter());
        assertEquals('C', buffer.getCellAt(0, 2).getCharacter());
        
        // Wide character at 3
        assertEquals('\u4E2D', buffer.getCellAt(0, 3).getCharacter());
        assertTrue(buffer.getCellAt(0, 3).isWide());
        assertTrue(buffer.getCellAt(0, 4).isWideContinuation());
        
        // Wide character at 5
        assertEquals('\u6587', buffer.getCellAt(0, 5).getCharacter());
        assertTrue(buffer.getCellAt(0, 5).isWide());
        assertTrue(buffer.getCellAt(0, 6).isWideContinuation());
        
        // Normal characters at 7, 8, 9
        assertEquals('X', buffer.getCellAt(0, 7).getCharacter());
        assertEquals('Y', buffer.getCellAt(0, 8).getCharacter());
        assertEquals('Z', buffer.getCellAt(0, 9).getCharacter());
    }
    
    @Test
    void testCursorMovementWithWideCharacters() {
        TerminalBuffer buffer = new TerminalBuffer(20, 5, 100);
        
        buffer.setCursorPosition(0, 0);
        buffer.writeText("A\u4E2DB"); // A中B
        
        buffer.setCursorPosition(0, 0);
        
        // Move right should skip over wide character properly
        buffer.moveCursorRight(1); // Should be at column 1 (the wide char)
        assertEquals(1, buffer.getCursor().getColumn());
        
        buffer.moveCursorRight(1); // Should skip to column 3 (after continuation)
        assertEquals(3, buffer.getCursor().getColumn());
        
        // Move left from 3 should go to 1 (skipping continuation)
        buffer.moveCursorLeft(1);
        assertEquals(1, buffer.getCursor().getColumn());
    }
    
    @Test
    void testCellEqualsWithWideFlags() {
        Cell cell1 = new Cell('\u4E2D', CellAttributes.DEFAULT, true, false);
        Cell cell2 = new Cell('\u4E2D', CellAttributes.DEFAULT, true, false);
        Cell cell3 = new Cell('\u4E2D', CellAttributes.DEFAULT, false, false);
        Cell cell4 = new Cell(' ', CellAttributes.DEFAULT, false, true);
        
        assertEquals(cell1, cell2);
        assertNotEquals(cell1, cell3); // Different isWide flag
        assertNotEquals(cell1, cell4); // Different character and flags
    }
}
