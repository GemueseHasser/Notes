package de.jonas.notes.handler;

import de.jonas.notes.Notes;
import de.jonas.notes.object.Note;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public final class NotesHandler {

    @NotNull
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy'T'HH-mm-ss");


    @NotNull
    public static List<Note> getNotes() {
        final List<Note> notes = new ArrayList<>();

        for (@NotNull final File noteFile : getNoteFiles()) {
            try {
                final List<String> lines = Files.readAllLines(noteFile.toPath());
                final String title = lines.get(0);
                final LocalDateTime dateTime = LocalDateTime.parse(lines.get(1), FORMATTER);
                final List<String> noteLines = lines.subList(2, lines.size());

                notes.add(new Note(title, dateTime, noteLines));
            } catch (@NotNull final IOException e) {
                throw new RuntimeException(e);
            }
        }

        return notes;
    }

    @Nullable
    public static Note getNote(@NotNull final String dateTime) {
        try {
            final File noteFile = getNoteFile(getDateTime(dateTime));

            if (noteFile == null) return null;

            final List<String> lines = Files.readAllLines(noteFile.toPath());
            final String title = lines.get(0);
            final List<String> noteLines = lines.subList(2, lines.size());

            return new Note(title, getDateTime(dateTime), noteLines);
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveNote(@NotNull final Note note) {
        final File noteFile = new File(
            Notes.NOTES_FOLDER + File.separator + getDateTimeText(note.getDateTime()) + ".txt"
        );
        try {
            noteFile.createNewFile();
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }

        try (@NotNull final BufferedWriter writer = new BufferedWriter(new FileWriter(noteFile, false))) {
            writer.write(note.getTitle());
            writer.newLine();
            writer.write(getDateTimeText(note.getDateTime()));
            writer.newLine();

            for (@NotNull final String line : note.getLines()) {
                writer.write(line);
                writer.newLine();
            }
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static String getDateTimeText(@NotNull final LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    @NotNull
    public static LocalDateTime getDateTime(@NotNull final String dateTime) {
        return LocalDateTime.parse(dateTime, FORMATTER);
    }

    @NotNull
    private static List<File> getNoteFiles() {
        final List<File> notes = new ArrayList<>();
        final File[] files = Notes.NOTES_FOLDER.listFiles();

        if (files == null) return notes;

        for (@NotNull final File file : files) {
            // check if file is a text document
            if (!file.isFile() || !file.getName().endsWith(".txt")) continue;

            final String fileName = file.getName().substring(0, file.getName().length() - 4);
            try {
                LocalDateTime.parse(fileName, FORMATTER);
                notes.add(file);
            } catch (@NotNull final DateTimeParseException ignored) {
            }
        }

        return notes;
    }

    @Nullable
    private static File getNoteFile(@NotNull final LocalDateTime dateTime) {
        final File noteFile = new File(Notes.NOTES_FOLDER + File.separator + getDateTimeText(dateTime) + ".txt");

        if (noteFile.exists()) return noteFile;
        return null;
    }

}
