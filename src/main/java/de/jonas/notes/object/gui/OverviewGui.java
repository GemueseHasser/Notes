package de.jonas.notes.object.gui;

import de.jonas.notes.handler.NotesHandler;
import de.jonas.notes.listener.NoteClickListener;
import de.jonas.notes.object.Drawable;
import de.jonas.notes.object.Gui;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.component.RoundButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.format.DateTimeFormatter;

public final class OverviewGui extends Gui implements Drawable {

    @NotNull
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    @NotNull
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");


    @NotNull
    private static final String TITLE = "Notizbücher";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int NOTES_MARGIN_TOP = 40;


    @NotNull
    private final JPanel notesPanel;


    public OverviewGui() {
        super(TITLE, WIDTH, HEIGHT);
        this.addDrawable(this);
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        notesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        notesPanel.setBounds(0, NOTES_MARGIN_TOP, WIDTH, HEIGHT - NOTES_MARGIN_TOP);

        for (@NotNull final Note note : NotesHandler.getNotes()) {
            addNoteButton(note);
        }

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(@NotNull final ComponentEvent e) {
                super.componentResized(e);

                notesPanel.setBounds(
                    0,
                    NOTES_MARGIN_TOP,
                    e.getComponent().getWidth(),
                    e.getComponent().getHeight() - NOTES_MARGIN_TOP
                );
            }
        });
        this.add(notesPanel);
    }


    public void addNoteButton(@NotNull final Note note) {
        final RoundButton button = new RoundButton(
            "<html>" + note.getTitle() + "<br><br>" + FORMATTER.format(note.getDateTime()),
            30
        );
        button.setPreferredSize(new Dimension(150, 150));
        button.addActionListener(new NoteClickListener(note));
        notesPanel.add(button);
    }


    @Override
    public void draw(@NotNull final Graphics2D g) {
        g.setFont(TITLE_FONT);
        g.drawString(
            TITLE,
            this.getWidth() / 2 - g.getFontMetrics().stringWidth(TITLE) / 2,
            5 + g.getFontMetrics().getAscent()
        );
    }
}
