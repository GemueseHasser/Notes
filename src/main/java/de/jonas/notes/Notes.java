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

/**
 * Die Haupt- und Main-Klasse dieser Anwendung. Diese Anwendung stellt eine Ansammlung an Notizbüchern dar, in denen man
 * Notizen erstellen, bearbeiten und löschen kann.
 * <p>Die Notizen werden mittels einer bestimmten Dateistruktur gespeichert. Dabei werden die Dateien, die den Text
 * der Notizen beinhalten von den Dateien, die die Textformatierungen und den eingefügten Bildern getrennt
 * gespeichert.</p>
 */
public class Notes {

    //<editor-fold desc="CONSTANTS">
    /** Die Art der Formatierung eines Zeitpunkts, wie sie dem Nutzer angezeigt wird. */
    @NotNull
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    /** Die Info-Datei, die alle Zeitpunkte aller Notizbücher beinhaltet, zu denen diese zuletzt bearbeitet wurden. */
    @NotNull
    public static final File INFO_FILE = new File("Notes/notebooks.info");
    //</editor-fold>


    //<editor-fold desc="main">

    /**
     * Der Einstiegspunkt dieses Programms. Zuallererst wird das Aussehen grundlegender Java-Elemente an das Aussehen
     * des Betriebssystems angepasst, auf dem diese Anwendung ausgeführt wird. Dann wird sichergestellt, dass der
     * Notes-Ordner und die darin liegende Info-Datei der Notizbücher existieren. Und erst dann wird die grafische
     * Oberfläche für den Benutzer gestartet. Der Benutzer bekommt eine Übersicht aller vorhandenen Notizbücher
     * angezeigt.
     *
     * @param args Die Argumente, die von der JRE übergeben werden; diese werden jedoch nicht verwendet.
     */
    @SneakyThrows
    public static void main(@NotNull final String @NotNull [] args) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Files.createDirectories(Paths.get(INFO_FILE.getParent()));
        INFO_FILE.createNewFile();

        final NotebookOverviewGui notebookOverviewGui = new NotebookOverviewGui();
        notebookOverviewGui.open();
    }
    //</editor-fold>


    //<editor-fold desc="utility">

    /**
     * Erzeugt ein Grafik-Objekt eines Bildes und schaltet Kantenglättung ein und setzt die Render-Qualität hoch, bevor
     * dieses Grafik-Objekt zurückgegeben wird.
     *
     * @param image Das Bild, für das ein Grafik-Objekt erzeugt werden soll.
     *
     * @return Das Grafik-Objekt des Bildes.
     */
    @NotNull
    public static Graphics2D getImageGraphics(@NotNull final BufferedImage image) {
        final Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return g;
    }
    //</editor-fold>

}
