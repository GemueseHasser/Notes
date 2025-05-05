package de.jonas.notes.handler;

import de.jonas.notes.constant.FileType;
import de.jonas.notes.constant.TextStyleType;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.TextStyleInformation;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mithilfe dieses Handlers lassen sich die Text-Formatierungen einer Notiz losgelöst von der Notiz - mit demselben
 * Datenamen wie die Notiz - selber als 'notestyle'-Datei abspeichern, laden und löschen.
 */
public final class TextStyleHandler {

    //<editor-fold desc="utility">

    /**
     * Löscht eine Text-Style-Datei einer bestimmten Notiz.
     *
     * @param note Die Notiz, dessen Text-Style-Datei gelöscht werden soll.
     */
    public static void deleteTextStyle(@NotNull final Note note) {
        final File styleFile = new File(
            FileType.STYLE.getFile(note.getParentNotebook()) + File.separator + NotesHandler.getDateTimeText(note.getDateTime()) + ".notestyle"
        );
        if (!styleFile.exists()) return;

        styleFile.delete();
    }

    /**
     * Speichert alle Formatierungen einer Notiz in dessen Text-Style-Datei ab.
     *
     * @param note  Die Notiz, dessen Formatierungen abgespeichert werden sollen.
     * @param style Die Informationen der Formatierungen, die diese Notiz beinhaltet.
     */
    public static void saveTextStyle(
        @NotNull final Note note,
        @NotNull final TextStyleInformation style
    ) {
        final File styleFile = new File(
            FileType.STYLE.getFile(note.getParentNotebook()) + File.separator + NotesHandler.getDateTimeText(note.getDateTime()) + ".notestyle"
        );
        try {
            styleFile.createNewFile();
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }

        try (@NotNull final BufferedWriter writer = new BufferedWriter(new FileWriter(styleFile, false))) {
            final StringBuilder lineBuilder = new StringBuilder();

            // save font family
            lineBuilder.append("FONT_FAMILY:").append(style.getFontFamily()).append("\n");

            // save text format
            for (@NotNull final Map.Entry<TextStyleType, List<Integer>> styleEntry : style.getStyles().entrySet()) {
                final TextStyleType styleType = styleEntry.getKey();
                final List<Integer> positions = styleEntry.getValue();

                lineBuilder.append(styleType.name()).append(":");

                for (final int position : positions) {
                    lineBuilder.append(position).append(",");
                }

                lineBuilder.deleteCharAt(lineBuilder.length() - 1).append("\n");
            }

            // save images
            for (@NotNull final Map.Entry<Integer, File> imageEntry : style.getImages().entrySet()) {
                final int startPosition = imageEntry.getKey();
                final File imageFile = imageEntry.getValue();

                lineBuilder.append("IMAGE:").append(startPosition).append(":").append(imageFile.getAbsoluteFile()).append(
                    "\n");
            }

            writer.write(lineBuilder.toString());
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lädt die {@link TextStyleInformation Formatierungen} einer bestimmten Notiz.
     *
     * @param note Die Notiz, dessen {@link TextStyleInformation Formatierungen} geladen werden sollen.
     */
    public static void loadTextStyle(@NotNull final Note note) {
        final File styleFile = new File(
            FileType.STYLE.getFile(note.getParentNotebook()) + File.separator + NotesHandler.getDateTimeText(note.getDateTime()) + ".notestyle"
        );
        try {
            if (styleFile.createNewFile()) return;
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }

        try {
            for (@NotNull final String line : Files.readAllLines(styleFile.toPath())) {
                final String[] parts = line.split(":", 2);

                final TextStyleInformation information = note.getTextStyleInformation();

                // load font family
                if (parts[0].equals("FONT_FAMILY")) {
                    information.setFontFamily(parts[1]);
                    return;
                }

                // load image
                if (parts[0].equals("IMAGE")) {
                    final int position = Integer.parseInt(parts[1].split(":", 2)[0]);
                    final String path = parts[1].split(":", 2)[1];

                    information.getImages().put(position, new File(path));
                    continue;
                }

                final TextStyleType styleType = TextStyleType.valueOf(parts[0]);
                final String[] positionsText = parts[1].split(",");
                final List<Integer> positions = new ArrayList<>();

                for (@NotNull final String position : positionsText) {
                    positions.add(Integer.parseInt(position));
                }

                information.getStyles().put(styleType, positions);
            }
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }
    //</editor-fold>

}
