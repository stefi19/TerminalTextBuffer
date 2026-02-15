package org.example;

import org.example.terminal.buffer.Cursor;
import org.example.terminal.buffer.TerminalBuffer;
import org.example.terminal.model.Cell;
import org.example.terminal.model.CellAttributes;
import org.example.terminal.model.Color;
import org.example.terminal.model.StyleFlags;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for TerminalBuffer.
 * Tests all functionality including edge cases and boundary conditions.
 */
@DisplayName("TerminalBuffer Tests")
public class TerminalBufferTest {

    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {

        @Test
        @DisplayName("Should create buffer with specified dimensions")
        void testCreation() {
            TerminalBuffer buffer = new TerminalBuffer(80, 24, 1000);
            
            assertEquals(80, buffer.getWidth());
            assertEquals(24, buffer.getHeight());
            assertEquals(1000, buffer.getMaxScrollbackLines());
            assertEquals(0, buffer.getScrollbackSize());
        }

        @Test
        @DisplayName("Should initialize cursor at (0, 0)")
        void testInitialCursorPosition() {
            TerminalBuffer buffer = new TerminalBuffer(80, 24, 1000);
            
            assertEquals(0, buffer.getCursor().getRow());
            assertEquals(0, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should initialize with default attributes")
        void testInitialAttributes() {
            TerminalBuffer buffer = new TerminalBuffer(80, 24, 1000);
            
            assertEquals(CellAttributes.DEFAULT, buffer.getCurrentAttributes());
        }

        @Test
        @DisplayName("Should throw exception for invalid dimensions")
        void testInvalidDimensions() {
            assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(0, 24, 1000));
            assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(80, 0, 1000));
            assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(-1, 24, 1000));
            assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(80, -1, 1000));
        }

        @Test
        @DisplayName("Should throw exception for negative scrollback size")
        void testNegativeScrollback() {
            assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(80, 24, -1));
        }

        @Test
        @DisplayName("Should allow zero scrollback size")
        void testZeroScrollback() {
            TerminalBuffer buffer = new TerminalBuffer(80, 24, 0);
            assertEquals(0, buffer.getMaxScrollbackLines());
        }
    }

    @Nested
    @DisplayName("Cursor Management Tests")
    class CursorManagementTests {

        private TerminalBuffer buffer;

        @BeforeEach
        void setUp() {
            buffer = new TerminalBuffer(80, 24, 1000);
        }

        @Test
        @DisplayName("Should set cursor position within bounds")
        void testSetCursorPosition() {
            buffer.setCursorPosition(10, 20);
            
            assertEquals(10, buffer.getCursor().getRow());
            assertEquals(20, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should set cursor at top-left corner")
        void testSetCursorTopLeft() {
            buffer.setCursorPosition(5, 5);
            buffer.setCursorPosition(0, 0);
            
            assertEquals(0, buffer.getCursor().getRow());
            assertEquals(0, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should set cursor at bottom-right corner")
        void testSetCursorBottomRight() {
            buffer.setCursorPosition(23, 79);
            
            assertEquals(23, buffer.getCursor().getRow());
            assertEquals(79, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should throw exception when setting cursor out of bounds")
        void testSetCursorOutOfBounds() {
            assertThrows(IllegalArgumentException.class, () -> buffer.setCursorPosition(-1, 0));
            assertThrows(IllegalArgumentException.class, () -> buffer.setCursorPosition(0, -1));
            assertThrows(IllegalArgumentException.class, () -> buffer.setCursorPosition(24, 0));
            assertThrows(IllegalArgumentException.class, () -> buffer.setCursorPosition(0, 80));
            assertThrows(IllegalArgumentException.class, () -> buffer.setCursorPosition(100, 100));
        }

        @Test
        @DisplayName("Should move cursor up within bounds")
        void testMoveCursorUp() {
            buffer.setCursorPosition(10, 10);
            buffer.moveCursorUp(3);
            
            assertEquals(7, buffer.getCursor().getRow());
            assertEquals(10, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should not move cursor up beyond top boundary")
        void testMoveCursorUpBeyondBounds() {
            buffer.setCursorPosition(5, 10);
            buffer.moveCursorUp(10);
            
            assertEquals(0, buffer.getCursor().getRow());
        }

        @Test
        @DisplayName("Should move cursor down within bounds")
        void testMoveCursorDown() {
            buffer.setCursorPosition(10, 10);
            buffer.moveCursorDown(3);
            
            assertEquals(13, buffer.getCursor().getRow());
            assertEquals(10, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should not move cursor down beyond bottom boundary")
        void testMoveCursorDownBeyondBounds() {
            buffer.setCursorPosition(20, 10);
            buffer.moveCursorDown(10);
            
            assertEquals(23, buffer.getCursor().getRow());
        }

        @Test
        @DisplayName("Should move cursor left within bounds")
        void testMoveCursorLeft() {
            buffer.setCursorPosition(10, 10);
            buffer.moveCursorLeft(3);
            
            assertEquals(10, buffer.getCursor().getRow());
            assertEquals(7, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should not move cursor left beyond left boundary")
        void testMoveCursorLeftBeyondBounds() {
            buffer.setCursorPosition(10, 5);
            buffer.moveCursorLeft(10);
            
            assertEquals(0, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should move cursor right within bounds")
        void testMoveCursorRight() {
            buffer.setCursorPosition(10, 10);
            buffer.moveCursorRight(3);
            
            assertEquals(10, buffer.getCursor().getRow());
            assertEquals(13, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should not move cursor right beyond right boundary")
        void testMoveCursorRightBeyondBounds() {
            buffer.setCursorPosition(10, 75);
            buffer.moveCursorRight(10);
            
            assertEquals(79, buffer.getCursor().getColumn());
        }
    }

    @Nested
    @DisplayName("Attribute Management Tests")
    class AttributeManagementTests {

        private TerminalBuffer buffer;

        @BeforeEach
        void setUp() {
            buffer = new TerminalBuffer(80, 24, 1000);
        }

        @Test
        @DisplayName("Should set and get current attributes")
        void testSetAttributes() {
            CellAttributes attrs = new CellAttributes(
                Color.RED,
                Color.BLUE,
                new StyleFlags(true, false, true)
            );
            
            buffer.setCurrentAttributes(attrs);
            
            assertEquals(attrs, buffer.getCurrentAttributes());
        }

        @Test
        @DisplayName("Should use default attributes when null is provided")
        void testSetNullAttributes() {
            buffer.setCurrentAttributes(null);
            
            assertEquals(CellAttributes.DEFAULT, buffer.getCurrentAttributes());
        }
    }

    @Nested
    @DisplayName("Write Text Tests")
    class WriteTextTests {

        private TerminalBuffer buffer;

        @BeforeEach
        void setUp() {
            buffer = new TerminalBuffer(10, 5, 100);
        }

        @Test
        @DisplayName("Should write text at cursor position")
        void testWriteText() {
            buffer.writeText("Hello");
            
            assertEquals("Hello     ", buffer.getLineAsString(0));
            assertEquals(5, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should overwrite existing text")
        void testWriteTextOverwrite() {
            buffer.writeText("Hello");
            buffer.setCursorPosition(0, 0);
            buffer.writeText("Hi");
            
            assertEquals("Hillo     ", buffer.getLineAsString(0));
        }

        @Test
        @DisplayName("Should truncate text at line boundary")
        void testWriteTextTruncate() {
            buffer.setCursorPosition(0, 7);
            buffer.writeText("Hello");
            
            String line = buffer.getLineAsString(0);
            assertEquals("       Hel", line);
        }

        @Test
        @DisplayName("Should handle empty string")
        void testWriteEmptyString() {
            buffer.writeText("");
            
            assertEquals("          ", buffer.getLineAsString(0));
            assertEquals(0, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should handle null string")
        void testWriteNullString() {
            buffer.writeText(null);
            
            assertEquals("          ", buffer.getLineAsString(0));
            assertEquals(0, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should write text with custom attributes")
        void testWriteTextWithAttributes() {
            CellAttributes attrs = new CellAttributes(
                Color.RED,
                Color.BLUE,
                StyleFlags.DEFAULT
            );
            buffer.setCurrentAttributes(attrs);
            buffer.writeText("Test");
            
            assertEquals(attrs, buffer.getAttributesAt(0, 0));
            assertEquals(attrs, buffer.getAttributesAt(0, 1));
            assertEquals(attrs, buffer.getAttributesAt(0, 2));
            assertEquals(attrs, buffer.getAttributesAt(0, 3));
        }

        @Test
        @DisplayName("Should move cursor to last position when text fits exactly")
        void testWriteTextExactFit() {
            buffer.writeText("0123456789");
            
            assertEquals(9, buffer.getCursor().getColumn());
        }
    }

    @Nested
    @DisplayName("Insert Text Tests")
    class InsertTextTests {

        private TerminalBuffer buffer;

        @BeforeEach
        void setUp() {
            buffer = new TerminalBuffer(10, 5, 100);
        }

        @Test
        @DisplayName("Should insert text at cursor position")
        void testInsertText() {
            buffer.writeText("World");
            buffer.setCursorPosition(0, 0);
            buffer.insertText("Hello ");
            
            String line = buffer.getLineAsString(0);
            assertTrue(line.startsWith("Hello Worl") || line.startsWith("Hello W"));
        }

        @Test
        @DisplayName("Should handle empty string")
        void testInsertEmptyString() {
            buffer.writeText("Test");
            buffer.setCursorPosition(0, 0);
            buffer.insertText("");
            
            assertEquals("Test      ", buffer.getLineAsString(0));
        }

        @Test
        @DisplayName("Should handle null string")
        void testInsertNullString() {
            buffer.writeText("Test");
            buffer.setCursorPosition(0, 0);
            buffer.insertText(null);
            
            assertEquals("Test      ", buffer.getLineAsString(0));
        }
    }

    @Nested
    @DisplayName("Fill Line Tests")
    class FillLineTests {

        private TerminalBuffer buffer;

        @BeforeEach
        void setUp() {
            buffer = new TerminalBuffer(10, 5, 100);
        }

        @Test
        @DisplayName("Should fill line with character")
        void testFillLine() {
            buffer.fillLine(0, '-');
            
            assertEquals("----------", buffer.getLineAsString(0));
        }

        @Test
        @DisplayName("Should fill line with space (empty)")
        void testFillLineWithSpace() {
            buffer.writeText("Test");
            buffer.fillLine(0, ' ');
            
            assertEquals("          ", buffer.getLineAsString(0));
        }

        @Test
        @DisplayName("Should fill with current attributes")
        void testFillLineWithAttributes() {
            CellAttributes attrs = new CellAttributes(
                Color.GREEN,
                Color.BLACK,
                StyleFlags.DEFAULT
            );
            buffer.setCurrentAttributes(attrs);
            buffer.fillLine(0, '*');
            
            assertEquals(attrs, buffer.getAttributesAt(0, 0));
            assertEquals(attrs, buffer.getAttributesAt(0, 9));
        }

        @Test
        @DisplayName("Should throw exception for invalid row")
        void testFillLineInvalidRow() {
            assertThrows(IllegalArgumentException.class, () -> buffer.fillLine(-1, 'X'));
            assertThrows(IllegalArgumentException.class, () -> buffer.fillLine(5, 'X'));
        }
    }

    @Nested
    @DisplayName("Clear Operations Tests")
    class ClearOperationsTests {

        private TerminalBuffer buffer;

        @BeforeEach
        void setUp() {
            buffer = new TerminalBuffer(10, 5, 100);
        }

        @Test
        @DisplayName("Should clear screen content")
        void testClearScreen() {
            // Fill screen with content
            for (int i = 0; i < 5; i++) {
                buffer.setCursorPosition(i, 0);
                buffer.writeText("Line " + i);
            }
            
            buffer.clearScreen();
            
            for (int i = 0; i < 5; i++) {
                assertEquals("          ", buffer.getLineAsString(i));
            }
        }

        @Test
        @DisplayName("Should reset cursor to (0, 0) when clearing screen")
        void testClearScreenResetsCursor() {
            buffer.setCursorPosition(3, 5);
            buffer.clearScreen();
            
            assertEquals(0, buffer.getCursor().getRow());
            assertEquals(0, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should preserve scrollback when clearing screen")
        void testClearScreenPreservesScrollback() {
            // Add some lines to scrollback
            for (int i = 0; i < 3; i++) {
                buffer.insertEmptyLineAtBottom();
            }
            
            int scrollbackSize = buffer.getScrollbackSize();
            buffer.clearScreen();
            
            assertEquals(scrollbackSize, buffer.getScrollbackSize());
        }

        @Test
        @DisplayName("Should clear screen and scrollback")
        void testClearAll() {
            // Fill screen and add to scrollback
            for (int i = 0; i < 3; i++) {
                buffer.setCursorPosition(0, 0);
                buffer.writeText("Line " + i);
                buffer.insertEmptyLineAtBottom();
            }
            
            buffer.clearAll();
            
            assertEquals(0, buffer.getScrollbackSize());
            for (int i = 0; i < 5; i++) {
                assertEquals("          ", buffer.getLineAsString(i));
            }
            assertEquals(0, buffer.getCursor().getRow());
            assertEquals(0, buffer.getCursor().getColumn());
        }
    }

    @Nested
    @DisplayName("Scrollback Tests")
    class ScrollbackTests {

        private TerminalBuffer buffer;

        @BeforeEach
        void setUp() {
            buffer = new TerminalBuffer(10, 3, 5);
        }

        @Test
        @DisplayName("Should add line to scrollback when inserting at bottom")
        void testInsertEmptyLineAtBottom() {
            buffer.setCursorPosition(0, 0);
            buffer.writeText("Line 0");
            
            buffer.insertEmptyLineAtBottom();
            
            assertEquals(1, buffer.getScrollbackSize());
        }

        @Test
        @DisplayName("Should maintain scrollback limit")
        void testScrollbackLimit() {
            for (int i = 0; i < 10; i++) {
                buffer.setCursorPosition(0, 0);
                buffer.writeText("Line " + i);
                buffer.insertEmptyLineAtBottom();
            }
            
            assertEquals(5, buffer.getScrollbackSize());
        }

        @Test
        @DisplayName("Should access scrollback with negative indices")
        void testAccessScrollback() {
            buffer.setCursorPosition(0, 0);
            buffer.writeText("First");
            buffer.insertEmptyLineAtBottom();
            
            buffer.setCursorPosition(0, 0);
            buffer.writeText("Second");
            buffer.insertEmptyLineAtBottom();
            
            assertEquals("First     ", buffer.getLineAsString(-2));
            assertEquals("Second    ", buffer.getLineAsString(-1));
        }

        @Test
        @DisplayName("Should adjust cursor when inserting line at bottom")
        void testCursorAdjustmentOnInsert() {
            buffer.setCursorPosition(2, 5);
            buffer.insertEmptyLineAtBottom();
            
            assertEquals(1, buffer.getCursor().getRow());
            assertEquals(5, buffer.getCursor().getColumn());
        }

        @Test
        @DisplayName("Should keep cursor at row 0 when already at top")
        void testCursorAtTopOnInsert() {
            buffer.setCursorPosition(0, 5);
            buffer.insertEmptyLineAtBottom();
            
            assertEquals(0, buffer.getCursor().getRow());
            assertEquals(5, buffer.getCursor().getColumn());
        }
    }

    @Nested
    @DisplayName("Content Access Tests")
    class ContentAccessTests {

        private TerminalBuffer buffer;

        @BeforeEach
        void setUp() {
            buffer = new TerminalBuffer(10, 3, 100);
        }

        @Test
        @DisplayName("Should get character at position")
        void testGetCharAt() {
            buffer.writeText("Hello");
            
            assertEquals('H', buffer.getCharAt(0, 0));
            assertEquals('e', buffer.getCharAt(0, 1));
            assertEquals('l', buffer.getCharAt(0, 2));
            assertEquals('l', buffer.getCharAt(0, 3));
            assertEquals('o', buffer.getCharAt(0, 4));
        }

        @Test
        @DisplayName("Should throw exception for invalid position")
        void testGetCharAtInvalidPosition() {
            assertThrows(IllegalArgumentException.class, () -> buffer.getCharAt(0, -1));
            assertThrows(IllegalArgumentException.class, () -> buffer.getCharAt(0, 10));
            assertThrows(IllegalArgumentException.class, () -> buffer.getCharAt(3, 0));
            assertThrows(IllegalArgumentException.class, () -> buffer.getCharAt(-10, 0));
        }

        @Test
        @DisplayName("Should get attributes at position")
        void testGetAttributesAt() {
            CellAttributes attrs = new CellAttributes(
                Color.YELLOW,
                Color.BLACK,
                new StyleFlags(true, true, true)
            );
            buffer.setCurrentAttributes(attrs);
            buffer.writeText("Test");
            
            assertEquals(attrs, buffer.getAttributesAt(0, 0));
            assertEquals(attrs, buffer.getAttributesAt(0, 3));
        }

        @Test
        @DisplayName("Should get line as string")
        void testGetLineAsString() {
            buffer.setCursorPosition(1, 0);
            buffer.writeText("Middle");
            
            assertEquals("          ", buffer.getLineAsString(0));
            assertEquals("Middle    ", buffer.getLineAsString(1));
            assertEquals("          ", buffer.getLineAsString(2));
        }

        @Test
        @DisplayName("Should get screen as string")
        void testGetScreenAsString() {
            buffer.setCursorPosition(0, 0);
            buffer.writeText("Line1");
            buffer.setCursorPosition(1, 0);
            buffer.writeText("Line2");
            buffer.setCursorPosition(2, 0);
            buffer.writeText("Line3");
            
            String expected = "Line1     \nLine2     \nLine3     ";
            assertEquals(expected, buffer.getScreenAsString());
        }

        @Test
        @DisplayName("Should get all content including scrollback")
        void testGetAllContentAsString() {
            buffer.setCursorPosition(0, 0);
            buffer.writeText("Scroll1");
            buffer.insertEmptyLineAtBottom();
            
            buffer.setCursorPosition(0, 0);
            buffer.writeText("Scroll2");
            buffer.insertEmptyLineAtBottom();
            
            buffer.setCursorPosition(0, 0);
            buffer.writeText("Screen1");
            buffer.setCursorPosition(1, 0);
            buffer.writeText("Screen2");
            buffer.setCursorPosition(2, 0);
            buffer.writeText("Screen3");
            
            String allContent = buffer.getAllContentAsString();
            
            assertTrue(allContent.contains("Scroll1"));
            assertTrue(allContent.contains("Scroll2"));
            assertTrue(allContent.contains("Screen1"));
            assertTrue(allContent.contains("Screen2"));
            assertTrue(allContent.contains("Screen3"));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle 1x1 buffer")
        void testMinimalBuffer() {
            TerminalBuffer buffer = new TerminalBuffer(1, 1, 0);
            
            buffer.writeText("X");
            assertEquals("X", buffer.getLineAsString(0));
        }

        @Test
        @DisplayName("Should handle large buffer dimensions")
        void testLargeBuffer() {
            TerminalBuffer buffer = new TerminalBuffer(200, 100, 10000);
            
            assertEquals(200, buffer.getWidth());
            assertEquals(100, buffer.getHeight());
        }

        @Test
        @DisplayName("Should handle zero scrollback correctly")
        void testZeroScrollbackBehavior() {
            TerminalBuffer buffer = new TerminalBuffer(10, 3, 0);
            
            buffer.writeText("Test");
            buffer.insertEmptyLineAtBottom();
            
            assertEquals(0, buffer.getScrollbackSize());
        }

        @Test
        @DisplayName("Should handle writing at last position")
        void testWriteAtLastPosition() {
            TerminalBuffer buffer = new TerminalBuffer(10, 5, 100);
            
            buffer.setCursorPosition(4, 9);
            buffer.writeText("X");
            
            assertEquals('X', buffer.getCharAt(4, 9));
        }

        @Test
        @DisplayName("Should handle multiple attribute changes")
        void testMultipleAttributeChanges() {
            TerminalBuffer buffer = new TerminalBuffer(20, 3, 100);
            
            buffer.setCurrentAttributes(new CellAttributes(Color.RED, Color.DEFAULT, StyleFlags.DEFAULT));
            buffer.writeText("Red");
            
            buffer.setCurrentAttributes(new CellAttributes(Color.GREEN, Color.DEFAULT, StyleFlags.DEFAULT));
            buffer.writeText("Green");
            
            buffer.setCurrentAttributes(new CellAttributes(Color.BLUE, Color.DEFAULT, StyleFlags.DEFAULT));
            buffer.writeText("Blue");
            
            assertEquals(Color.RED, buffer.getAttributesAt(0, 0).getForeground());
            assertEquals(Color.GREEN, buffer.getAttributesAt(0, 3).getForeground());
            assertEquals(Color.BLUE, buffer.getAttributesAt(0, 8).getForeground());
        }
    }
}
