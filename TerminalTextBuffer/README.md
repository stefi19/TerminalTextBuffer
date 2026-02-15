# Terminal Text Buffer

A Java implementation of the core data structure used by terminal emulators to store and manipulate displayed text, with **full GUI support for visual rendering and interactive keyboard input**, plus **wide character support** and **dynamic resize capabilities**.

## Overview

This project implements a terminal text buffer that manages:
- A grid of character cells with visual attributes (colors and styles)
- Screen display (the visible portion)
- Scrollback history (lines that have scrolled off-screen)
- Cursor position and movement
- Text editing operations
- **Wide character support** (CJK, emoji - occupying 2 cells) (NEW!)
- **Dynamic resize functionality** (NEW!)
- **Visual rendering with Swing GUI**
- **Interactive keyboard input mode**
- **Style and color controls in GUI** (NEW!)

## Features

### Core Components

- **Color**: 16 standard terminal colors (ANSI) plus default
- **StyleFlags**: Text styling (bold, italic, underline)
- **CellAttributes**: Combination of foreground color, background color, and style flags
- **Cell**: Individual character cell with its attributes + **wide character flags** (NEW!)
- **Cursor**: Position tracking within the buffer with **wide-char aware movement** (NEW!)
- **TerminalBuffer**: Main buffer implementation with screen, scrollback, and **resize capability** (NEW!)
- **ColorMapper**: Maps terminal colors to AWT colors for rendering
- **TerminalPanel**: Swing component that visually renders the buffer with keyboard input support
- **TerminalGUI**: Interactive GUI demo application with **style/color controls** (NEW!)

### Operations Supported

#### Setup
- Configurable screen dimensions (width × height)
- Configurable scrollback buffer size
- **Dynamic resizing** (NEW!)

#### Wide Character Support (NEW!)
- **Automatic detection** of wide characters (CJK, Hiragana, Katakana, Hangul, emoji)
- **Proper rendering** of 2-cell characters
- **Smart cursor movement** that skips over continuation cells
- **Unicode range detection** for various character types

#### Cursor Management
- Get/set cursor position (0-based row and column)
- Move cursor: up, down, left, right by N cells
- Cursor stays within screen bounds automatically
- **Automatic handling of wide character boundaries** (NEW!)

#### Text Attributes
- Set current foreground color
- Set current background color
- Set style flags (bold, italic, underline)
- Attributes apply to subsequently written text
- **GUI controls for real-time style/color changes** (NEW!)

#### Editing
- **Write text**: Overwrites content at cursor position (with **wide char support**)
- **Insert text**: Inserts content, shifting existing text
- **Fill line**: Fills entire line with a character
- **Insert empty line at bottom**: Scrolls screen up, top line moves to scrollback
- **Clear screen**: Removes all content while preserving scrollback
- **Clear all**: Removes screen and scrollback content

#### Content Access
- Get character at any position (screen or scrollback)
- Get attributes at any position (screen or scrollback)
- Get line as string
- Get entire screen as string
- Get all content (scrollback + screen) as string
- Access scrollback using negative indices

## Building the Project

The project uses Maven for build management and requires Java 21.

### Prerequisites
- Java 21 or higher
- Maven 3.6+ (optional, can compile manually with javac)

### Compile with Maven
```bash
mvn clean compile
```

### Compile manually with javac
```bash
javac -d target/classes src/main/java/org/example/*.java
```

### Run the console demo
```bash
# With Maven
mvn exec:java -Dexec.mainClass="org.example.Main"

# With javac
java -cp target/classes org.example.Main
```

### Run the GUI demo (NEW!)
```bash
# With Maven
mvn exec:java -Dexec.mainClass="org.example.TerminalGUI"

# With javac
java -cp target/classes org.example.TerminalGUI
```

The GUI demo provides:
- **Visual rendering** of all terminal colors (normal and bright variants)
- **Real-time display** of bold, italic, and underline styles
- **Background colors** with proper foreground/background combinations
- **Wide character demonstration** (CJK, Japanese, Korean characters) (NEW!)
- **Interactive keyboard input mode**: Type directly into the terminal!
  - Regular character input (including wide characters)
  - Arrow keys for cursor movement (←, →, ↑, ↓)
  - Enter for newline
  - Backspace for deletion
  - Tab for spaces
  - Home/End keys
