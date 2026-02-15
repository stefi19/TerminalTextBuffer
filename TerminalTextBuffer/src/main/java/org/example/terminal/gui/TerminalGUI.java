package org.example.terminal.gui;

import org.example.terminal.buffer.TerminalBuffer;
import org.example.terminal.model.Cell;
import org.example.terminal.model.CellAttributes;
import org.example.terminal.model.Color;
import org.example.terminal.model.StyleFlags;
import org.example.terminal.util.ColorMapper;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * A GUI application that demonstrates the TerminalBuffer with visual rendering.
 * Shows colors, styles (bold, italic, underline), and interactive features.
 * Includes controls for styling text and selecting colors.
 */
public class TerminalGUI {
    private JFrame frame;
    private TerminalBuffer buffer;
    private TerminalPanel terminalPanel;
    private JButton interactiveModeButton;
    private JLabel statusLabel;
    
    // Style controls
    private JCheckBox boldCheckBox;
    private JCheckBox italicCheckBox;
    private JCheckBox underlineCheckBox;
    
    // Color controls
    private JComboBox<Color> foregroundCombo;
    private JComboBox<Color> backgroundCombo;

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

        // Create control panel at the bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Main control panel with buttons
        JPanel controlPanel = new JPanel(new FlowLayout());
        
        // Status label
        statusLabel = new JLabel("Demo Mode");
        statusLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        
        // Interactive mode toggle button
        interactiveModeButton = new JButton("Enable Interactive Mode");
        interactiveModeButton.addActionListener(e -> toggleInteractiveMode());
        
        JButton clearButton = new JButton("Clear Screen");
        clearButton.addActionListener(e -> {
            buffer.clearScreen();
            terminalPanel.repaint();
        });
        
