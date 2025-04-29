package de.jonas.notes.object.gui;

import de.jonas.notes.constant.ImageType;
import de.jonas.notes.handler.NotesHandler;
import de.jonas.notes.handler.TextStyleHandler;
import de.jonas.notes.listener.NoteClickListener;
import de.jonas.notes.object.Drawable;
import de.jonas.notes.object.Gui;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.component.RoundButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public final class OverviewGui extends Gui implements Drawable {

    public static final int CREATE_BUTTON_SIZE = 70;
    @NotNull
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    @NotNull
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    @NotNull
    private static final String TITLE = "Notizbücher";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int NOTE_BUTTON_SIZE = 150;
    private static final int NOTES_MARGIN_TOP = 40;
    private static final int CREATE_BUTTON_MARGIN_BOTTOM = 50;
    private static final int NOTES_COLUMN_COUNT = WIDTH / NOTE_BUTTON_SIZE - 1;


    @NotNull
    private final GridBagConstraints constraints = new GridBagConstraints();
    @NotNull
    private final JPanel notesPanel;


    public OverviewGui() {
        super("", WIDTH, HEIGHT);
        this.addDrawable(this);
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setResizable(false);

        final JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, WIDTH, HEIGHT);
        layeredPane.setLayout(null);

        final JScrollPane notesScrollPane = new JScrollPane();
        notesScrollPane.setBounds(
            0,
            NOTES_MARGIN_TOP,
            WIDTH - 18,
            HEIGHT - 2 * NOTES_MARGIN_TOP - this.getInsets().top
        );

        final GridBagLayout gridBagLayout = new GridBagLayout();
        notesPanel = new JPanel(gridBagLayout);
        constraints.insets = new Insets(5, 5, 5, 5);

        final RoundButton createNoteButton = new RoundButton("", 100, this);
        createNoteButton.setBounds(
            WIDTH - (int) (CREATE_BUTTON_SIZE * 1.5),
            HEIGHT - CREATE_BUTTON_SIZE - CREATE_BUTTON_MARGIN_BOTTOM,
            CREATE_BUTTON_SIZE,
            CREATE_BUTTON_SIZE
        );
        createNoteButton.setIcon(new ImageIcon(ImageType.ADD_NOTE_ICON.getImage()));
        createNoteButton.setFont(TITLE_FONT.deriveFont(15F));
        createNoteButton.addActionListener(e -> {
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

            final Note note = new Note(title, LocalDateTime.now(), new ArrayList<>());
            NotesHandler.saveNote(note);
            TextStyleHandler.saveTextStyle(note, note.getTextStyleInformation());

            reloadNotes();
            this.revalidate();
        });

        loadNotes();
        notesScrollPane.setViewportView(notesPanel);

        layeredPane.add(notesScrollPane, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(createNoteButton, JLayeredPane.PALETTE_LAYER);
        this.add(layeredPane);
    }


    public void reloadNotes() {
        notesPanel.removeAll();
        loadNotes();
        notesPanel.revalidate();
        repaint();
    }

    public void loadNotes() {
        for (@NotNull final Note note : NotesHandler.getNotes()) {
            addNoteButton(note);
        }
    }

    public void addNoteButton(@NotNull final Note note) {
        final RoundButton button = new RoundButton(
            "<html>" + note.getTitle() + "<br><br>" + FORMATTER.format(note.getDateTime()),
            30,
            this
        );
        button.setPreferredSize(new Dimension(NOTE_BUTTON_SIZE, NOTE_BUTTON_SIZE));
        button.addActionListener(new NoteClickListener(note));

        constraints.gridx = notesPanel.getComponentCount() % NOTES_COLUMN_COUNT;
        constraints.gridy = notesPanel.getComponentCount() / NOTES_COLUMN_COUNT;
        notesPanel.add(button, constraints);
    }

    @Override
    public void draw(@NotNull final Graphics2D g) {
        g.setFont(TITLE_FONT);
        g.drawString(
            TITLE,
            this.getWidth() / 2 - g.getFontMetrics().stringWidth(TITLE) / 2,
            5 + g.getFontMetrics().getAscent()
        );
        g.drawLine(0, NOTES_MARGIN_TOP - 2, this.getWidth(), NOTES_MARGIN_TOP - 2);

        if (notesPanel.getComponentCount() > 0) return;

        final String text = "Du hast noch keine Notizen erstellt.";
        g.drawString(
            text,
            WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2,
            HEIGHT / 2 - g.getFontMetrics().getAscent()
        );
    }
}
