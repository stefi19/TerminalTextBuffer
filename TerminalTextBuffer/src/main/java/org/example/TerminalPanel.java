package org.example;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A Swing component that renders a TerminalBuffer with full color and style support.
 * Displays the terminal content with proper colors, bold, italic, and underline styles.
 */
public class TerminalPanel extends JPanel {
    private final TerminalBuffer buffer;
    private Font baseFont;
    private Font boldFont;
    private Font italicFont;
    private Font boldItalicFont;
    private int charWidth;
    private int charHeight;
    private int charAscent;
    
    private boolean showCursor = true;
    private Timer cursorBlinkTimer;
    private boolean interactiveMode = false;

    /**
     * Creates a new TerminalPanel for the given buffer.
     * 
     * @param buffer the terminal buffer to display
     */
    public TerminalPanel(TerminalBuffer buffer) {
        this.buffer = buffer;
        
        // Use a monospace font for proper character alignment
        this.baseFont = new Font("Monospaced", Font.PLAIN, 14);
        this.boldFont = baseFont.deriveFont(Font.BOLD);
        this.italicFont = baseFont.deriveFont(Font.ITALIC);
        this.boldItalicFont = baseFont.deriveFont(Font.BOLD | Font.ITALIC);
        
        calculateFontMetrics();
        
        setBackground(ColorMapper.getDefaultBackground());
        setFocusable(true);
        
        // Add keyboard listener for interactive mode
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (interactiveMode) {
                    handleKeyTyped(e);
                }
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (interactiveMode) {
                    handleKeyPressed(e);
                }
            }
        });
        
        // Make cursor blink
        cursorBlinkTimer = new Timer(500, e -> {
            showCursor = !showCursor;
            repaint();
        });
        cursorBlinkTimer.start();
        
        // Update preferred size based on buffer dimensions
        updatePreferredSize();
    }

    /**
     * Calculate font metrics for character rendering.
     */
    private void calculateFontMetrics() {
        FontMetrics fm = getFontMetrics(baseFont);
        charWidth = fm.charWidth('W'); // Use 'W' for max width
        charHeight = fm.getHeight();
        charAscent = fm.getAscent();
    }

    /**
     * Updates the preferred size based on buffer dimensions.
     */
    private void updatePreferredSize() {
        int width = buffer.getWidth() * charWidth + 10; // +10 for padding
        int height = buffer.getHeight() * charHeight + 10;
        setPreferredSize(new Dimension(width, height));
        revalidate();
    }

    /**
     * Sets the font size for the terminal.
     */
    public void setFontSize(int size) {
        this.baseFont = new Font("Monospaced", Font.PLAIN, size);
        this.boldFont = baseFont.deriveFont(Font.BOLD);
        this.italicFont = baseFont.deriveFont(Font.ITALIC);
        this.boldItalicFont = baseFont.deriveFont(Font.BOLD | Font.ITALIC);
        calculateFontMetrics();
        updatePreferredSize();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable antialiasing for smoother text
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int xOffset = 5;
        int yOffset = 5;
        
        // Render each line of the buffer
        for (int row = 0; row < buffer.getHeight(); row++) {
            for (int col = 0; col < buffer.getWidth(); col++) {
                int x = xOffset + col * charWidth;
                int y = yOffset + row * charHeight;
                
                char ch = buffer.getCharAt(row, col);
                CellAttributes attrs = buffer.getAttributesAt(row, col);
                
                // Draw background
                java.awt.Color bgColor = getBackgroundColor(attrs);
                if (bgColor != null) {
                    g2d.setColor(bgColor);
                    g2d.fillRect(x, y, charWidth, charHeight);
                }
                
                // Draw character
                java.awt.Color fgColor = getForegroundColor(attrs);
                g2d.setColor(fgColor);
                
                // Select appropriate font based on style flags
                Font font = selectFont(attrs.getStyleFlags());
                g2d.setFont(font);
                
                // Draw the character
                g2d.drawString(String.valueOf(ch), x, y + charAscent);
                
                // Draw underline if needed
                if (attrs.getStyleFlags().isUnderline()) {
                    int underlineY = y + charAscent + 2;
                    g2d.drawLine(x, underlineY, x + charWidth, underlineY);
                }
            }
        }
        
        // Draw cursor
        if (showCursor) {
            drawCursor(g2d, xOffset, yOffset);
        }
    }

    /**
     * Draws the cursor at the current position.
     */
    private void drawCursor(Graphics2D g2d, int xOffset, int yOffset) {
        Cursor cursor = buffer.getCursor();
        int x = xOffset + cursor.getColumn() * charWidth;
        int y = yOffset + cursor.getRow() * charHeight;
        
        g2d.setColor(java.awt.Color.WHITE);
        g2d.fillRect(x, y, 2, charHeight); // Thin vertical line cursor
    }

    /**
     * Gets the foreground color for a cell.
     */
    private java.awt.Color getForegroundColor(CellAttributes attrs) {
        java.awt.Color color = ColorMapper.toAwtColor(attrs.getForeground());
        return color != null ? color : ColorMapper.getDefaultForeground();
    }

    /**
     * Gets the background color for a cell.
     */
    private java.awt.Color getBackgroundColor(CellAttributes attrs) {
        java.awt.Color color = ColorMapper.toAwtColor(attrs.getBackground());
        return color != null ? color : ColorMapper.getDefaultBackground();
    }

    /**
     * Selects the appropriate font based on style flags.
     */
    private Font selectFont(StyleFlags flags) {
        if (flags.isBold() && flags.isItalic()) {
            return boldItalicFont;
        } else if (flags.isBold()) {
            return boldFont;
        } else if (flags.isItalic()) {
            return italicFont;
        } else {
            return baseFont;
        }
    }

    /**
     * Stops the cursor blink timer.
     */
    public void stopCursorBlink() {
        if (cursorBlinkTimer != null) {
            cursorBlinkTimer.stop();
        }
    }

    /**
     * Gets the terminal buffer being displayed.
     */
    public TerminalBuffer getBuffer() {
        return buffer;
    }
    
    /**
     * Enables or disables interactive mode.
     * In interactive mode, keyboard input is captured and written to the buffer.
     */
    public void setInteractiveMode(boolean enabled) {
        this.interactiveMode = enabled;
        if (enabled) {
            requestFocusInWindow();
        }
    }
    
    /**
     * Returns whether interactive mode is enabled.
     */
    public boolean isInteractiveMode() {
        return interactiveMode;
    }
    
    /**
     * Handles regular character input.
     */
    private void handleKeyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        
        // Ignore control characters except specific ones
        if (Character.isISOControl(ch) && ch != '\n' && ch != '\t' && ch != '\b') {
            return;
        }
        
        // Handle backspace
        if (ch == '\b') {
            handleBackspace();
            return;
        }
        
        // Handle tab
        if (ch == '\t') {
            buffer.writeText("    "); // 4 spaces for tab
            repaint();
            return;
        }
        
        // Handle newline
        if (ch == '\n') {
            handleNewline();
            return;
        }
        
        // Write the character
        buffer.writeText(String.valueOf(ch));
        repaint();
    }
    
    /**
     * Handles special key presses (arrows, etc).
     */
    private void handleKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                buffer.moveCursorLeft(1);
                repaint();
                break;
                
            case KeyEvent.VK_RIGHT:
                buffer.moveCursorRight(1);
                repaint();
                break;
                
            case KeyEvent.VK_UP:
                buffer.moveCursorUp(1);
                repaint();
                break;
                
            case KeyEvent.VK_DOWN:
                buffer.moveCursorDown(1);
                repaint();
                break;
                
            case KeyEvent.VK_HOME:
                buffer.setCursorPosition(buffer.getCursor().getRow(), 0);
                repaint();
                break;
                
            case KeyEvent.VK_END:
                buffer.setCursorPosition(buffer.getCursor().getRow(), buffer.getWidth() - 1);
                repaint();
                break;
        }
    }
    
    /**
     * Handles backspace key.
     */
    private void handleBackspace() {
        Cursor cursor = buffer.getCursor();
        int col = cursor.getColumn();
        int row = cursor.getRow();
        
        if (col > 0) {
            // Move cursor left and write space
            buffer.moveCursorLeft(1);
            buffer.writeText(" ");
            buffer.moveCursorLeft(1);
        } else if (row > 0) {
            // Move to end of previous line
            buffer.setCursorPosition(row - 1, buffer.getWidth() - 1);
        }
        repaint();
    }
    
    /**
     * Handles newline/Enter key.
     */
    private void handleNewline() {
        Cursor cursor = buffer.getCursor();
        int row = cursor.getRow();
        
        // Move to next line, beginning of line
        if (row < buffer.getHeight() - 1) {
            buffer.setCursorPosition(row + 1, 0);
        } else {
            // At bottom, need to scroll
            buffer.insertEmptyLineAtBottom();
            buffer.setCursorPosition(buffer.getHeight() - 1, 0);
        }
        repaint();
    }
}
