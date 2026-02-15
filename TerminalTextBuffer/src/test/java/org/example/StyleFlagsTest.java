package org.example;

import org.example.terminal.model.StyleFlags;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the StyleFlags class.
 */
@DisplayName("StyleFlags Tests")
public class StyleFlagsTest {

    @Test
    @DisplayName("Should create default style with no flags")
    void testDefaultStyle() {
        assertFalse(StyleFlags.DEFAULT.isBold());
        assertFalse(StyleFlags.DEFAULT.isItalic());
        assertFalse(StyleFlags.DEFAULT.isUnderline());
    }

    @Test
    @DisplayName("Should create style with specified flags")
    void testCreateWithFlags() {
        StyleFlags style = new StyleFlags(true, false, true);
        
        assertTrue(style.isBold());
        assertFalse(style.isItalic());
        assertTrue(style.isUnderline());
    }

    @Test
    @DisplayName("Should create new instance with bold changed")
    void testWithBold() {
        StyleFlags original = new StyleFlags(false, true, false);
        StyleFlags modified = original.withBold(true);
        
        assertTrue(modified.isBold());
        assertTrue(modified.isItalic());
        assertFalse(modified.isUnderline());
        
        // Original should be unchanged
        assertFalse(original.isBold());
    }

    @Test
    @DisplayName("Should create new instance with italic changed")
    void testWithItalic() {
        StyleFlags original = new StyleFlags(true, false, false);
        StyleFlags modified = original.withItalic(true);
        
        assertTrue(modified.isBold());
        assertTrue(modified.isItalic());
        assertFalse(modified.isUnderline());
    }

    @Test
    @DisplayName("Should create new instance with underline changed")
    void testWithUnderline() {
        StyleFlags original = new StyleFlags(true, false, false);
        StyleFlags modified = original.withUnderline(true);
        
        assertTrue(modified.isBold());
        assertFalse(modified.isItalic());
        assertTrue(modified.isUnderline());
    }

    @Test
    @DisplayName("Should be equal when flags match")
    void testEquals() {
        StyleFlags style1 = new StyleFlags(true, false, true);
        StyleFlags style2 = new StyleFlags(true, false, true);
        
        assertEquals(style1, style2);
        assertEquals(style1.hashCode(), style2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when flags differ")
    void testNotEquals() {
        StyleFlags style1 = new StyleFlags(true, false, true);
        StyleFlags style2 = new StyleFlags(false, false, true);
        
        assertNotEquals(style1, style2);
    }
}
