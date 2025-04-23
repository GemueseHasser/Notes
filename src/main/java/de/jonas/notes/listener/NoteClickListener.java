package de.jonas.notes.listener;

import de.jonas.notes.object.Note;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@RequiredArgsConstructor
public final class NoteClickListener implements ActionListener {

    @NotNull
    private final Note note;


    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {

    }
}
