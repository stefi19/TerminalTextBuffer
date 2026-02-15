package org.example;

import java.awt.Color;

/**
 * Maps terminal Color enum values to java.awt.Color for rendering.
 * Uses standard ANSI terminal color palette.
 */
public class ColorMapper {
    
    /**
     * Converts a terminal Color to java.awt.Color for rendering.
     * 
     * @param color the terminal color
     * @return the corresponding AWT color
     */
    public static Color toAwtColor(org.example.Color color) {
        return switch (color) {
            case BLACK -> new Color(0, 0, 0);
            case RED -> new Color(205, 49, 49);
            case GREEN -> new Color(13, 188, 121);
            case YELLOW -> new Color(229, 229, 16);
            case BLUE -> new Color(36, 114, 200);
            case MAGENTA -> new Color(188, 63, 188);
            case CYAN -> new Color(17, 168, 205);
            case WHITE -> new Color(229, 229, 229);
            case BRIGHT_BLACK -> new Color(102, 102, 102);
            case BRIGHT_RED -> new Color(241, 76, 76);
            case BRIGHT_GREEN -> new Color(35, 209, 139);
            case BRIGHT_YELLOW -> new Color(245, 245, 67);
            case BRIGHT_BLUE -> new Color(59, 142, 234);
            case BRIGHT_MAGENTA -> new Color(214, 112, 214);
            case BRIGHT_CYAN -> new Color(41, 184, 219);
            case BRIGHT_WHITE -> new Color(255, 255, 255);
            case DEFAULT -> null; // null means use default foreground/background
        };
    }
    
    /**
     * Gets the default foreground color for terminal rendering.
     */
    public static Color getDefaultForeground() {
        return new Color(229, 229, 229); // Light gray
    }
    
    /**
     * Gets the default background color for terminal rendering.
     */
    public static Color getDefaultBackground() {
        return new Color(30, 30, 30); // Dark gray/black
    }
}
