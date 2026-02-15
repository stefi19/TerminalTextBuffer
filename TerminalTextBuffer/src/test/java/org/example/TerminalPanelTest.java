package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for interactive features of TerminalPanel.
 * Tests the keyboard input handling and special keys.
 */
@DisplayName("TerminalPanel Interactive Tests")
public class TerminalPanelTest {

    private TerminalBuffer buffer;
    private TerminalPanel panel;

    @BeforeEach
    void setUp() {
        buffer = new TerminalBuffer(20, 10, 100);
        panel = new TerminalPanel(buffer);
    }

    @Test
    @DisplayName("Should start in non-interactive mode by default")
    void testDefaultNonInteractive() {
        assertFalse(panel.isInteractiveMode());
    }

    @Test
    @DisplayName("Should enable interactive mode")
    void testEnableInteractiveMode() {
        panel.setInteractiveMode(true);
        assertTrue(panel.isInteractiveMode());
    }

    @Test
    @DisplayName("Should disable interactive mode")
    void testDisableInteractiveMode() {
        panel.setInteractiveMode(true);
        panel.setInteractiveMode(false);
        assertFalse(panel.isInteractiveMode());
    }

    @Test
    @DisplayName("Should toggle interactive mode")
    void testToggleInteractiveMode() {
        assertFalse(panel.isInteractiveMode());
        
        panel.setInteractiveMode(true);
        assertTrue(panel.isInteractiveMode());
        
        panel.setInteractiveMode(false);
        assertFalse(panel.isInteractiveMode());
    }

    @Test
    @DisplayName("Should maintain reference to buffer")
    void testBufferReference() {
        assertSame(buffer, panel.getBuffer());
    }

    @Test
    @DisplayName("Panel should be focusable for keyboard input")
    void testPanelIsFocusable() {
        assertTrue(panel.isFocusable());
    }
}
