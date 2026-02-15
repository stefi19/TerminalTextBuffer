package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ColorMapper utility class.
 */
@DisplayName("ColorMapper Tests")
public class ColorMapperTest {

    @Test
    @DisplayName("Should map basic colors correctly")
    void testBasicColors() {
        assertNotNull(ColorMapper.toAwtColor(Color.RED));
        assertNotNull(ColorMapper.toAwtColor(Color.GREEN));
        assertNotNull(ColorMapper.toAwtColor(Color.BLUE));
        assertNotNull(ColorMapper.toAwtColor(Color.YELLOW));
        assertNotNull(ColorMapper.toAwtColor(Color.MAGENTA));
        assertNotNull(ColorMapper.toAwtColor(Color.CYAN));
        assertNotNull(ColorMapper.toAwtColor(Color.BLACK));
        assertNotNull(ColorMapper.toAwtColor(Color.WHITE));
    }

    @Test
    @DisplayName("Should map bright colors correctly")
    void testBrightColors() {
        assertNotNull(ColorMapper.toAwtColor(Color.BRIGHT_RED));
        assertNotNull(ColorMapper.toAwtColor(Color.BRIGHT_GREEN));
        assertNotNull(ColorMapper.toAwtColor(Color.BRIGHT_BLUE));
        assertNotNull(ColorMapper.toAwtColor(Color.BRIGHT_YELLOW));
        assertNotNull(ColorMapper.toAwtColor(Color.BRIGHT_MAGENTA));
        assertNotNull(ColorMapper.toAwtColor(Color.BRIGHT_CYAN));
        assertNotNull(ColorMapper.toAwtColor(Color.BRIGHT_BLACK));
        assertNotNull(ColorMapper.toAwtColor(Color.BRIGHT_WHITE));
    }

    @Test
    @DisplayName("Should return null for DEFAULT color")
    void testDefaultColor() {
        assertNull(ColorMapper.toAwtColor(Color.DEFAULT));
    }

    @Test
    @DisplayName("Should provide default foreground color")
    void testDefaultForeground() {
        java.awt.Color fg = ColorMapper.getDefaultForeground();
        assertNotNull(fg);
        // Should be a light color for dark background
        assertTrue(fg.getRed() > 100 || fg.getGreen() > 100 || fg.getBlue() > 100);
    }

    @Test
    @DisplayName("Should provide default background color")
    void testDefaultBackground() {
        java.awt.Color bg = ColorMapper.getDefaultBackground();
        assertNotNull(bg);
        // Should be a dark color
        assertTrue(bg.getRed() < 100 && bg.getGreen() < 100 && bg.getBlue() < 100);
    }

    @Test
    @DisplayName("Bright colors should be brighter than normal colors")
    void testBrightVsNormal() {
        java.awt.Color red = ColorMapper.toAwtColor(Color.RED);
        java.awt.Color brightRed = ColorMapper.toAwtColor(Color.BRIGHT_RED);
        
        // Bright red should have higher RGB values
        assertTrue(brightRed.getRed() > red.getRed() ||
                   brightRed.getGreen() > red.getGreen() ||
                   brightRed.getBlue() > red.getBlue());
    }

    @Test
    @DisplayName("All mapped colors should be distinct")
    void testColorsAreDistinct() {
        java.awt.Color red = ColorMapper.toAwtColor(Color.RED);
        java.awt.Color green = ColorMapper.toAwtColor(Color.GREEN);
        java.awt.Color blue = ColorMapper.toAwtColor(Color.BLUE);
        
        assertNotEquals(red, green);
        assertNotEquals(red, blue);
        assertNotEquals(green, blue);
    }
}
