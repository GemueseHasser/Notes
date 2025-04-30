package de.jonas.notes;

import de.jonas.notes.object.gui.NotebookOverviewGui;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.swing.UIManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

public class Notes {

    @NotNull
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    @NotNull
    public static final File INFO_FILE = new File("Notes/notebooks.info");


    @SneakyThrows
    public static void main(@NotNull final String @NotNull [] args) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Files.createDirectories(Paths.get(INFO_FILE.getParent()));
        INFO_FILE.createNewFile();

        final NotebookOverviewGui notebookOverviewGui = new NotebookOverviewGui();
        notebookOverviewGui.open();
    }

}
