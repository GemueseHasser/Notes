package de.jonas.notes.object.gui;

import de.jonas.notes.constant.ImageType;
import de.jonas.notes.handler.NotebookHandler;
import de.jonas.notes.listener.NotebookClickListener;
import de.jonas.notes.object.Notebook;
import de.jonas.notes.object.component.RoundButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

public final class NotebookOverviewGui extends OverviewGui {

    @NotNull
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    @NotNull
    private static final String TITLE = "Notizbücher";
    private static final int WIDTH = 800;
    private static final int NOTE_BUTTON_SIZE = 150;
    private static final int NOTES_COLUMN_COUNT = WIDTH / NOTE_BUTTON_SIZE - 1;


    public NotebookOverviewGui() {
        super(TITLE, "Notizbücher");
        super.loadOverviewGui();
    }


    @Override
    public void loadButtons(@NotNull JPanel buttonsPanel) {
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

        super.reloadButtons();
        this.revalidate();
    }


    public void addNotebookButton(@NotNull final Notebook notebook) {
        final RoundButton button = new RoundButton(
            "<html>" + notebook.getName() + "<br><br>" + FORMATTER.format(notebook.getLastAccess()),
            30,
            this
        );
        button.setPreferredSize(new Dimension(NOTE_BUTTON_SIZE, NOTE_BUTTON_SIZE));
        button.addActionListener(new NotebookClickListener(notebook, this));

        constraints.gridx = buttonsPanel.getComponentCount() % NOTES_COLUMN_COUNT;
        constraints.gridy = buttonsPanel.getComponentCount() / NOTES_COLUMN_COUNT;
        buttonsPanel.add(button, constraints);
    }

}