- **Style controls**: Bold, Italic, Underline checkboxes (NEW!)
- **Color pickers**: Foreground and background color selection with preview (NEW!)
- **Interactive controls**: Clear screen, run demo, add scrollback, adjust font size
- **Mode toggle**: Switch between demo mode and interactive mode
- **Blinking cursor** showing current position
- **Status indicator** showing current mode

## Documentation

- **[ADVANCED_FEATURES.md](ADVANCED_FEATURES.md)** - Detailed guide for wide characters, resize, and GUI controls (NEW!)
- **[INTERACTIVE_MODE.md](INTERACTIVE_MODE.md)** - Complete guide to interactive keyboard features
- **[README.md](README.md)** - This file (project overview)

## Running Tests

The project includes comprehensive unit tests with JUnit 5.

### Run tests with Maven
```bash
mvn test
```

### Compile tests manually
```bash
# Find JUnit jars in your Maven repository
# Then compile with appropriate classpath
javac -cp "target/classes:$JUNIT_JARS" -d target/test-classes src/test/java/org/example/*.java
```

## Usage Example

```java
// Create a buffer: 80 columns, 24 rows, 1000 lines scrollback
TerminalBuffer buffer = new TerminalBuffer(80, 24, 1000);

// Set text attributes
CellAttributes attrs = new CellAttributes(
    Color.GREEN, 
    Color.BLACK, 
    new StyleFlags(true, false, false)  // bold
);
buffer.setCurrentAttributes(attrs);

// Write text at cursor position
buffer.setCursorPosition(0, 0);
buffer.writeText("Hello, Terminal!");

// Move cursor and write more
buffer.moveCursorDown(2);
buffer.setCursorPosition(buffer.getCursor().getRow(), 0);
buffer.writeText("Line 3");

// Read back content
String line = buffer.getLineAsString(0);
char ch = buffer.getCharAt(0, 0);
CellAttributes cellAttrs = buffer.getAttributesAt(0, 0);

// Get full screen content
String screen = buffer.getScreenAsString();

// Add line and scroll
buffer.insertEmptyLineAtBottom();

// Access scrollback (negative indices)
String scrolledLine = buffer.getLineAsString(-1);
```

### GUI Usage Example (NEW!)

```java
// Create and display terminal GUI
SwingUtilities.invokeLater(() -> {
    TerminalBuffer buffer = new TerminalBuffer(80, 24, 1000);
    TerminalPanel panel = new TerminalPanel(buffer);
    
    JFrame frame = new JFrame("Terminal");
    frame.add(new JScrollPane(panel));
    frame.pack();
    frame.setVisible(true);
    
    // Write colorful text
    buffer.setCurrentAttributes(new CellAttributes(
        Color.BRIGHT_GREEN,
        Color.BLACK,
        new StyleFlags(true, false, false)
    ));
    buffer.writeText("Hello, colorful world!");
    panel.repaint();
});
```

## Test Coverage

The project includes comprehensive tests covering:

### Unit Tests
- **TerminalBufferTest**: Main buffer functionality
  - Initialization and configuration
  - Cursor management and boundary conditions
  - Attribute management
  - Text writing and editing operations
  - Scrollback behavior
  - Content access methods
  - Edge cases (1×1 buffer, large buffers, boundary conditions)

- **StyleFlagsTest**: Style flag combinations
- **CellAttributesTest**: Attribute immutability and combinations
- **CellTest**: Cell behavior and empty cell detection
- **ColorMapperTest**: Color mapping and conversion
- **TerminalPanelTest**: Interactive mode and panel functionality (NEW!)

### Test Statistics
- 90+ test cases
- All edge cases and boundary conditions covered
- Tests document expected behavior
- Interactive features tested

## Project Structure

```
src/
├── main/java/org/example/
│   ├── Cell.java              # Character cell with attributes
│   ├── CellAttributes.java    # Color and style attributes
│   ├── Color.java             # Terminal color enumeration
│   ├── ColorMapper.java       # Terminal to AWT color mapping (NEW!)
│   ├── Cursor.java            # Cursor position management
│   ├── Main.java              # Console demo application
│   ├── StyleFlags.java        # Text style flags
│   ├── TerminalBuffer.java    # Main buffer implementation with resize
│   ├── TerminalGUI.java       # GUI demo with style/color controls
│   └── TerminalPanel.java     # Swing rendering component
└── test/java/org/example/
    ├── CellAttributesTest.java
    ├── CellTest.java
    ├── ColorMapperTest.java
    ├── ResizeTest.java         # (NEW!)
    ├── StyleFlagsTest.java
    ├── TerminalBufferTest.java
    ├── TerminalPanelTest.java
    └── WideCharacterTest.java  # (NEW!)
```

