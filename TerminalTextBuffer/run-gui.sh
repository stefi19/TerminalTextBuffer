#!/bin/bash

# Run the Terminal GUI Demo

PROJECT_DIR="/Users/stefi/Desktop/Projects/TerminalTextBuffer/TerminalTextBuffer"
cd "$PROJECT_DIR"

# Check if classes are compiled
if [ ! -d "target/classes/org/example" ]; then
    echo "Compiling sources..."
    javac -d target/classes src/main/java/org/example/*.java
fi

echo "Starting Terminal GUI Demo..."
echo "Features:"
echo "  - Visual rendering with colors (16 ANSI colors)"
echo "  - Bold, italic, underline styles"
echo "  - Background colors"
echo "  - Interactive controls"
echo "  - Adjustable font size"
echo "  - Blinking cursor"
echo ""

java -cp target/classes org.example.TerminalGUI
