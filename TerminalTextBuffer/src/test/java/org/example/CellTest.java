package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Cell class.
 */
@DisplayName("Cell Tests")
public class CellTest {

    @Test
    @DisplayName("Should create empty cell")
    void testEmptyCell() {
        assertEquals(' ', Cell.EMPTY.getCharacter());
        assertEquals(CellAttributes.DEFAULT, Cell.EMPTY.getAttributes());
        assertTrue(Cell.EMPTY.isEmpty());
    }

    @Test
    @DisplayName("Should create cell with character and attributes")
    void testCreateCell() {
        CellAttributes attrs = new CellAttributes(Color.RED, Color.BLUE, StyleFlags.DEFAULT);
        Cell cell = new Cell('X', attrs);
        
        assertEquals('X', cell.getCharacter());
        assertEquals(attrs, cell.getAttributes());
        assertFalse(cell.isEmpty());
    }

    @Test
    @DisplayName("Should use default attributes when null provided")
    void testNullAttributes() {
        Cell cell = new Cell('Y', null);
        
        assertEquals('Y', cell.getCharacter());
        assertEquals(CellAttributes.DEFAULT, cell.getAttributes());
    }

    @Test
    @DisplayName("Should identify empty cells")
    void testIsEmpty() {
        Cell spaceCell = new Cell(' ', CellAttributes.DEFAULT);
        Cell nullCell = new Cell('\0', CellAttributes.DEFAULT);
        Cell charCell = new Cell('A', CellAttributes.DEFAULT);
        
        assertTrue(spaceCell.isEmpty());
        assertTrue(nullCell.isEmpty());
        assertFalse(charCell.isEmpty());
    }

    @Test
    @DisplayName("Should be equal when character and attributes match")
    void testEquals() {
        CellAttributes attrs = new CellAttributes(Color.GREEN, Color.BLACK, StyleFlags.DEFAULT);
        Cell cell1 = new Cell('Z', attrs);
        Cell cell2 = new Cell('Z', attrs);
        
        assertEquals(cell1, cell2);
        assertEquals(cell1.hashCode(), cell2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when character differs")
    void testNotEqualsCharacter() {
        CellAttributes attrs = CellAttributes.DEFAULT;
        Cell cell1 = new Cell('A', attrs);
        Cell cell2 = new Cell('B', attrs);
        
        assertNotEquals(cell1, cell2);
    }

    @Test
    @DisplayName("Should not be equal when attributes differ")
    void testNotEqualsAttributes() {
        CellAttributes attrs1 = new CellAttributes(Color.RED, Color.DEFAULT, StyleFlags.DEFAULT);
        CellAttributes attrs2 = new CellAttributes(Color.BLUE, Color.DEFAULT, StyleFlags.DEFAULT);
        Cell cell1 = new Cell('A', attrs1);
        Cell cell2 = new Cell('A', attrs2);
        
        assertNotEquals(cell1, cell2);
    }
}
