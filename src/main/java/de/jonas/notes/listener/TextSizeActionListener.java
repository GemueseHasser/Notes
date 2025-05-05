package de.jonas.notes.listener;

import de.jonas.notes.object.component.RoundToggleButton;
import de.jonas.notes.object.gui.NoteGui;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Durch diesen Listener wird die Aktion implementiert, die ausgeführt wird, sobald der Nutzer einen Button anklickt,
 * mit dem man die Text-Größe innerhalb einer Notiz verändern kann (h1-h6).
 */
@RequiredArgsConstructor
public final class TextSizeActionListener implements ActionListener {

    //<editor-fold desc="LOCAL FIELDS">
    /** Das Fenster, in dem sich der Button befindet, der angeklickt wurde. */
    @NotNull
    private final NoteGui noteGui;
    /** Der Button, der angeklickt wurde, um die Text-Größe zu verändern. */
    @NotNull
    private final RoundToggleButton toggleButton;
    //</editor-fold>


    //<editor-fold desc="implementation">
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
    //</editor-fold>
}
