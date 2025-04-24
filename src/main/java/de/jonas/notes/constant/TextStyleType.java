package de.jonas.notes.constant;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.StyledEditorKit;

@Getter
public enum TextStyleType {

    BOLD(new StyledEditorKit.BoldAction());


    @NotNull
    private final StyledEditorKit.StyledTextAction styledTextAction;


    TextStyleType(@NotNull final StyledEditorKit.StyledTextAction styledTextAction) {
        this.styledTextAction = styledTextAction;
    }
}
