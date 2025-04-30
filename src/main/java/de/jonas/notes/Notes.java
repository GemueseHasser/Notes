package de.jonas.notes;

import de.jonas.notes.object.gui.NotebookOverviewGui;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.swing.UIManager;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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


    //<editor-fold desc="utility">
    @NotNull
    public static Graphics2D getImageGraphics(@NotNull final BufferedImage image) {
        final Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return g;
    }
    //</editor-fold>

}
