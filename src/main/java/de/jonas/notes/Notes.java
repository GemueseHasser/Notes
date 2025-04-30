package de.jonas.notes;

import de.jonas.notes.object.gui.NotebookOverviewGui;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.swing.UIManager;
import java.time.format.DateTimeFormatter;

public class Notes {

    @NotNull
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");


    @SneakyThrows
    public static void main(@NotNull final String @NotNull [] args) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        final NotebookOverviewGui notebookOverviewGui = new NotebookOverviewGui();
        notebookOverviewGui.open();
    }

}
