package org.example.terminal.util;

import org.example.terminal.model.Color;

/**
 * Maps terminal Color enum values to java.awt.Color for rendering.
 * Uses standard ANSI terminal color palette.
 */
public class ColorMapper {
    
    /**
     * Converts a terminal Color to java.awt.Color for rendering.
     * 
     * @param color the terminal color
     * @return the corresponding AWT color, or null for DEFAULT
     */
    public static java.awt.Color toAwtColor(Color color) {
        return switch (color) {
            case BLACK -> new java.awt.Color(0, 0, 0);
            case RED -> new java.awt.Color(205, 49, 49);
            case GREEN -> new java.awt.Color(13, 188, 121);
            case YELLOW -> new java.awt.Color(229, 229, 16);
            case BLUE -> new java.awt.Color(36, 114, 200);
            case MAGENTA -> new java.awt.Color(188, 63, 188);
            case CYAN -> new java.awt.Color(17, 168, 205);
            case WHITE -> new java.awt.Color(229, 229, 229);
            case BRIGHT_BLACK -> new java.awt.Color(102, 102, 102);
            case BRIGHT_RED -> new java.awt.Color(241, 76, 76);
            case BRIGHT_GREEN -> new java.awt.Color(35, 209, 139);
            case BRIGHT_YELLOW -> new java.awt.Color(245, 245, 67);
            case BRIGHT_BLUE -> new java.awt.Color(59, 142, 234);
            case BRIGHT_MAGENTA -> new java.awt.Color(214, 112, 214);
            case BRIGHT_CYAN -> new java.awt.Color(41, 184, 219);
            case BRIGHT_WHITE -> new java.awt.Color(255, 255, 255);
            case DEFAULT -> null; // null means use default foreground/background
        };
    }
    
    /**
     * Gets the default foreground color for terminal rendering.
     * 
     * @return light gray color for text
     */
    public static java.awt.Color getDefaultForeground() {
        return new java.awt.Color(229, 229, 229);
    }
    
    /**
     * Gets the default background color for terminal rendering.
     * 
     * @return dark gray/black color for background
     */
    public static java.awt.Color getDefaultBackground() {
        return new java.awt.Color(30, 30, 30);
    }
}
