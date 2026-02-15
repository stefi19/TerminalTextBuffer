package org.example;

import org.example.terminal.model.CellAttributes;
import org.example.terminal.model.Color;
import org.example.terminal.model.StyleFlags;

import org.example.terminal.model.CellAttributes;
import org.example.terminal.model.Color;
import org.example.terminal.model.StyleFlags;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the CellAttributes class.
 */
@DisplayName("CellAttributes Tests")
public class CellAttributesTest {

    @Test
    @DisplayName("Should create default attributes")
    void testDefaultAttributes() {
        assertEquals(Color.DEFAULT, CellAttributes.DEFAULT.getForeground());
        assertEquals(Color.DEFAULT, CellAttributes.DEFAULT.getBackground());
        assertEquals(StyleFlags.DEFAULT, CellAttributes.DEFAULT.getStyleFlags());
    }

    @Test
    @DisplayName("Should create attributes with specified values")
    void testCreateWithValues() {
        StyleFlags style = new StyleFlags(true, false, true);
        CellAttributes attrs = new CellAttributes(Color.RED, Color.BLUE, style);
        
        assertEquals(Color.RED, attrs.getForeground());
        assertEquals(Color.BLUE, attrs.getBackground());
        assertEquals(style, attrs.getStyleFlags());
    }

    @Test
    @DisplayName("Should use defaults for null values")
    void testNullValues() {
        CellAttributes attrs = new CellAttributes(null, null, null);
        
        assertEquals(Color.DEFAULT, attrs.getForeground());
        assertEquals(Color.DEFAULT, attrs.getBackground());
        assertEquals(StyleFlags.DEFAULT, attrs.getStyleFlags());
    }

    @Test
    @DisplayName("Should create new instance with foreground changed")
    void testWithForeground() {
        CellAttributes original = new CellAttributes(Color.RED, Color.BLUE, StyleFlags.DEFAULT);
        CellAttributes modified = original.withForeground(Color.GREEN);
        
        assertEquals(Color.GREEN, modified.getForeground());
        assertEquals(Color.BLUE, modified.getBackground());
        assertEquals(Color.RED, original.getForeground()); // Original unchanged
    }

    @Test
    @DisplayName("Should create new instance with background changed")
    void testWithBackground() {
        CellAttributes original = new CellAttributes(Color.RED, Color.BLUE, StyleFlags.DEFAULT);
        CellAttributes modified = original.withBackground(Color.GREEN);
        
        assertEquals(Color.RED, modified.getForeground());
        assertEquals(Color.GREEN, modified.getBackground());
    }

    @Test
    @DisplayName("Should create new instance with style flags changed")
    void testWithStyleFlags() {
        StyleFlags newStyle = new StyleFlags(true, true, true);
        CellAttributes original = new CellAttributes(Color.RED, Color.BLUE, StyleFlags.DEFAULT);
        CellAttributes modified = original.withStyleFlags(newStyle);
        
        assertEquals(Color.RED, modified.getForeground());
        assertEquals(Color.BLUE, modified.getBackground());
        assertEquals(newStyle, modified.getStyleFlags());
    }

    @Test
    @DisplayName("Should be equal when all values match")
    void testEquals() {
        CellAttributes attrs1 = new CellAttributes(
            Color.RED, 
            Color.BLUE, 
            new StyleFlags(true, false, true)
        );
        CellAttributes attrs2 = new CellAttributes(
            Color.RED, 
            Color.BLUE, 
            new StyleFlags(true, false, true)
        );
        
        assertEquals(attrs1, attrs2);
        assertEquals(attrs1.hashCode(), attrs2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values differ")
    void testNotEquals() {
        CellAttributes attrs1 = new CellAttributes(Color.RED, Color.BLUE, StyleFlags.DEFAULT);
        CellAttributes attrs2 = new CellAttributes(Color.GREEN, Color.BLUE, StyleFlags.DEFAULT);
        
        assertNotEquals(attrs1, attrs2);
    }
}
