package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for buffer resize functionality.
 */
class ResizeTest {
    
    @Test
    void testResizeIncreaseWidth() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        // Write some content
        buffer.setCursorPosition(0, 0);
        buffer.writeText("Hello");
        
        // Resize to wider
        buffer.resize(15, 5);
        
        assertEquals(15, buffer.getWidth());
        assertEquals(5, buffer.getHeight());
        
        // Content should be preserved
        assertEquals("Hello", buffer.getLineAsString(0).substring(0, 5));
        
        // New cells should be empty
        Cell cell = buffer.getCellAt(0, 10);
        assertTrue(cell.isEmpty());
    }
    
    @Test
    void testResizeDecreaseWidth() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        // Write content
        buffer.setCursorPosition(0, 0);
        buffer.writeText("HelloWorld");
        
        // Resize to narrower
        buffer.resize(5, 5);
        
        assertEquals(5, buffer.getWidth());
        assertEquals(5, buffer.getHeight());
        
        // Content should be truncated
        assertEquals("Hello", buffer.getLineAsString(0).trim());
    }
    
    @Test
    void testResizeIncreaseHeight() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        // Fill all rows
        for (int i = 0; i < 5; i++) {
            buffer.setCursorPosition(i, 0);
            buffer.writeText("Row " + i);
        }
        
        // Resize to taller
        buffer.resize(10, 8);
        
        assertEquals(10, buffer.getWidth());
        assertEquals(8, buffer.getHeight());
        
        // Original content should be preserved
        for (int i = 0; i < 5; i++) {
            assertTrue(buffer.getLineAsString(i).startsWith("Row " + i));
        }
        
        // New rows should be empty
        assertTrue(buffer.getLineAsString(5).trim().isEmpty());
        assertTrue(buffer.getLineAsString(6).trim().isEmpty());
        assertTrue(buffer.getLineAsString(7).trim().isEmpty());
    }
    
    @Test
    void testResizeDecreaseHeight() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        // Fill all rows
        for (int i = 0; i < 5; i++) {
            buffer.setCursorPosition(i, 0);
            buffer.writeText("Row " + i);
        }
        
        // Resize to shorter
        buffer.resize(10, 3);
        
        assertEquals(10, buffer.getWidth());
        assertEquals(3, buffer.getHeight());
        
        // Bottom lines should be moved to scrollback
        assertEquals(2, buffer.getScrollbackSize());
        
        // Remaining screen content should be the top lines
        assertTrue(buffer.getLineAsString(0).startsWith("Row 0"));
        assertTrue(buffer.getLineAsString(1).startsWith("Row 1"));
        assertTrue(buffer.getLineAsString(2).startsWith("Row 2"));
    }
    
    @Test
    void testResizeCursorAdjustment() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        // Put cursor at bottom-right
        buffer.setCursorPosition(4, 9);
        
        // Resize to smaller
        buffer.resize(5, 3);
        
        // Cursor should be adjusted to remain within bounds
        assertEquals(2, buffer.getCursor().getRow());
        assertEquals(4, buffer.getCursor().getColumn());
    }
    
    @Test
    void testResizeSameDimensions() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        buffer.setCursorPosition(0, 0);
        buffer.writeText("Hello");
        
        // Resize to same dimensions
        buffer.resize(10, 5);
        
        // Nothing should change
        assertEquals(10, buffer.getWidth());
        assertEquals(5, buffer.getHeight());
        assertEquals("Hello", buffer.getLineAsString(0).substring(0, 5));
    }
    
    @Test
    void testResizeInvalidDimensions() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        assertThrows(IllegalArgumentException.class, () -> buffer.resize(0, 5));
        assertThrows(IllegalArgumentException.class, () -> buffer.resize(10, 0));
        assertThrows(IllegalArgumentException.class, () -> buffer.resize(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> buffer.resize(10, -1));
    }
    
    @Test
    void testResizePreservesScrollback() {
        TerminalBuffer buffer = new TerminalBuffer(10, 3, 100);
        
        // Create some scrollback
        buffer.insertEmptyLineAtBottom();
        buffer.setCursorPosition(2, 0);
        buffer.writeText("Line 1");
        
        buffer.insertEmptyLineAtBottom();
        buffer.setCursorPosition(2, 0);
        buffer.writeText("Line 2");
        
        buffer.insertEmptyLineAtBottom();
        buffer.setCursorPosition(2, 0);
        buffer.writeText("Line 3");
        
        int originalScrollbackSize = buffer.getScrollbackSize();
        
        // Resize width only
        buffer.resize(15, 3);
        
        // Scrollback size should be unchanged
        assertEquals(originalScrollbackSize, buffer.getScrollbackSize());
    }
    
    @Test
    void testResizeMultipleTimes() {
        TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
        
        buffer.setCursorPosition(0, 0);
        buffer.writeText("Test");
        
        // Multiple resizes
        buffer.resize(20, 10);
        buffer.resize(15, 8);
        buffer.resize(12, 6);
        
        assertEquals(12, buffer.getWidth());
        assertEquals(6, buffer.getHeight());
        
        // Content should still be preserved
        assertTrue(buffer.getLineAsString(0).startsWith("Test"));
    }
}
