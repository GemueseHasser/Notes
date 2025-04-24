package de.jonas.notes.handler;

import de.jonas.notes.Notes;
import de.jonas.notes.constant.TextStyleType;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.TextStyleInformation;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Map;

public final class TextStyleHandler {

    public static void deleteTextStyle(@NotNull final Note note) {
        final File styleFile = new File(
            Notes.NOTES_FOLDER + File.separator + NotesHandler.getDateTimeText(note.getDateTime()) + ".txtstyle"
        );
        if (!styleFile.exists()) return;

        styleFile.delete();
    }

    public static void saveTextStyle(@NotNull final Note note, @NotNull final TextStyleInformation style) {
        final File styleFile = new File(
            Notes.NOTES_FOLDER + File.separator + NotesHandler.getDateTimeText(note.getDateTime()) + ".txtstyle"
        );
        try {
            styleFile.createNewFile();
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }

        try (@NotNull final BufferedWriter writer = new BufferedWriter(new FileWriter(styleFile, false))) {
            for (@NotNull final Map.Entry<TextStyleType, LinkedList<TextStyleInformation.StyleInformation>> styleEntry : style.getStyles().entrySet()) {
                final String styleName = styleEntry.getKey().name();

                for (@NotNull final TextStyleInformation.StyleInformation styleInformation : styleEntry.getValue()) {
                    final int startPosition = styleInformation.getStartPosition();
                    final int endPosition = styleInformation.getEndPosition();

                    writer.write(styleName + ":" + startPosition + ":" + endPosition + "\n");
                }
            }
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadTextStyle(@NotNull final Note note) {
        final File styleFile = new File(
            Notes.NOTES_FOLDER + File.separator + NotesHandler.getDateTimeText(note.getDateTime()) + ".txtstyle"
        );

        try {
            for (@NotNull final String line : Files.readAllLines(styleFile.toPath())) {
                final String[] parts = line.split(":");
                if (parts.length != 3) continue;

                final TextStyleType styleType = TextStyleType.valueOf(parts[0]);
                final int startPosition = Integer.parseInt(parts[1]);
                final int endPosition = Integer.parseInt(parts[2]);

                final TextStyleInformation information = note.getTextStyleInformation();

                if (!information.getStyles().containsKey(styleType)) {
                    information.getStyles().put(styleType, new LinkedList<>());
                }

                final TextStyleInformation.StyleInformation styleInformation = new TextStyleInformation.StyleInformation();
                styleInformation.setStartPosition(startPosition);
                styleInformation.setEndPosition(endPosition);

                information.getStyles().get(styleType).add(styleInformation);
            }
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