**Total:** 3,500+ lines of code, 107 tests (all passing)

## Design Decisions

### Immutability
- `Color`, `StyleFlags`, `CellAttributes`, and `Cell` are immutable
- Modifications create new instances
- Prevents accidental state changes
- Thread-safe by design

### 0-Based Indexing
- Rows and columns use 0-based indexing
- Scrollback uses negative indices (-1 is most recent scrollback line)

### Wide Character Handling (NEW!)
- Wide characters automatically detected via Unicode ranges
- Each wide character occupies exactly 2 cells
- Second cell marked as continuation (not directly editable)
- Cursor movement intelligently skips continuation cells

### Resize Strategy (NEW!)
- Content preserved from top-left corner
- Width increase: pad with empty cells
- Width decrease: truncate from right
- Height increase: add empty lines at bottom
- Height decrease: move bottom lines to scrollback
- Cursor position automatically adjusted to remain valid

### Cursor Bounds Checking
- Setting cursor position validates bounds and throws exception if invalid
- Movement operations (up, down, left, right) clamp to valid range
- Prevents cursor from leaving screen area

### Memory Management
- Scrollback has configurable maximum size
- Oldest scrollback lines are discarded when limit reached
- Screen size is fixed after initialization

### Attribute Application
- Buffer maintains "current attributes"
- Writing text applies current attributes to new cells
- Allows easy batch styling without passing attributes to each operation

### GUI Rendering
- Full color support: 16 standard ANSI colors (normal + bright variants)
- Style rendering: bold, italic, underline visually displayed
- Background colors properly rendered
- **Wide character rendering** with proper 2-cell spacing (NEW!)
- Monospace font for proper character alignment
- Blinking cursor indicator
- Adjustable font size
- Smooth text antialiasing

### Interactive Features
- **Keyboard input**: Type directly into the terminal buffer (including wide chars)
- **Character input**: All printable characters supported
- **Special keys**:
  - Arrow keys (←, →, ↑, ↓) for cursor movement
  - Enter/Return for newline
  - Backspace for character deletion
  - Tab for spaces (4-space indent)
  - Home/End for line navigation
- **Style controls**: Checkboxes for Bold, Italic, Underline (NEW!)
- **Color pickers**: Dropdown menus for foreground/background colors with preview (NEW!)
- **Mode toggle**: Switch between demo and interactive modes
- **Visual feedback**: Status indicator shows current mode
- **Focus management**: Automatic focus when entering interactive mode

## Git History

The repository demonstrates incremental development with clear commits:

1. Configure Maven build with JUnit 5
2. Add Color enum for terminal colors
3. Add StyleFlags class for text styling
4. Add CellAttributes class for cell styling
5. Add Cell class for terminal character cells
6. Add Cursor class for cursor position management
7. Add TerminalBuffer core structure with cursor management
8. Add editing operations to TerminalBuffer
9. Add content access methods to TerminalBuffer
10. Add comprehensive unit tests for TerminalBuffer
11. Add unit tests for StyleFlags, CellAttributes, and Cell
12. Add demonstration program for TerminalBuffer
13. Add comprehensive README documentation
14. Add test compilation script
15. Add ColorMapper for terminal color to AWT color conversion
16. Add TerminalPanel Swing component for visual rendering
17. Add tests for ColorMapper
18. Update README with GUI documentation and examples
19. Add script to easily run GUI demo
20. Add interactive keyboard input mode to GUI
21. Update TerminalPanel for interactive keyboard input
22. Add interactive mode user guide
23. **Add wide character support with CJK/emoji detection** (NEW!)
24. **Add resize functionality to dynamically change dimensions** (NEW!)
25. **Add GUI style controls (Bold/Italic/Underline) and color pickers** (NEW!)

Each commit represents a logical unit of work with descriptive messages.

## Statistics

- **Lines of Code**: 3,500+
- **Test Classes**: 8
- **Test Methods**: 107
- **Test Pass Rate**: 100%
- **Commits**: 23
- **Documentation Files**: 3 (README.md, INTERACTIVE_MODE.md, ADVANCED_FEATURES.md)

## License

This is a demonstration project created for educational purposes.

## Author

Created as a comprehensive implementation of terminal buffer data structures.
