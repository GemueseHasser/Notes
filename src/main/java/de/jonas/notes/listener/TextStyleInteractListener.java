package de.jonas.notes.listener;

import de.jonas.notes.constant.TextStyleType;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.TextStyleInformation;
import de.jonas.notes.object.component.RoundToggleButton;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.swing.JTextPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

@RequiredArgsConstructor
public final class TextStyleInteractListener implements ActionListener {

    @NotNull
    private final Note note;
    @NotNull
    private final TextStyleType styleType;
    @NotNull
    private final RoundToggleButton toggleButton;
    @NotNull
    private final JTextPane textPane;


    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        final TextStyleInformation textStyleInformation = note.getTextStyleInformation();

        // check if user is focusing the text pane
        if (!textPane.isFocusOwner()) {
            if (!toggleButton.isSelected()) {
                textStyleInformation.getStyles().get(styleType).getLast().setEndPosition(textPane.getText().length());
                return;
            }

            toggleButton.setSelected(false);
            return;
        }

        // check if button is selected
        if (toggleButton.isSelected()) {
            // check if style already exists
            if (!textStyleInformation.getStyles().containsKey(styleType)) {
                textStyleInformation.getStyles().put(styleType, new LinkedList<>());
            }

            // create new style information
            textStyleInformation.getStyles().get(styleType).addLast(new TextStyleInformation.StyleInformation());

            // check if user selects text
            if (textPane.getSelectionStart() != textPane.getSelectionEnd()) {
                textStyleInformation.getStyles().get(styleType).getLast().setStartPosition(textPane.getSelectionStart());
                textStyleInformation.getStyles().get(styleType).getLast().setEndPosition(textPane.getSelectionEnd());
                toggleButton.setSelected(false);
                return;
            }

            // set start position
            textStyleInformation.getStyles().get(styleType).getLast().setStartPosition(textPane.getCaretPosition());
            return;
        }

        // set end position
        textStyleInformation.getStyles().get(styleType).getLast().setEndPosition(textPane.getCaretPosition());
    }
}
