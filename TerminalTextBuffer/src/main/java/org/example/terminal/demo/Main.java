package org.example.terminal.demo;

import org.example.terminal.buffer.Cursor;
import org.example.terminal.buffer.TerminalBuffer;
import org.example.terminal.model.CellAttributes;
import org.example.terminal.model.Color;
import org.example.terminal.model.StyleFlags;

/**
 * Demonstration of the TerminalBuffer functionality.
 * This shows how terminal emulators use the buffer to display text.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Terminal Text Buffer Demo ===\n");

        // Create a terminal buffer: 40 columns, 10 rows, 100 lines scrollback
        TerminalBuffer buffer = new TerminalBuffer(40, 10, 100);

        System.out.println("1. Basic text writing:");
        demonstrateBasicWriting(buffer);
        
        System.out.println("\n2. Cursor movement:");
        demonstrateCursorMovement(buffer);
        
        System.out.println("\n3. Styled text:");
        demonstrateStyledText(buffer);
        
        System.out.println("\n4. Line operations:");
        demonstrateLineOperations(buffer);
        
        System.out.println("\n5. Scrollback:");
        demonstrateScrollback(buffer);
    }

    private static void demonstrateBasicWriting(TerminalBuffer buffer) {
        buffer.clearScreen();
        buffer.setCursorPosition(0, 0);
        buffer.writeText("Hello, Terminal!");
        
        System.out.println("Wrote 'Hello, Terminal!' at (0,0):");
        printScreen(buffer);
    }

    private static void demonstrateCursorMovement(TerminalBuffer buffer) {
        buffer.clearScreen();
        
        // Write at different positions
        buffer.setCursorPosition(2, 5);
        buffer.writeText("Center");
        
        buffer.setCursorPosition(0, 0);
        buffer.writeText("Top-Left");
        
        buffer.setCursorPosition(9, 30);
        buffer.writeText("Bottom");
        
        System.out.println("Text at various positions:");
        printScreen(buffer);
    }

    private static void demonstrateStyledText(TerminalBuffer buffer) {
        buffer.clearScreen();
        
        // Write with different colors
        buffer.setCursorPosition(0, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            Color.RED, Color.DEFAULT, StyleFlags.DEFAULT
        ));
        buffer.writeText("Red text ");
        
        buffer.setCurrentAttributes(new CellAttributes(
            Color.GREEN, Color.DEFAULT, StyleFlags.DEFAULT
        ));
        buffer.writeText("Green text");
        
        // Write with bold style
        buffer.setCursorPosition(2, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            Color.BLUE, 
            Color.DEFAULT, 
            new StyleFlags(true, false, false)
        ));
        buffer.writeText("Bold blue text");
        
        // Write with underline
        buffer.setCursorPosition(4, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            Color.YELLOW, 
            Color.BLACK, 
            new StyleFlags(false, false, true)
        ));
        buffer.writeText("Underlined with background");
        
        System.out.println("Text with different attributes:");
        printScreen(buffer);
        System.out.println("(Note: attributes are stored but not visually displayed in this demo)");
    }

    private static void demonstrateLineOperations(TerminalBuffer buffer) {
        buffer.clearScreen();
        
        // Fill a line with dashes
        buffer.fillLine(3, '-');
        
        buffer.setCursorPosition(1, 0);
        buffer.writeText("Line above separator");
        
        buffer.setCursorPosition(5, 0);
        buffer.writeText("Line below separator");
        
        System.out.println("Line filled with dashes:");
        printScreen(buffer);
    }

    private static void demonstrateScrollback(TerminalBuffer buffer) {
        buffer.clearScreen();
        
        // Fill screen with numbered lines
        for (int i = 0; i < 10; i++) {
            buffer.setCursorPosition(i, 0);
            buffer.writeText("Screen line " + i);
        }
        
        System.out.println("Initial screen (before scrolling):");
        printScreen(buffer);
        
        // Add lines, pushing top lines to scrollback
        System.out.println("\nAdding new lines (old lines scroll up)...");
        for (int i = 0; i < 3; i++) {
            buffer.insertEmptyLineAtBottom();
            buffer.setCursorPosition(9, 0);
            buffer.writeText("New line " + (i + 1));
        }
        
        System.out.println("\nCurrent screen:");
        printScreen(buffer);
        
        System.out.println("\nScrollback buffer (3 lines):");
        for (int i = -3; i < 0; i++) {
            System.out.println("  " + buffer.getLineAsString(i));
        }
        
        System.out.println("\nAll content (scrollback + screen):");
        String[] lines = buffer.getAllContentAsString().split("\n");
        for (int i = 0; i < lines.length; i++) {
            System.out.printf("  %2d: %s\n", i, lines[i]);
        }
    }

    private static void printScreen(TerminalBuffer buffer) {
        String[] lines = buffer.getScreenAsString().split("\n");
        System.out.println("  +" + "-".repeat(buffer.getWidth()) + "+");
        for (String line : lines) {
            System.out.println("  |" + line + "|");
        }
        System.out.println("  +" + "-".repeat(buffer.getWidth()) + "+");
        
        Cursor cursor = buffer.getCursor();
        System.out.println("  Cursor: row=" + cursor.getRow() + ", col=" + cursor.getColumn());
    }
}