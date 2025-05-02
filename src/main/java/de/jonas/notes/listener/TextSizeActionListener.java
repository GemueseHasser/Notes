package de.jonas.notes.listener;

import de.jonas.notes.object.component.RoundToggleButton;
import de.jonas.notes.object.gui.NoteGui;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@RequiredArgsConstructor
public final class TextSizeActionListener implements ActionListener {

    @NotNull
    private final NoteGui noteGui;
    @NotNull
    private final RoundToggleButton toggleButton;


    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        if (!toggleButton.isSelected()) return;

        // unselect all h1-h6 buttons
        for (@NotNull final RoundToggleButton sizeStyleButton : noteGui.getStyleButtons()) {
            if (!sizeStyleButton.isSelected()) continue;
            if (!sizeStyleButton.getText().matches("^h[1-6]$")) continue;
            if (sizeStyleButton.equals(toggleButton)) continue;

            sizeStyleButton.doClick();
        }

        // select current button
        toggleButton.setSelected(true);
    }
}
