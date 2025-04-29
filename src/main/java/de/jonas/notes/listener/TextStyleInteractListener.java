package de.jonas.notes.listener;

import de.jonas.notes.constant.TextStyleType;
import de.jonas.notes.object.TextStyleInformation;
import de.jonas.notes.object.component.RoundToggleButton;
import de.jonas.notes.object.gui.NoteGui;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.swing.JTextPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

@RequiredArgsConstructor
public final class TextStyleInteractListener implements ActionListener {

    @NotNull
    private final NoteGui noteGui;
    @NotNull
    private final TextStyleType styleType;
    @NotNull
    private final RoundToggleButton toggleButton;


    public void interactAction() {
        final JTextPane textPane = noteGui.getTextPane();
        final TextStyleInformation textStyleInformation = noteGui.getNote().getTextStyleInformation();

        // check if user is focusing the text pane
        if (!textPane.isFocusOwner()) {
            if (!toggleButton.isSelected()) {
                textStyleInformation.getStyles().get(styleType).getLast().setEndPosition(textPane.getText().length());
                return;
            }

            toggleButton.setSelected(false);
            return;
        }

        // check if style type is h1-h6
        if (styleType.name().matches("^H[1-6]$") && toggleButton.isSelected()) {
            // unselect all h1-h6 buttons
            for (@NotNull final RoundToggleButton sizeStyleButton : noteGui.getStyleButtons()) {
                if (!sizeStyleButton.isSelected()) continue;
                if (!sizeStyleButton.getText().matches("^h[1-6]$")) continue;
                if (sizeStyleButton.equals(toggleButton)) continue;

                sizeStyleButton.setSelected(false);
                final LinkedList<TextStyleInformation.StyleInformation> styleInformations = textStyleInformation
                    .getStyles()
                    .get(TextStyleType.valueOf(sizeStyleButton.getText().toUpperCase()));

                if (styleInformations != null) {
                    styleInformations.getLast().setEndPosition(textPane.getCaretPosition());
                }
            }

            // select current button
            toggleButton.setSelected(true);
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

        final LinkedList<TextStyleInformation.StyleInformation> styleInformations = textStyleInformation
            .getStyles()
            .get(styleType);
        if (styleInformations == null) return;

        // set end position
        textStyleInformation.getStyles().get(styleType).getLast().setEndPosition(textPane.getCaretPosition());
    }


    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        interactAction();
    }
}
