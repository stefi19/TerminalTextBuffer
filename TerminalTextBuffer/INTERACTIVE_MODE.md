# Interactive Mode Guide

## How to Use Interactive Mode

### Starting Interactive Mode

1. Launch the GUI application:
   ```bash
   ./run-gui.sh
   # or
   java -cp target/classes org.example.TerminalGUI
   ```

2. Click the **"Enable Interactive Mode"** button at the bottom of the window

3. The screen will clear and show instructions

4. Start typing!

### Keyboard Controls

| Key | Action |
|-----|--------|
| **Letters, numbers, symbols** | Type characters at cursor position |
| **Enter** | New line (move cursor to next line) |
| **Backspace** | Delete character before cursor |
| **Tab** | Insert 4 spaces |
| **Arrow Keys** | Move cursor (←, →, ↑, ↓) |
| **Home** | Move cursor to beginning of line |
| **End** | Move cursor to end of line |

### Features

- ✅ **Real-time rendering**: See what you type immediately
- ✅ **Cursor movement**: Navigate anywhere on the screen
- ✅ **Line wrapping**: Cursor moves to next line when needed
- ✅ **Scrollback**: Old lines scroll up when screen is full
- ✅ **Visual feedback**: Blinking cursor shows your position

### Tips

1. **Focus**: Make sure the terminal panel has focus (click on it if needed)
2. **Mode indicator**: Check the status label - it shows "Interactive Mode - Type to test!"
3. **Switching modes**: Click "Disable Interactive Mode" to return to demo mode
4. **Testing**: Try all the special keys to see how the terminal responds!

### Example Session

```
=== INTERACTIVE MODE ===

Start typing! Try:
  - Regular text
  - Arrow keys to move cursor
  - Enter for new line
  - Backspace to delete
  - Tab for spaces

Type below this line:
Hello, Terminal!
This is interactive mode!
[Your cursor is here blinking]
```

### What You Can Test

1. **Basic typing**: Type any text you want
2. **Cursor navigation**: Use arrows to move around and edit
3. **Line editing**: Use backspace to fix mistakes
4. **Multiline text**: Press Enter to create new lines
5. **Tab stops**: Use Tab for indentation
6. **Buffer limits**: See what happens when you reach the edge

### Differences from Real Terminal

This is a **terminal buffer demonstration**, not a full terminal emulator:
- ❌ No command execution
- ❌ No shell integration
- ❌ No ANSI escape sequences processing
- ✅ Pure text buffer operations
- ✅ Visual rendering with colors
- ✅ Cursor management
- ✅ All the core terminal buffer features!

### Use Cases

- **Testing** the terminal buffer implementation
- **Demonstrating** how terminals store text
- **Learning** about terminal data structures
- **Experimenting** with cursor movement and text editing
- **Visual validation** of buffer operations

Enjoy testing the terminal buffer! 🚀
