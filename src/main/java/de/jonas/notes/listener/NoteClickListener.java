package de.jonas.notes.listener;

import de.jonas.notes.object.Note;
import de.jonas.notes.object.gui.NoteGui;
import de.jonas.notes.object.gui.OverviewGui;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Durch diesen Listener wird die Aktion implementiert, die ausgeführt wird, sobald der Nutzer auf eine Notiz klickt.
 */
@RequiredArgsConstructor
public final class NoteClickListener implements ActionListener {

    //<editor-fold desc="LOCAL FIELDS">
    /** Die Notiz, die angeklickt wurde. */
    @NotNull
    private final Note note;
    /** Das Fenster, in dem sich dei Notiz befindet, die angeklickt wurde. */
    @NotNull
    private final OverviewGui overviewGui;
    //</editor-fold>


    //<editor-fold desc="implementation">
    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        new NoteGui(overviewGui, note).open();
    }
    //</editor-fold>
}
