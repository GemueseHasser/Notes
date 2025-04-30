package de.jonas.notes.object.gui;

import de.jonas.notes.Notes;
import de.jonas.notes.constant.ImageType;
import de.jonas.notes.handler.NotebookHandler;
import de.jonas.notes.handler.NotesHandler;
import de.jonas.notes.handler.TextStyleHandler;
import de.jonas.notes.listener.NoteClickListener;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.Notebook;
import de.jonas.notes.object.component.RoundButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.util.ArrayList;

public final class NoteOverviewGui extends OverviewGui {

    private static final int WIDTH = 800;
    private static final int NOTE_BUTTON_SIZE = 150;
    private static final int NOTES_COLUMN_COUNT = WIDTH / NOTE_BUTTON_SIZE - 1;


    @NotNull
    private final Notebook notebook;


    public NoteOverviewGui(@NotNull final Notebook notebook) {
        super(notebook.getName(), "Notizen");
        this.notebook = notebook;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.loadOverviewGui();
    }

    public void addNoteButton(@NotNull final Note note) {
        final RoundButton button = new RoundButton(
            "<html>" + note.getTitle() + "<br><br>" + Notes.FORMATTER.format(note.getDateTime()),
            30,
            this
        );
        button.setPreferredSize(new Dimension(NOTE_BUTTON_SIZE, NOTE_BUTTON_SIZE));
        button.addActionListener(new NoteClickListener(note, this));

        constraints.gridx = buttonsPanel.getComponentCount() % NOTES_COLUMN_COUNT;
        constraints.gridy = buttonsPanel.getComponentCount() / NOTES_COLUMN_COUNT;
        buttonsPanel.add(button, constraints);
    }

    @Override
    public void loadButtons(@NotNull JPanel buttonsPanel) {
        for (@NotNull final Note note : NotesHandler.getNotes(notebook)) {
            addNoteButton(note);
        }
    }

    @Override
    public void createButtonAction() {
        final String title = (String) JOptionPane.showInputDialog(
            null,
            "Welchen Titel soll die Notiz haben?",
            "Neue Notiz erstellen",
            JOptionPane.PLAIN_MESSAGE,
            new ImageIcon(ImageType.CREATE_NOTE_ICON.getImage()),
            null,
            null
        );

        if (title == null || title.isEmpty()) return;

        final Note note = new Note(title, LocalDateTime.now(), new ArrayList<>(), notebook);
        NotesHandler.saveNote(note);
        TextStyleHandler.saveTextStyle(note, note.getTextStyleInformation());

        super.reloadButtons();
        this.revalidate();

        NotebookHandler.setLastAccessTimestamp(notebook.getName());
    }

    @Override
    public void dispose() {
        super.dispose();

        final NotebookOverviewGui notebookOverviewGui = new NotebookOverviewGui();
        notebookOverviewGui.open();
    }
}
