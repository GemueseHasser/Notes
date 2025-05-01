package de.jonas.notes.constant;

import de.jonas.notes.object.gui.NoteOverviewGui;
import de.jonas.notes.object.gui.NotebookOverviewGui;
import de.jonas.notes.object.gui.OverviewGui;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Ein {@link ImageType Typ} wird für jedes Bild erzeugt, welches genau einmal geladen werden soll. Dieser Typ wird auf
 * der Grundlage des Namens des Bildes erzeugt, womit ein {@link BufferedImage} mit einer bestimmten Größe generiert
 * wird.
 */
@Getter
public enum ImageType {

    //<editor-fold desc="VALUES">
    /** Der Typ für das Icon des Buttons, um Notizen hinzuzufügen. */
    ADD_NOTE_ICON("addButton.png", OverviewGui.CREATE_BUTTON_SIZE, OverviewGui.CREATE_BUTTON_SIZE),
    /** Der Typ des Bildes, welcher im Option-Pane angezeigt wird, wenn man eine neue Notiz erstellt. */
    CREATE_NOTE_ICON("createNote.png", 130, 150),
    /** Der Typ des Bildes, welcher im Option-Pane angezeigt wird, wenn man etwas löschen möchte. */
    DELETE_ICON("deleteNote.png", 150, 150),
    /** Der Typ des Bildes, welches im Option-Pane angezeigt wird, wenn man ein Bild einfügen möchte. */
    INSERT_IMAGE_ICON("insertImage.png", 150, 150),
    NOTEBOOK_ICON("notebook.png", NotebookOverviewGui.NOTEBOOK_BUTTON_SIZE, NotebookOverviewGui.NOTEBOOK_BUTTON_SIZE),
    NOTE_ICON("note.png", NoteOverviewGui.NOTE_BUTTON_SIZE, NoteOverviewGui.NOTE_BUTTON_SIZE),
    DELETE_NOTEBOOK_ICON("deleteNotebook.png", 70, 70),
    LOGO("logo.png", 50, 50);
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Das Bild, welches auf der Grundlage dieses Typs generiert wird. */
    @NotNull
    private final BufferedImage image;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt einen neuen und vollständig unabhängigen {@link ImageType Typen}. Ein {@link ImageType Typ} wird für
     * jedes Bild erzeugt, welches genau einmal geladen werden soll. Dieser Typ wird auf der Grundlage des Namens des
     * Bildes erzeugt, womit ein {@link BufferedImage} mit einer bestimmten Größe generiert wird.
     *
     * @param imageName Der Name des Bildes, welches im resources-Ordner des Projekts liegt, welches geladen werden
     *                  soll.
     * @param width     Die Breite, die das Bild haben soll.
     * @param height    Die Höhe, die das Bild haben soll.
     */
    ImageType(
        @NotNull final String imageName,
        @Range(from = 0, to = Integer.MAX_VALUE) final int width,
        @Range(from = 0, to = Integer.MAX_VALUE) final int height
    ) {
        try {
            final BufferedImage tempImage = ImageIO.read(
                Objects.requireNonNull(getClass().getResource("/" + imageName))
            );
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.drawImage(tempImage, 0, 0, width, height, null);
            g2d.dispose();
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }
    //</editor-fold>

}