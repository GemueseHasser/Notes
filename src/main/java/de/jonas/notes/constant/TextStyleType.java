package de.jonas.notes.constant;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

/**
 * Ein {@link TextStyleType} stellt einen Typen einer Text-Formatierung dar, welche man im
 * {@link de.jonas.notes.object.gui.NoteGui} anwenden kann. Für jeden Typen wird automatisch ein Button erzeugt. Ein Typ
 * wird auf der Grundlage einer {@link javax.swing.text.StyledEditorKit.StyledTextAction} und des Textes, welche auf dem
 * Button angezeigt werden soll, erzeugt.
 */
@Getter
public enum TextStyleType {

    //<editor-fold desc="VALUES">
    /** Der Typ, um Text Fett darzustellen. */
    BOLD(new StyledEditorKit.BoldAction(), "<html><b>F"),
    /** Der Typ, um Text kursiv darzustellen. */
    ITALIC(new StyledEditorKit.ItalicAction(), "<html><i>K"),
    /** Der Typ, um Text unterstrichen darzustellen. */
    UNDERLINE(new StyledEditorKit.UnderlineAction(), "<html><u>U"),
    /** Der Typ, um die Schriftgröße h1 darzustellen (34px). */
    H1(new StyledEditorKit.FontSizeAction("h1", 34), "h1"),
    /** Der Typ, um die Schriftgröße h2 darzustellen (30px). */
    H2(new StyledEditorKit.FontSizeAction("h2", 30), "h2"),
    /** Der Typ, um die Schriftgröße h3 darzustellen (24px). */
    H3(new StyledEditorKit.FontSizeAction("h3", 24), "h3"),
    /** Der Typ, um die Schriftgröße h4 darzustellen (20px). */
    H4(new StyledEditorKit.FontSizeAction("h4", 20), "h4"),
    /** Der Typ, um die Schriftgröße h5 darzustellen (18px). */
    H5(new StyledEditorKit.FontSizeAction("h5", 18), "h5"),
    /** Der Typ, um die Schriftgröße h6 darzustellen (16px). */
    H6(new StyledEditorKit.FontSizeAction("h6", 16), "h6");
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Die Formatierung, die durch diesen Typen vorgenommen werden soll. */
    @NotNull
    private final StyledEditorKit.StyledTextAction styledTextAction;
    /** Der Text, der auf dem Button, der für diesen Typen erzeugt wird, angezeigt werden soll. */
    @NotNull
    private final String text;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt einen neuen {@link TextStyleType Typen}. Ein {@link TextStyleType} stellt eine Text-Formatierung dar,
     * welche man im {@link de.jonas.notes.object.gui.NoteGui} anwenden kann. Für jeden Typen wird automatisch ein
     * Button erzeugt. Ein Typ wird auf der Grundlage einer {@link javax.swing.text.StyledEditorKit.StyledTextAction}
     * und des Textes, welche auf dem Button angezeigt werden soll, erzeugt.
     *
     * @param styledTextAction Die Formatierung, die durch diesen Typen vorgenommen werden soll.
     * @param text             Der Text, der auf dem Button, der für diesen Typen erzeugt wird, angezeigt werden soll.
     */
    TextStyleType(
        @NotNull final StyledEditorKit.StyledTextAction styledTextAction,
        @NotNull final String text
    ) {
        this.styledTextAction = styledTextAction;
        this.text = text;
    }
    //</editor-fold>


    /**
     * Erweitert einen bestimmten {@link Style} um die Formatierung, die durch diesen Typen vorgenommen werden soll.
     *
     * @param style Der Style, welcher die Formatierung dieses Typen gesetzt bekommen soll.
     */
    public void expandStyle(@NotNull final Style style) {
        switch (this) {
            case BOLD:
                StyleConstants.setBold(style, true);
                break;

            case ITALIC:
                StyleConstants.setItalic(style, true);
                break;

            case UNDERLINE:
                StyleConstants.setUnderline(style, true);
                break;

            case H1:
                StyleConstants.setFontSize(style, 34);
                break;

            case H2:
                StyleConstants.setFontSize(style, 30);
                break;

            case H3:
                StyleConstants.setFontSize(style, 24);
                break;

            case H4:
                StyleConstants.setFontSize(style, 20);
                break;

            case H5:
                StyleConstants.setFontSize(style, 18);
                break;

            case H6:
                StyleConstants.setFontSize(style, 16);
                break;
        }
    }

    /**
     * Prüft, ob eine bestimmte Ansammlung an {@link AttributeSet Text-Eigenschaften} die Formatierung dieses Typen
     * beinhaltet.
     *
     * @param attributes Die Ansammlung, an Text-Eigenschaften, die überprüft werden soll.
     *
     * @return Wenn die Ansammlung der Eigenschaften die Formatierung dieses Typen beinhaltet {@code true}, ansonsten
     *     {@code false}.
     */
    public boolean attributesContains(@NotNull final AttributeSet attributes) {
        switch (this) {
            case BOLD:
                return StyleConstants.isBold(attributes);

            case ITALIC:
                return StyleConstants.isItalic(attributes);

            case UNDERLINE:
                return StyleConstants.isUnderline(attributes);

            case H1:
                return StyleConstants.getFontSize(attributes) == 34;

            case H2:
                return StyleConstants.getFontSize(attributes) == 30;

            case H3:
                return StyleConstants.getFontSize(attributes) == 24;

            case H4:
                return StyleConstants.getFontSize(attributes) == 20;

            case H5:
                return StyleConstants.getFontSize(attributes) == 18;

            case H6:
                return StyleConstants.getFontSize(attributes) == 16;
        }

        return false;
    }
}
