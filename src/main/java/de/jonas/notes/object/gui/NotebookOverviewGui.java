package de.jonas.notes.object.gui;

import de.jonas.notes.Notes;
import de.jonas.notes.constant.ImageType;
import de.jonas.notes.handler.NotebookHandler;
import de.jonas.notes.listener.NotebookClickListener;
import de.jonas.notes.object.Notebook;
import de.jonas.notes.object.component.RoundButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class NotebookOverviewGui extends OverviewGui {

    public static final int NOTE_BUTTON_SIZE = 150;
    @NotNull
    private static final Font NOTE_FONT = new Font("Arial", Font.PLAIN, 15);
    @NotNull
    private static final String TITLE = "Notizbücher";
    private static final int WIDTH = 800;
    private static final int NOTES_COLUMN_COUNT = WIDTH / NOTE_BUTTON_SIZE - 1;


    public NotebookOverviewGui() {
        super(TITLE, "Notizbücher");
        super.loadOverviewGui();
    }


    @Override
    public void loadButtons(@NotNull final JPanel buttonsPanel) {
        for (@NotNull final Notebook note : NotebookHandler.loadNotebooks()) {
            addNotebookButton(note);
        }
    }

    @Override
    public void createButtonAction() {
        final String name = (String) JOptionPane.showInputDialog(
            null,
            "Welchen Namen soll das Notizbuch haben?",
            "Neues Notizbuch erstellen",
            JOptionPane.PLAIN_MESSAGE,
            new ImageIcon(ImageType.CREATE_NOTE_ICON.getImage()),
            null,
            null
        );

        if (name == null || name.isEmpty()) return;

        final File notebookFile = new File("Notes" + File.separator + name);

        if (notebookFile.exists()) {
            JOptionPane.showMessageDialog(
                null,
                notebookFile.getName() + " existiert bereits.",
                "Fehler",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Files.createDirectories(notebookFile.toPath());
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }

        NotebookHandler.setLastAccessTimestamp(name);

        super.reloadButtons();
        this.revalidate();
    }


    public void addNotebookButton(@NotNull final Notebook notebook) {
        final BufferedImage notebookIcon = new BufferedImage(NOTE_BUTTON_SIZE, NOTE_BUTTON_SIZE, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = Notes.getImageGraphics(notebookIcon);
        g.drawImage(ImageType.NOTEBOOK_ICON.getImage(), 0, 0, NOTE_BUTTON_SIZE, NOTE_BUTTON_SIZE, null);
        g.setFont(NOTE_FONT);
        g.setColor(Color.BLACK);
        g.drawString(
            notebook.getName(),
            notebookIcon.getWidth() / 2 - g.getFontMetrics().stringWidth(notebook.getName()) / 2,
            35
        );

        final String[] lastAccess = Notes.FORMATTER.format(notebook.getLastAccess()).split(" ");

        g.drawString(
            lastAccess[0],
            notebookIcon.getWidth() / 2 - g.getFontMetrics().stringWidth(lastAccess[0]) / 2,
            90
        );
        g.drawString(
            lastAccess[1],
            notebookIcon.getWidth() / 2 - g.getFontMetrics().stringWidth(lastAccess[1]) / 2,
            115
        );
        g.dispose();

        final RoundButton button = new RoundButton(
            notebookIcon,
            60,
            this
        );

        button.setPreferredSize(new Dimension(NOTE_BUTTON_SIZE, NOTE_BUTTON_SIZE));
        button.addActionListener(new NotebookClickListener(notebook, this));

        constraints.gridx = buttonsPanel.getComponentCount() % NOTES_COLUMN_COUNT;
        constraints.gridy = buttonsPanel.getComponentCount() / NOTES_COLUMN_COUNT;
        buttonsPanel.add(button, constraints);
    }

}
