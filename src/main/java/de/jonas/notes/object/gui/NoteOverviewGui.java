package de.jonas.notes.object.gui;

import de.jonas.notes.Notes;
import de.jonas.notes.constant.ImageType;
import de.jonas.notes.handler.FileHandler;
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Ein {@link NoteOverviewGui} stellt eine Instanz eines {@link OverviewGui} dar, welches dem Benutzer eine grafische
 * Übersicht aller bereits erstellten Notizen in einem bestimmten Notizbuch anzeigt.
 */
public final class NoteOverviewGui extends OverviewGui {

    //<editor-fold desc="CONSTANTS">
    /** Die Schriftart des Titels, der auf den Notizen angezeigt wird. */
    @NotNull
    private static final Font NOTE_TITLE_FONT = new Font("Arial", Font.PLAIN, 13);
    /** Die (rechteckige) Größe aller Notizen. */
    public static final int NOTE_BUTTON_SIZE = 150;
    /** Die Anzahl an Notizen, die maximal in einer Reihe dargestellt werden. */
    private static final int NOTES_COLUMN_COUNT = WIDTH / NOTE_BUTTON_SIZE - 1;
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Das Notizbuch, auf dessen Grundlage diese Übersicht der Notizen erzeugt wird. */
    @NotNull
    private final Notebook notebook;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt eine neue Instanz eines {@link NoteOverviewGui}. Ein {@link NoteOverviewGui} stellt eine Instanz eines
     * {@link OverviewGui} dar, welches dem Benutzer eine grafische Übersicht aller bereits erstellten Notizen in einem
     * bestimmten Notizbuch anzeigt.
     *
     * @param notebook Das Notizbuch, auf dessen Grundlage diese Übersicht der Notizen erzeugt wird.
     */
    public NoteOverviewGui(@NotNull final Notebook notebook) {
        super(notebook.getName(), "Notizen");
        this.notebook = notebook;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.loadOverviewGui();

        final RoundButton deleteButton = new RoundButton(ImageType.DELETE_NOTEBOOK_ICON.getImage(), 100, this);
        deleteButton.setBounds(
            (int) (WIDTH - CREATE_BUTTON_SIZE * 2.5),
            HEIGHT - CREATE_BUTTON_SIZE - CREATE_BUTTON_MARGIN_BOTTOM,
            CREATE_BUTTON_SIZE,
            CREATE_BUTTON_SIZE
        );
        deleteButton.addActionListener(e -> {
            final int delete = JOptionPane.showConfirmDialog(
                null,
                "Möchtest du dieses Notizbuch wirklich löschen?",
                "Notizbuch löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(ImageType.DELETE_ICON.getImage())
            );

            if (delete == JOptionPane.NO_OPTION) return;

            final File notebookFile = new File("Notes" + File.separator + notebook.getName());
            if (notebookFile.exists()) FileHandler.deleteDirectory(notebookFile);
            NotebookHandler.setLastAccessTimestamp(notebook.getName());
            this.dispose();
        });

        super.addTopLayerComponent(deleteButton);
    }
    //</editor-fold>


    /**
     * Erzeugt einen neuen Button, welcher eine bestimmte Notiz darstellt und fügt diesen Button zu diesem Fenster
     * hinzu. Dabei wird das Icon, welches jede Notiz bekommt mit dem richtigen Titel angepasst.
     *
     * @param note Die Notiz, welche diesem Fenster als Button hinzugefügt werden soll.
     */
    public void addNoteButton(@NotNull final Note note) {
        final BufferedImage notebookIcon = new BufferedImage(
            NOTE_BUTTON_SIZE,
            NOTE_BUTTON_SIZE,
            BufferedImage.TYPE_INT_ARGB
        );
        final Graphics2D g = Notes.getImageGraphics(notebookIcon);
        g.drawImage(ImageType.NOTE_ICON.getImage(), 0, 0, NOTE_BUTTON_SIZE, NOTE_BUTTON_SIZE, null);
        g.setFont(NOTE_TITLE_FONT);
        g.setColor(Color.BLACK);
        g.drawString(
            note.getTitle(),
            notebookIcon.getWidth() / 2 - g.getFontMetrics().stringWidth(note.getTitle()) / 2,
            60
        );

        final String[] lastAccess = Notes.FORMATTER.format(note.getDateTime()).split(" ");

        g.drawString(
            lastAccess[0],
            notebookIcon.getWidth() / 2 - g.getFontMetrics().stringWidth(lastAccess[0]) / 2,
            100
        );
        g.drawString(
            lastAccess[1],
            notebookIcon.getWidth() / 2 - g.getFontMetrics().stringWidth(lastAccess[1]) / 2,
            120
        );
        g.dispose();

        final RoundButton button = new RoundButton(
            notebookIcon,
            60,
            this
        );

        button.setPreferredSize(new Dimension(NOTE_BUTTON_SIZE, NOTE_BUTTON_SIZE));
        button.addActionListener(new NoteClickListener(note, this));

        constraints.gridx = buttonsPanel.getComponentCount() % NOTES_COLUMN_COUNT;
        constraints.gridy = buttonsPanel.getComponentCount() / NOTES_COLUMN_COUNT;
        buttonsPanel.add(button, constraints);
    }

    //<editor-fold desc="implementation">
    @Override
    public void loadButtons(@NotNull final JPanel buttonsPanel) {
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
    //</editor-fold>
}
