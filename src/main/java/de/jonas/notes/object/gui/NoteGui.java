package de.jonas.notes.object.gui;

import de.jonas.notes.Notes;
import de.jonas.notes.constant.ImageType;
import de.jonas.notes.constant.TextStyleType;
import de.jonas.notes.handler.NotesHandler;
import de.jonas.notes.handler.TextStyleHandler;
import de.jonas.notes.listener.CursorListener;
import de.jonas.notes.object.Drawable;
import de.jonas.notes.object.Gui;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.TextStyleInformation;
import de.jonas.notes.object.component.RoundButton;
import de.jonas.notes.object.component.RoundToggleButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class NoteGui extends Gui implements Drawable {

    @NotNull
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 22);
    @NotNull
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 18);
    private static final int WIDTH = 700;
    private static final int HEIGHT = 550;


    @NotNull
    private final List<RoundToggleButton> styleButtons = new ArrayList<>();
    @NotNull
    private final JTextPane textPane = new JTextPane();
    @NotNull
    private final JScrollPane scrollTextPane = new JScrollPane(textPane);


    public NoteGui(@NotNull final Note note) throws BadLocationException {
        super(note.getTitle(), WIDTH, HEIGHT);
        this.addDrawable(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(5, 5));

        final JToolBar toolBar = new JToolBar();

        for (@NotNull final TextStyleType style : TextStyleType.values()) {
            final RoundToggleButton toggleButton = new RoundToggleButton(style.getStyledTextAction(), 10, this);
            toggleButton.setText(style.getText());
            toggleButton.addActionListener(e -> {
                if (styleButtons.stream().filter(AbstractButton::isSelected).count() > 1) {
                    toggleButton.setSelected(false);
                    return;
                }

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
            styleButtons.add(toggleButton);
            toolBar.add(toggleButton);
            toolBar.addSeparator();
        }

        final JTextField titleField = new JTextField(note.getTitle());
        titleField.setHorizontalAlignment(JTextField.CENTER);
        titleField.setFont(TITLE_FONT);
        titleField.setPreferredSize(new Dimension(WIDTH, 50));
        titleField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        titleField.addMouseListener(new CursorListener(this));

        final JPanel pageStartPanel = new JPanel(new GridLayout(2, 1));
        pageStartPanel.add(titleField, 0);
        pageStartPanel.add(toolBar, 1);

        textPane.setFont(TEXT_FONT);
        textPane.setBorder(null);
        textPane.setPreferredSize(new Dimension(WIDTH, HEIGHT - 50));
        textPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        textPane.addMouseListener(new CursorListener(this));
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
            final TextStyleType styleType = styleEntry.getKey();

            for (@NotNull final TextStyleInformation.StyleInformation styleInformation : styleEntry.getValue()) {
                final StyledDocument document = textPane.getStyledDocument();
                final Style documentStyle = document.addStyle(
                    styleType.name() + styleInformation.getStartPosition(),
                    null
                );
                styleType.setStyle(documentStyle);

                final String styled = textPane.getText().substring(
                    styleInformation.getStartPosition(),
                    styleInformation.getEndPosition()
                );

                document.remove(styleInformation.getStartPosition(), styled.length());
                document.insertString(styleInformation.getStartPosition(), styled, documentStyle);
            }
        }

        if (note.getLines().isEmpty()) textPane.setText("");

        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        final RoundButton saveButton = new RoundButton("Speichern", 10, this);
        saveButton.addActionListener(e -> {
            final Note newNote = new Note(
                titleField.getText(),
                LocalDateTime.now(),
                new ArrayList<>(Arrays.asList(textPane.getText().split("\n")))
            );

            // imitate button unselect
            for (@NotNull final RoundToggleButton styleButton : styleButtons) {
                if (!styleButton.isSelected()) continue;
                styleButton.setSelected(false);

                for (@NotNull final ActionListener actionListener : styleButton.getActionListeners()) {
                    actionListener.actionPerformed(new ActionEvent(
                        styleButton,
                        ActionEvent.ACTION_PERFORMED,
                        "close all style attributes"
                    ));
                }
            }

            NotesHandler.saveNote(newNote);
            TextStyleHandler.saveTextStyle(newNote, note.getTextStyleInformation());
            NotesHandler.deleteNote(note);
            TextStyleHandler.deleteTextStyle(note);
            Notes.getOverviewGui().reloadNotes();

            this.dispose();
        });

        final RoundButton deleteButton = new RoundButton("Löschen", 10, this);
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

            TextStyleHandler.deleteTextStyle(note);
            this.dispose();
        });

        panel.add(saveButton);
        panel.add(deleteButton);

        this.add(pageStartPanel, BorderLayout.PAGE_START);
        this.add(scrollTextPane, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
        this.pack();
    }


    @Override
    public void draw(final @NotNull Graphics2D g) {
        if (!textPane.getText().trim().isEmpty()) return;

        g.setFont(TEXT_FONT);
        g.drawString("Notizen hinzufügen...", 7, scrollTextPane.getY() + g.getFontMetrics().getAscent());
    }
}
