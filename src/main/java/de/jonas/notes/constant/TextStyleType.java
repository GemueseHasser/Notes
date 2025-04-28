package de.jonas.notes.constant;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

@Getter
public enum TextStyleType {

    BOLD(new StyledEditorKit.BoldAction(), "<html><b>F"),
    ITALIC(new StyledEditorKit.ItalicAction(), "<html><i>K"),
    UNDERLINE(new StyledEditorKit.UnderlineAction(), "<html><u>U");


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
        }
    }
}
