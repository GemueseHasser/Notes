package de.jonas.notes;

import de.jonas.notes.object.gui.OverviewGui;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.swing.UIManager;
import java.io.File;
import java.nio.file.Files;

public class Notes {

    //<editor-fold desc="CONSTANTS">
    /** Der Ordner, in welchem alle Dateien gespeichert werden, die mit dieser Anwendung zutun haben. */
    @NotNull
    public static final File NOTES_FOLDER = new File("Notes");
    //</editor-fold>


    @Getter
    private static OverviewGui overviewGui;


    @SneakyThrows
    public static void main(@NotNull final String @NotNull [] args) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Files.createDirectories(NOTES_FOLDER.toPath());

        overviewGui = new OverviewGui();
        overviewGui.open();
    }

}
