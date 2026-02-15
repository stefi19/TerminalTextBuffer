#!/bin/bash

# Compile and run tests manually without Maven

PROJECT_DIR="/Users/stefi/Desktop/Projects/TerminalTextBuffer/TerminalTextBuffer"
SRC_DIR="$PROJECT_DIR/src/main/java"
TEST_DIR="$PROJECT_DIR/src/test/java"
TARGET_DIR="$PROJECT_DIR/target"
CLASSES_DIR="$TARGET_DIR/classes"
TEST_CLASSES_DIR="$TARGET_DIR/test-classes"

# Find JUnit jars
JUNIT_API=$(find ~/.m2/repository/org/junit/jupiter/junit-jupiter-api -name "*.jar" | grep "5.10.1" | head -1)
JUNIT_ENGINE=$(find ~/.m2/repository/org/junit/jupiter/junit-jupiter-engine -name "*.jar" | grep "5.10.1" | head -1)
JUNIT_PLATFORM_ENGINE=$(find ~/.m2/repository/org/junit/platform/junit-platform-engine -name "*.jar" | head -1)
JUNIT_PLATFORM_COMMONS=$(find ~/.m2/repository/org/junit/platform/junit-platform-commons -name "*.jar" | head -1)
OPENTEST4J=$(find ~/.m2/repository/org/opentest4j/opentest4j -name "*.jar" | head -1)
APIGUARDIAN=$(find ~/.m2/repository/org/apiguardian/apiguardian-api -name "*.jar" | head -1)

CLASSPATH="$JUNIT_API:$JUNIT_ENGINE:$JUNIT_PLATFORM_ENGINE:$JUNIT_PLATFORM_COMMONS:$OPENTEST4J:$APIGUARDIAN:$CLASSES_DIR:$TEST_CLASSES_DIR"

# Create target directories
mkdir -p "$CLASSES_DIR"
mkdir -p "$TEST_CLASSES_DIR"

# Compile main sources
echo "Compiling main sources..."
javac -d "$CLASSES_DIR" $(find "$SRC_DIR" -name "*.java")

# Compile test sources
echo "Compiling test sources..."
javac -cp "$CLASSPATH" -d "$TEST_CLASSES_DIR" $(find "$TEST_DIR" -name "*.java")

echo "Compilation complete!"
echo "Note: To run tests, you would need the JUnit Console Launcher."
echo "Tests are compiled and ready in: $TEST_CLASSES_DIR"
