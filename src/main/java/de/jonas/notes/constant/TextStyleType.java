package de.jonas.notes.constant;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

@Getter
public enum TextStyleType {

    BOLD(new StyledEditorKit.BoldAction(), "<html><b>F"),
    ITALIC(new StyledEditorKit.ItalicAction(), "<html><i>K"),
    UNDERLINE(new StyledEditorKit.UnderlineAction(), "<html><u>U"),
    H1(new StyledEditorKit.FontSizeAction("h1", 34), "h1"),
    H2(new StyledEditorKit.FontSizeAction("h2", 30), "h2"),
    H3(new StyledEditorKit.FontSizeAction("h3", 24), "h3"),
    H4(new StyledEditorKit.FontSizeAction("h4", 20), "h4"),
    H5(new StyledEditorKit.FontSizeAction("h5", 18), "h5"),
    H6(new StyledEditorKit.FontSizeAction("h6", 16), "h6");


    @NotNull
    private final StyledEditorKit.StyledTextAction styledTextAction;
    @NotNull
    private final String text;


    TextStyleType(
        @NotNull final StyledEditorKit.StyledTextAction styledTextAction,
        @NotNull final String text
    ) {
        this.styledTextAction = styledTextAction;
        this.text = text;
    }


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
