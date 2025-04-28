package de.jonas.notes;

import de.jonas.notes.constant.ImageType;
import de.jonas.notes.object.gui.OverviewGui;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Notes {

    //<editor-fold desc="CONSTANTS">
    /** Der Ordner, in welchem alle Dateien gespeichert werden, die mit dieser Anwendung zutun haben. */
    @NotNull
    public static final File NOTES_FOLDER = new File("Notes");
    //</editor-fold>


    @Getter
    private static OverviewGui overviewGui;
    @Getter
    private static Cursor customTextCursor;


    public static void main(@NotNull final String @NotNull [] args) throws IOException {
        Files.createDirectories(NOTES_FOLDER.toPath());

        customTextCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            ImageType.TEXT_CURSOR.getImage(),
            new Point(15, 10),
            "custom text cursor"
        );

        overviewGui = new OverviewGui();
        overviewGui.open();
    }

}
