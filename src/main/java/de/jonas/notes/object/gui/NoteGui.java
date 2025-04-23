package de.jonas.notes.object.gui;

import de.jonas.notes.Notes;
import de.jonas.notes.constant.ImageType;
import de.jonas.notes.handler.NotesHandler;
import de.jonas.notes.object.Gui;
import de.jonas.notes.object.Note;
import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public final class NoteGui extends Gui {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;


    public NoteGui(@NotNull final Note note) {
        super(note.getTitle(), WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(5, 5));

        final JTextField titleField = new JTextField(note.getTitle());
        titleField.setPreferredSize(new Dimension(WIDTH, 30));

        final JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(WIDTH, HEIGHT - 30));
        for (@NotNull final String line : note.getLines()) {
            textArea.append(line + "\n");
        }

        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        final JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(e -> {
            final Note newNote = new Note(
                titleField.getText(),
                LocalDateTime.now(),
                new ArrayList<>(Arrays.asList(textArea.getText().split("\n")))
            );

            NotesHandler.saveNote(newNote);
            NotesHandler.deleteNote(note);
            Notes.getOverviewGui().reloadNotes();

            this.dispose();
        });

        final JButton deleteButton = new JButton("Löschen");
        deleteButton.addActionListener(e -> {
            final int delete = JOptionPane.showConfirmDialog(
                null,
                "Möchtest du diese Notiz wirklich löschen?",
                "Notiz löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(ImageType.DELETE_NOTE_ICON.getImage())
            );

            if (delete == JOptionPane.NO_OPTION) return;

            if (NotesHandler.deleteNote(note)) {
                Notes.getOverviewGui().reloadNotes();
            }

            this.dispose();
        });
        panel.add(saveButton);
        panel.add(deleteButton);

        this.add(textArea, BorderLayout.CENTER);
        this.add(titleField, BorderLayout.NORTH);
        this.add(panel, BorderLayout.SOUTH);
        this.pack();
    }

}
