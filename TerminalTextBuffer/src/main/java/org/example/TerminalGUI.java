package org.example;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * A GUI application that demonstrates the TerminalBuffer with visual rendering.
 * Shows colors, styles (bold, italic, underline), and interactive features.
 */
public class TerminalGUI {
    private JFrame frame;
    private TerminalBuffer buffer;
    private TerminalPanel terminalPanel;

    public TerminalGUI() {
        // Create buffer: 80 columns, 24 rows, 1000 lines scrollback
        buffer = new TerminalBuffer(80, 24, 1000);
        
        createAndShowGUI();
        runDemo();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Terminal Text Buffer - Visual Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create terminal panel
        terminalPanel = new TerminalPanel(buffer);
        JScrollPane scrollPane = new JScrollPane(terminalPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        
        JButton clearButton = new JButton("Clear Screen");
        clearButton.addActionListener(e -> {
            buffer.clearScreen();
            terminalPanel.repaint();
        });
        
        JButton demoButton = new JButton("Run Demo");
        demoButton.addActionListener(e -> runDemo());
        
        JButton scrollbackButton = new JButton("Add Scrollback");
        scrollbackButton.addActionListener(e -> {
            buffer.insertEmptyLineAtBottom();
            buffer.setCursorPosition(buffer.getHeight() - 1, 0);
            buffer.writeText("New line added!");
            terminalPanel.repaint();
        });
        
        JComboBox<String> fontSizeCombo = new JComboBox<>(new String[]{"10", "12", "14", "16", "18", "20"});
        fontSizeCombo.setSelectedItem("14");
        fontSizeCombo.addActionListener(e -> {
            String size = (String) fontSizeCombo.getSelectedItem();
            terminalPanel.setFontSize(Integer.parseInt(size));
        });
        
        controlPanel.add(new JLabel("Font Size:"));
        controlPanel.add(fontSizeCombo);
        controlPanel.add(clearButton);
        controlPanel.add(demoButton);
        controlPanel.add(scrollbackButton);
        
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void runDemo() {
        buffer.clearScreen();
        buffer.setCursorPosition(0, 0);
        
        // Title
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.BRIGHT_CYAN,
            org.example.Color.DEFAULT,
            new StyleFlags(true, false, false)
        ));
        buffer.writeText("=== Terminal Buffer Visual Demo ===");
        
        // Section 1: Colors
        buffer.setCursorPosition(2, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.BRIGHT_WHITE,
            org.example.Color.DEFAULT,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("1. Color Demonstration:");
        
        buffer.setCursorPosition(3, 3);
        demonstrateColors();
        
        // Section 2: Styles
        buffer.setCursorPosition(6, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.BRIGHT_WHITE,
            org.example.Color.DEFAULT,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("2. Style Demonstration:");
        
        buffer.setCursorPosition(7, 3);
        demonstrateStyles();
        
        // Section 3: Background colors
        buffer.setCursorPosition(11, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.BRIGHT_WHITE,
            org.example.Color.DEFAULT,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("3. Background Colors:");
        
        buffer.setCursorPosition(12, 3);
        demonstrateBackgrounds();
        
        // Section 4: Combined styles
        buffer.setCursorPosition(15, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.BRIGHT_WHITE,
            org.example.Color.DEFAULT,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("4. Combined Styles:");
        
        buffer.setCursorPosition(16, 3);
        demonstrateCombined();
        
        // Footer
        buffer.setCursorPosition(20, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.BRIGHT_BLACK,
            org.example.Color.DEFAULT,
            new StyleFlags(false, true, false)
        ));
        buffer.writeText("Use the buttons below to interact with the terminal buffer!");
        
        terminalPanel.repaint();
    }

    private void demonstrateColors() {
        org.example.Color[] colors = {
            org.example.Color.RED,
            org.example.Color.GREEN,
            org.example.Color.YELLOW,
            org.example.Color.BLUE,
            org.example.Color.MAGENTA,
            org.example.Color.CYAN
        };
        
        String[] names = {"Red", "Green", "Yellow", "Blue", "Magenta", "Cyan"};
        
        int row = buffer.getCursor().getRow();
        for (int i = 0; i < colors.length; i++) {
            buffer.setCursorPosition(row, 3 + i * 12);
            buffer.setCurrentAttributes(new CellAttributes(
                colors[i],
                org.example.Color.DEFAULT,
                StyleFlags.DEFAULT
            ));
            buffer.writeText(names[i]);
        }
        
        // Bright colors on next line
        buffer.setCursorPosition(row + 1, 3);
        org.example.Color[] brightColors = {
            org.example.Color.BRIGHT_RED,
            org.example.Color.BRIGHT_GREEN,
            org.example.Color.BRIGHT_YELLOW,
            org.example.Color.BRIGHT_BLUE,
            org.example.Color.BRIGHT_MAGENTA,
            org.example.Color.BRIGHT_CYAN
        };
        
        for (int i = 0; i < brightColors.length; i++) {
            buffer.setCursorPosition(row + 1, 3 + i * 12);
            buffer.setCurrentAttributes(new CellAttributes(
                brightColors[i],
                org.example.Color.DEFAULT,
                StyleFlags.DEFAULT
            ));
            buffer.writeText("Bright");
        }
    }

    private void demonstrateStyles() {
        int row = buffer.getCursor().getRow();
        
        // Bold
        buffer.setCursorPosition(row, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.GREEN,
            org.example.Color.DEFAULT,
            new StyleFlags(true, false, false)
        ));
        buffer.writeText("Bold Text");
        
        // Italic
        buffer.setCursorPosition(row + 1, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.YELLOW,
            org.example.Color.DEFAULT,
            new StyleFlags(false, true, false)
        ));
        buffer.writeText("Italic Text");
        
        // Underline
        buffer.setCursorPosition(row + 2, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.CYAN,
            org.example.Color.DEFAULT,
            new StyleFlags(false, false, true)
        ));
        buffer.writeText("Underlined Text");
    }

    private void demonstrateBackgrounds() {
        int row = buffer.getCursor().getRow();
        
        buffer.setCursorPosition(row, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.WHITE,
            org.example.Color.RED,
            StyleFlags.DEFAULT
        ));
        buffer.writeText(" Red BG ");
        
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.WHITE,
            org.example.Color.BLUE,
            StyleFlags.DEFAULT
        ));
        buffer.writeText(" Blue BG ");
        
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.BLACK,
            org.example.Color.GREEN,
            StyleFlags.DEFAULT
        ));
        buffer.writeText(" Green BG ");
    }

    private void demonstrateCombined() {
        int row = buffer.getCursor().getRow();
        
        buffer.setCursorPosition(row, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.BRIGHT_YELLOW,
            org.example.Color.BLUE,
            new StyleFlags(true, true, true)
        ));
        buffer.writeText("Bold+Italic+Underline with colors!");
        
        buffer.setCursorPosition(row + 1, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.Color.BRIGHT_WHITE,
            org.example.Color.MAGENTA,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("Bold+Underline on colored background");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TerminalGUI());
    }
}
