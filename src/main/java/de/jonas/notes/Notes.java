package de.jonas.notes;

import de.jonas.notes.object.gui.OverviewGui;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Notes {

    //<editor-fold desc="CONSTANTS">
    /** Der Ordner, in welchem alle Dateien gespeichert werden, die mit dieser Anwendung zutun haben. */
    @NotNull
    public static final File NOTES_FOLDER = new File("Notes");
    //</editor-fold>


    public static void main(@NotNull final String @NotNull [] args) throws IOException {
        Files.createDirectories(NOTES_FOLDER.toPath());

        final OverviewGui overviewGui = new OverviewGui();
        overviewGui.open();
    }

}
