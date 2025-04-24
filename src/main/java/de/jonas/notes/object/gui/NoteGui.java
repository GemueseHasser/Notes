package de.jonas.notes.object.gui;

import de.jonas.notes.Notes;
import de.jonas.notes.constant.ImageType;
import de.jonas.notes.handler.NotesHandler;
import de.jonas.notes.object.Gui;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.component.RoundButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public final class NoteGui extends Gui {

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 12);


    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;


    private final JEditorPane editorPane;


    public NoteGui(@NotNull final Note note) {
        super(note.getTitle(), WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(5, 5));

        final JTextField titleField = new JTextField(note.getTitle());
        titleField.setFont(TITLE_FONT);
        titleField.setBorder(null);
        titleField.setPreferredSize(new Dimension(WIDTH, 50));

        final StringBuilder initialText = new StringBuilder();

        for (@NotNull final String line : note.getLines()) {
            initialText.append(line);
        }

        editorPane = new JEditorPane("text/html", initialText.toString());
        editorPane.setContentType("text/html");
        editorPane.setFont(TEXT_FONT);
        editorPane.setBorder(null);
        editorPane.setPreferredSize(new Dimension(WIDTH, HEIGHT - 50));
        editorPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent e) {
                super.focusLost(e);
                repaint();
            }
        });

        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        final RoundButton saveButton = new RoundButton("Speichern", 10);
        saveButton.addActionListener(e -> {
            final Note newNote = new Note(
                titleField.getText(),
                LocalDateTime.now(),
                new ArrayList<>(Collections.singleton(editorPane.getText()))
            );

            NotesHandler.saveNote(newNote);
            NotesHandler.deleteNote(note);
            Notes.getOverviewGui().reloadNotes();

            this.dispose();
        });

        final RoundButton deleteButton = new RoundButton("Löschen", 10);
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

        this.add(editorPane, BorderLayout.CENTER);
        this.add(titleField, BorderLayout.NORTH);
        this.add(panel, BorderLayout.SOUTH);
        this.pack();
    }
}
