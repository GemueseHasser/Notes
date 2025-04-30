package de.jonas.notes.listener;

import de.jonas.notes.object.Notebook;
import de.jonas.notes.object.gui.NoteOverviewGui;
import de.jonas.notes.object.gui.NotebookOverviewGui;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@RequiredArgsConstructor
public final class NotebookClickListener implements ActionListener {

    @NotNull
    private final Notebook notebook;
    @NotNull
    private final NotebookOverviewGui parentOverviewGui;


    @Override
    public void actionPerformed(ActionEvent e) {
        final NoteOverviewGui noteOverviewGui = new NoteOverviewGui(notebook);
        noteOverviewGui.open();
        parentOverviewGui.dispose();
    }
}
