package de.jonas.notes.listener;

import de.jonas.notes.object.Notebook;
import de.jonas.notes.object.gui.NoteOverviewGui;
import de.jonas.notes.object.gui.NotebookOverviewGui;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Durch diesen Listener wird die Aktion implementiert, die ausgeführt wird, sobald der Nutzer auf ein Notizbuch klickt.
 */
@RequiredArgsConstructor
public final class NotebookClickListener implements ActionListener {

    //<editor-fold desc="LOCAL FIELDS">
    /** Das Notizbuch, welches angeklickt wurde. */
    @NotNull
    private final Notebook notebook;
    /** Das Fenster, in dem sich das Notizbuch befindet, welches angeklickt wurde. */
    @NotNull
    private final NotebookOverviewGui parentOverviewGui;
    //</editor-fold>


    //<editor-fold desc="implementation">
    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        final NoteOverviewGui noteOverviewGui = new NoteOverviewGui(notebook);
        noteOverviewGui.open();
        parentOverviewGui.dispose();
    }
    //</editor-fold>
}
