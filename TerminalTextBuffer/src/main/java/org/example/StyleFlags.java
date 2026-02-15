package org.example;

import java.util.Objects;

/**
 * Represents style attributes for a terminal cell (bold, italic, underline).
 * This class is immutable and uses a builder pattern for creating variations.
 */
public class StyleFlags {
    private final boolean bold;
    private final boolean italic;
    private final boolean underline;

    /**
     * Default style with no flags set.
     */
    public static final StyleFlags DEFAULT = new StyleFlags(false, false, false);

    public StyleFlags(boolean bold, boolean italic, boolean underline) {
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public StyleFlags withBold(boolean bold) {
        return new StyleFlags(bold, this.italic, this.underline);
    }

    public StyleFlags withItalic(boolean italic) {
        return new StyleFlags(this.bold, italic, this.underline);
    }

    public StyleFlags withUnderline(boolean underline) {
        return new StyleFlags(this.bold, this.italic, underline);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StyleFlags that = (StyleFlags) o;
        return bold == that.bold && italic == that.italic && underline == that.underline;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bold, italic, underline);
    }

    @Override
    public String toString() {
        return "StyleFlags{" +
                "bold=" + bold +
                ", italic=" + italic +
                ", underline=" + underline +
                '}';
    }
}
