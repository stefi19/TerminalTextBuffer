package org.example.terminal.model;

import java.util.Objects;

/**
 * Represents the visual attributes of a terminal cell.
 * Includes foreground color, background color, and style flags.
 * This class is immutable.
 */
public class CellAttributes {
    
    private final Color foreground;
    private final Color background;
    private final StyleFlags styleFlags;

    /**
     * Default cell attributes with default colors and no styling.
     */
    public static final CellAttributes DEFAULT = new CellAttributes(
            Color.DEFAULT,
            Color.DEFAULT,
            StyleFlags.DEFAULT
    );

    public CellAttributes(Color foreground, Color background, StyleFlags styleFlags) {
        this.foreground = foreground != null ? foreground : Color.DEFAULT;
        this.background = background != null ? background : Color.DEFAULT;
        this.styleFlags = styleFlags != null ? styleFlags : StyleFlags.DEFAULT;
    }

    public Color getForeground() {
        return foreground;
    }

    public Color getBackground() {
        return background;
    }

    public StyleFlags getStyleFlags() {
        return styleFlags;
    }

    public CellAttributes withForeground(Color foreground) {
        return new CellAttributes(foreground, this.background, this.styleFlags);
    }

    public CellAttributes withBackground(Color background) {
        return new CellAttributes(this.foreground, background, this.styleFlags);
    }

    public CellAttributes withStyleFlags(StyleFlags styleFlags) {
        return new CellAttributes(this.foreground, this.background, styleFlags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellAttributes that = (CellAttributes) o;
        return foreground == that.foreground &&
                background == that.background &&
                Objects.equals(styleFlags, that.styleFlags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foreground, background, styleFlags);
    }

    @Override
    public String toString() {
        return "CellAttributes{" +
                "foreground=" + foreground +
                ", background=" + background +
                ", styleFlags=" + styleFlags +
                '}';
    }
}
