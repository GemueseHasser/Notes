package de.jonas.notes.object.gui;

import de.jonas.notes.Notes;
import de.jonas.notes.constant.ImageType;
import de.jonas.notes.constant.TextStyleType;
import de.jonas.notes.handler.NotesHandler;
import de.jonas.notes.handler.TextStyleHandler;
import de.jonas.notes.object.Drawable;
import de.jonas.notes.object.Gui;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.TextStyleInformation;
import de.jonas.notes.object.component.RoundButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public final class NoteGui extends Gui implements Drawable {

    @NotNull
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    @NotNull
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 12);


    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;


    @NotNull
    private final JTextPane textPane = new JTextPane();


    public NoteGui(@NotNull final Note note) throws BadLocationException {
        super(note.getTitle(), WIDTH, HEIGHT);
        this.addDrawable(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(5, 5));

        final JTextField titleField = new JTextField(note.getTitle());
        titleField.setFont(TITLE_FONT);
        titleField.setBorder(null);
        titleField.setPreferredSize(new Dimension(WIDTH, 50));

        textPane.setFont(TEXT_FONT);
        textPane.setBorder(null);
        textPane.setPreferredSize(new Dimension(WIDTH, HEIGHT - 50));
        textPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent e) {
                super.focusLost(e);
                repaint();
            }
        });

        // load plain text
        for (@NotNull final String line : note.getLines()) {
            textPane.setText(textPane.getText() + line + "\n");
        }

        // format text
        for (@NotNull final Map.Entry<TextStyleType, LinkedList<TextStyleInformation.StyleInformation>> styleEntry : note.getTextStyleInformation().getStyles().entrySet()) {
            final TextStyleType style = styleEntry.getKey();

            for (@NotNull final TextStyleInformation.StyleInformation styleInformation : styleEntry.getValue()) {
                final StyledDocument document = textPane.getStyledDocument();
                final Style documentStyle = document.addStyle(style.name() + styleInformation.getStartPosition(), null);
                style.setStyle(documentStyle);

                final String styled = textPane.getText().substring(
                    styleInformation.getStartPosition(),
                    styleInformation.getEndPosition()
                );

                document.remove(styleInformation.getStartPosition(), styled.length());
                document.insertString(styleInformation.getStartPosition(), styled, documentStyle);
            }
        }

        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        final RoundButton saveButton = new RoundButton("Speichern", 10);
        saveButton.addActionListener(e -> {
            final Note newNote = new Note(
                titleField.getText(),
                LocalDateTime.now(),
                new ArrayList<>(Arrays.asList(textPane.getText().split("\n")))
            );

            NotesHandler.saveNote(newNote);
            TextStyleHandler.saveTextStyle(newNote, note.getTextStyleInformation());
            NotesHandler.deleteNote(note);
            TextStyleHandler.deleteTextStyle(note);
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

        for (@NotNull final TextStyleType style : TextStyleType.values()) {
            final JToggleButton toggleButton = new JToggleButton(style.getStyledTextAction());
            toggleButton.addActionListener(e -> {
                final TextStyleInformation textStyleInformation = note.getTextStyleInformation();

                if (toggleButton.isSelected()) {
                    if (!textStyleInformation.getStyles().containsKey(style)) {
                        textStyleInformation.getStyles().put(style, new LinkedList<>());
                    }

                    textStyleInformation.getStyles().get(style).addLast(new TextStyleInformation.StyleInformation());
                    textStyleInformation.getStyles().get(style).getLast().setStartPosition(textPane.getCaretPosition());
                    return;
                }

                textStyleInformation.getStyles().get(style).getLast().setEndPosition(textPane.getCaretPosition());
            });
            panel.add(toggleButton);
        }

        panel.add(saveButton);
        panel.add(deleteButton);

        this.add(textPane, BorderLayout.CENTER);
        this.add(titleField, BorderLayout.NORTH);
        this.add(panel, BorderLayout.SOUTH);
        this.pack();
    }

    @Override
    public void draw(final @NotNull Graphics2D g) {
        if (!textPane.getText().isEmpty()) return;

        g.drawString("Notizen hinzufügen...", 5, textPane.getY() + g.getFontMetrics().getAscent());
    }
}
