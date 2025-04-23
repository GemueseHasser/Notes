package de.jonas.notes.object.gui;

import de.jonas.notes.object.Gui;
import de.jonas.notes.object.Note;
import org.jetbrains.annotations.NotNull;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;

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

        this.add(textArea, BorderLayout.CENTER);
        this.add(titleField, BorderLayout.NORTH);
        this.pack();
    }

}