        JButton demoButton = new JButton("Run Demo");
        demoButton.addActionListener(e -> {
            if (terminalPanel.isInteractiveMode()) {
                JOptionPane.showMessageDialog(frame, 
                    "Disable Interactive Mode first to run the demo.",
                    "Interactive Mode Active", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                runDemo();
            }
        });
        
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
        
        controlPanel.add(statusLabel);
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL));
        controlPanel.add(interactiveModeButton);
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL));
        controlPanel.add(new JLabel("Font Size:"));
        controlPanel.add(fontSizeCombo);
        controlPanel.add(clearButton);
        controlPanel.add(demoButton);
        controlPanel.add(scrollbackButton);
        
        // Style and color panel
        JPanel stylePanel = createStylePanel();
        
        bottomPanel.add(controlPanel, BorderLayout.CENTER);
        bottomPanel.add(stylePanel, BorderLayout.SOUTH);
        
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private JPanel createStylePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Text Styling"));
        
        // Style checkboxes
        boldCheckBox = new JCheckBox("Bold");
        boldCheckBox.addActionListener(e -> {
            updateCurrentAttributes();
            if (terminalPanel.isInteractiveMode()) {
                terminalPanel.requestFocusInWindow();
            }
        });
        
        italicCheckBox = new JCheckBox("Italic");
        italicCheckBox.addActionListener(e -> {
            updateCurrentAttributes();
            if (terminalPanel.isInteractiveMode()) {
                terminalPanel.requestFocusInWindow();
            }
        });
        
        underlineCheckBox = new JCheckBox("Underline");
        underlineCheckBox.addActionListener(e -> {
            updateCurrentAttributes();
            if (terminalPanel.isInteractiveMode()) {
                terminalPanel.requestFocusInWindow();
            }
        });
        
        // Color combos
        Color[] colors = Color.values();
        
        foregroundCombo = new JComboBox<>(colors);
        foregroundCombo.setSelectedItem(Color.DEFAULT);
        foregroundCombo.setRenderer(new ColorComboRenderer());
        foregroundCombo.addActionListener(e -> {
            updateCurrentAttributes();
            if (terminalPanel.isInteractiveMode()) {
                terminalPanel.requestFocusInWindow();
            }
        });
        
        backgroundCombo = new JComboBox<>(colors);
        backgroundCombo.setSelectedItem(Color.DEFAULT);
        backgroundCombo.setRenderer(new ColorComboRenderer());
        backgroundCombo.addActionListener(e -> {
            updateCurrentAttributes();
            if (terminalPanel.isInteractiveMode()) {
                terminalPanel.requestFocusInWindow();
            }
        });
        
        panel.add(boldCheckBox);
        panel.add(italicCheckBox);
        panel.add(underlineCheckBox);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(new JLabel("Foreground:"));
        panel.add(foregroundCombo);
        panel.add(new JLabel("Background:"));
        panel.add(backgroundCombo);
        
        return panel;
    }
    
    private void updateCurrentAttributes() {
        StyleFlags styleFlags = new StyleFlags(
            boldCheckBox.isSelected(),
            italicCheckBox.isSelected(),
            underlineCheckBox.isSelected()
        );
        
        Color fg = (Color) foregroundCombo.getSelectedItem();
        Color bg = (Color) backgroundCombo.getSelectedItem();
        
        CellAttributes newAttrs = new CellAttributes(
            fg != null ? fg : Color.DEFAULT,
            bg != null ? bg : Color.DEFAULT,
            styleFlags
        );
        
        buffer.setCurrentAttributes(newAttrs);
    }
    
    /**
     * Custom renderer for color combo boxes to show color names with visual preview.
     */
    private static class ColorComboRenderer extends DefaultListCellRenderer {
        @Override
        public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Color) {
                Color color = (Color) value;
                setText(color.name());
                
                // Show color preview (except for DEFAULT)
                if (color != Color.DEFAULT) {
                    java.awt.Color awtColor = ColorMapper.toAwtColor(color);
                    setIcon(new ColorIcon(awtColor));
                }
            }
            
            return this;
        }
    }
    
    /**
     * Simple icon that shows a color square.
     */
    private static class ColorIcon implements Icon {
        private final java.awt.Color color;
        private static final int SIZE = 16;
        
        public ColorIcon(java.awt.Color color) {
            this.color = color;
        }
        
        @Override
        public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, SIZE, SIZE);
            g.setColor(java.awt.Color.BLACK);
            g.drawRect(x, y, SIZE, SIZE);
        }
        
        @Override
        public int getIconWidth() {
            return SIZE;
        }
        
        @Override
        public int getIconHeight() {
            return SIZE;
        }
    }
    
    private void toggleInteractiveMode() {
        boolean newMode = !terminalPanel.isInteractiveMode();
        terminalPanel.setInteractiveMode(newMode);
        
        if (newMode) {
            // Entering interactive mode
            interactiveModeButton.setText("Disable Interactive Mode");
            statusLabel.setText("Interactive Mode - Type to test!");
            statusLabel.setForeground(new java.awt.Color(0, 150, 0));
            
            buffer.clearScreen();
            buffer.setCursorPosition(0, 0);
            
            // Write instructions
            buffer.setCurrentAttributes(new CellAttributes(
                org.example.terminal.model.Color.BRIGHT_CYAN,
                org.example.terminal.model.Color.DEFAULT,
                new StyleFlags(true, false, false)
            ));
            buffer.writeText("=== INTERACTIVE MODE ===");
            
            buffer.setCursorPosition(2, 0);
            buffer.setCurrentAttributes(new CellAttributes(
                org.example.terminal.model.Color.BRIGHT_WHITE,
                org.example.terminal.model.Color.DEFAULT,
                StyleFlags.DEFAULT
            ));
            buffer.writeText("Start typing! Try:");
            
            buffer.setCursorPosition(3, 2);
            buffer.setCurrentAttributes(new CellAttributes(
                org.example.terminal.model.Color.YELLOW,
                org.example.terminal.model.Color.DEFAULT,
                StyleFlags.DEFAULT
            ));
            buffer.writeText("- Regular text");
            
            buffer.setCursorPosition(4, 2);
            buffer.writeText("- Arrow keys to move cursor");
            
            buffer.setCursorPosition(5, 2);
            buffer.writeText("- Enter for new line");
            
            buffer.setCursorPosition(6, 2);
            buffer.writeText("- Backspace to delete");
            
            buffer.setCursorPosition(7, 2);
            buffer.writeText("- Tab for spaces");
            
            buffer.setCursorPosition(9, 0);
            buffer.setCurrentAttributes(new CellAttributes(
                org.example.terminal.model.Color.BRIGHT_GREEN,
                org.example.terminal.model.Color.DEFAULT,
                new StyleFlags(false, true, false)
            ));
            buffer.writeText("Type below this line:");
            
            buffer.setCursorPosition(10, 0);
            buffer.setCurrentAttributes(CellAttributes.DEFAULT);
            
            terminalPanel.repaint();
            terminalPanel.requestFocusInWindow();
        } else {
            // Exiting interactive mode
            interactiveModeButton.setText("Enable Interactive Mode");
            statusLabel.setText("Demo Mode");
            statusLabel.setForeground(java.awt.Color.BLACK);
        }
    }

    private void runDemo() {
        buffer.clearScreen();
        buffer.setCursorPosition(0, 0);
        
        // Title
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_CYAN,
            org.example.terminal.model.Color.DEFAULT,
            new StyleFlags(true, false, false)
        ));
        buffer.writeText("=== Terminal Buffer Visual Demo ===");
        
        // Section 1: Colors
        buffer.setCursorPosition(2, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_WHITE,
            org.example.terminal.model.Color.DEFAULT,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("1. Color Demonstration:");
        
        buffer.setCursorPosition(3, 3);
        demonstrateColors();
        
        // Section 2: Styles
        buffer.setCursorPosition(6, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_WHITE,
            org.example.terminal.model.Color.DEFAULT,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("2. Style Demonstration:");
        
        buffer.setCursorPosition(7, 3);
        demonstrateStyles();
        
        // Section 3: Background colors
        buffer.setCursorPosition(11, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_WHITE,
            org.example.terminal.model.Color.DEFAULT,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("3. Background Colors:");
        
        buffer.setCursorPosition(12, 3);
        demonstrateBackgrounds();
        
        // Section 4: Combined styles
        buffer.setCursorPosition(15, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_WHITE,
            org.example.terminal.model.Color.DEFAULT,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("4. Combined Styles:");
        
        buffer.setCursorPosition(16, 3);
        demonstrateCombined();
        
        // Section 5: Wide characters (CJK, emoji)
        buffer.setCursorPosition(19, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_WHITE,
            org.example.terminal.model.Color.DEFAULT,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("5. Wide Characters (2-cell):");
        
        buffer.setCursorPosition(20, 3);
        demonstrateWideCharacters();
        
        // Footer
        buffer.setCursorPosition(22, 0);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_BLACK,
            org.example.terminal.model.Color.DEFAULT,
            new StyleFlags(false, true, false)
        ));
        buffer.writeText("Use the buttons and style controls below to interact!");
        
        terminalPanel.repaint();
    }

    private void demonstrateColors() {
        org.example.terminal.model.Color[] colors = {
            org.example.terminal.model.Color.RED,
            org.example.terminal.model.Color.GREEN,
            org.example.terminal.model.Color.YELLOW,
            org.example.terminal.model.Color.BLUE,
            org.example.terminal.model.Color.MAGENTA,
            org.example.terminal.model.Color.CYAN
        };
        
        String[] names = {"Red", "Green", "Yellow", "Blue", "Magenta", "Cyan"};
        
        int row = buffer.getCursor().getRow();
        for (int i = 0; i < colors.length; i++) {
            buffer.setCursorPosition(row, 3 + i * 12);
            buffer.setCurrentAttributes(new CellAttributes(
                colors[i],
                org.example.terminal.model.Color.DEFAULT,
                StyleFlags.DEFAULT
            ));
            buffer.writeText(names[i]);
        }
        
        // Bright colors on next line
        buffer.setCursorPosition(row + 1, 3);
        org.example.terminal.model.Color[] brightColors = {
            org.example.terminal.model.Color.BRIGHT_RED,
            org.example.terminal.model.Color.BRIGHT_GREEN,
            org.example.terminal.model.Color.BRIGHT_YELLOW,
            org.example.terminal.model.Color.BRIGHT_BLUE,
            org.example.terminal.model.Color.BRIGHT_MAGENTA,
            org.example.terminal.model.Color.BRIGHT_CYAN
        };
        
        for (int i = 0; i < brightColors.length; i++) {
            buffer.setCursorPosition(row + 1, 3 + i * 12);
            buffer.setCurrentAttributes(new CellAttributes(
                brightColors[i],
                org.example.terminal.model.Color.DEFAULT,
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
            org.example.terminal.model.Color.GREEN,
            org.example.terminal.model.Color.DEFAULT,
            new StyleFlags(true, false, false)
        ));
        buffer.writeText("Bold Text");
        
        // Italic
        buffer.setCursorPosition(row + 1, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.YELLOW,
            org.example.terminal.model.Color.DEFAULT,
            new StyleFlags(false, true, false)
        ));
        buffer.writeText("Italic Text");
        
        // Underline
        buffer.setCursorPosition(row + 2, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.CYAN,
            org.example.terminal.model.Color.DEFAULT,
            new StyleFlags(false, false, true)
        ));
        buffer.writeText("Underlined Text");
    }

    private void demonstrateBackgrounds() {
        int row = buffer.getCursor().getRow();
        
        buffer.setCursorPosition(row, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.WHITE,
            org.example.terminal.model.Color.RED,
            StyleFlags.DEFAULT
        ));
        buffer.writeText(" Red BG ");
        
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.WHITE,
            org.example.terminal.model.Color.BLUE,
            StyleFlags.DEFAULT
        ));
        buffer.writeText(" Blue BG ");
        
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BLACK,
            org.example.terminal.model.Color.GREEN,
            StyleFlags.DEFAULT
        ));
        buffer.writeText(" Green BG ");
    }

    private void demonstrateCombined() {
        int row = buffer.getCursor().getRow();
        
        buffer.setCursorPosition(row, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_YELLOW,
            org.example.terminal.model.Color.BLUE,
            new StyleFlags(true, true, true)
        ));
        buffer.writeText("Bold+Italic+Underline with colors!");
        
        buffer.setCursorPosition(row + 1, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_WHITE,
            org.example.terminal.model.Color.MAGENTA,
            new StyleFlags(true, false, true)
        ));
        buffer.writeText("Bold+Underline on colored background");
    }
    
    private void demonstrateWideCharacters() {
        int row = buffer.getCursor().getRow();
        
        buffer.setCursorPosition(row, 3);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_GREEN,
            org.example.terminal.model.Color.DEFAULT,
            StyleFlags.DEFAULT
        ));
        // Note: Wide characters may not render properly depending on the font
        // These are examples of characters that occupy 2 cells in real terminals
        buffer.writeText("CJK: \u4E2D\u6587 ");  // Chinese characters
        
        buffer.setCursorPosition(row, 20);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_MAGENTA,
            org.example.terminal.model.Color.DEFAULT,
            StyleFlags.DEFAULT
        ));
        buffer.writeText("Katakana: \u30AB\u30BF\u30AB\u30CA");  // Japanese
        
        buffer.setCursorPosition(row, 40);
        buffer.setCurrentAttributes(new CellAttributes(
            org.example.terminal.model.Color.BRIGHT_CYAN,
            org.example.terminal.model.Color.DEFAULT,
            StyleFlags.DEFAULT
        ));
        buffer.writeText("Hangul: \uD55C\uAE00");  // Korean
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TerminalGUI());
    }
}
