package org.example;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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
}
