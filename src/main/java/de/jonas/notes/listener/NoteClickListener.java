package de.jonas.notes.listener;

import de.jonas.notes.object.Note;
import de.jonas.notes.object.gui.NoteGui;
import de.jonas.notes.object.gui.OverviewGui;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.BadLocationException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@RequiredArgsConstructor
public final class NoteClickListener implements ActionListener {

    @NotNull
    private final Note note;
    @NotNull
    private final OverviewGui overviewGui;


    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        try {
            new NoteGui(overviewGui, note).open();
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
