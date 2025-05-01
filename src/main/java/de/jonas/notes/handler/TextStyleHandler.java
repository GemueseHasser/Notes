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

public final class TextStyleHandler {

    public static void deleteTextStyle(@NotNull final Note note) {
        final File styleFile = new File(
            FileType.STYLE.getFile(note.getParentNotebook()) + File.separator + NotesHandler.getDateTimeText(note.getDateTime()) + ".notestyle"
        );
        if (!styleFile.exists()) return;

        styleFile.delete();
    }

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
            // save text format
            for (@NotNull final Map.Entry<Integer, List<TextStyleType>> styleEntry : style.getStyles().entrySet()) {
                final int position = styleEntry.getKey();
                final List<TextStyleType> styleTypes = styleEntry.getValue();

                final StringBuilder lineBuilder = new StringBuilder();
                lineBuilder.append(position).append(":");

                for (@NotNull final TextStyleType styleType : styleTypes) {
                    lineBuilder.append(styleType.name()).append(",");
                }

                lineBuilder.deleteCharAt(lineBuilder.length() - 1).append("\n");
                writer.write(lineBuilder.toString());
            }

            // save images
            for (@NotNull final Map.Entry<Integer, File> imageEntry : style.getImages().entrySet()) {
                final int startPosition = imageEntry.getKey();
                final File imageFile = imageEntry.getValue();

                writer.write("IMAGE:" + startPosition + ":" + imageFile.getAbsoluteFile() + "\n");
            }
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

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

                // load image
                if (parts[0].equals("IMAGE")) {
                    final int position = Integer.parseInt(parts[1].split(":")[0]);
                    final String path = parts[1].split(":")[1];

                    information.getImages().put(position, new File(path));
                    continue;
                }

                final int position = Integer.parseInt(parts[0]);
                final String[] textStyles = parts[1].split(",");
                final List<TextStyleType> textStyleTypes = new ArrayList<>();

                for (@NotNull final String textStyle : textStyles) {
                    textStyleTypes.add(TextStyleType.valueOf(textStyle));
                }

                information.getStyles().put(position, textStyleTypes);
            }
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
