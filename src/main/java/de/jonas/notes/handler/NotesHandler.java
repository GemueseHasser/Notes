package de.jonas.notes.handler;

import de.jonas.notes.constant.FileType;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.Notebook;
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
import java.util.stream.Collectors;

/**
 * Mithilfe dieses Handlers lassen sich alle Notizen, die sich in einem bestimmten Notizbuch befinden verwalten. Dabei
 * kann man alle Notizen eines Notizbuchs laden, kann eine bestimmte Notiz abspeichern, aber auch löschen.
 * <p>Gleichzeitig stellt dieser Handler eine Formatierungshilfe für eine {@link LocalDateTime Zeitangabe} zur
 * verfügung. Dabei kann man sich einen bestimmten Zeitpunkt als formatierten Text zurückgeben lassen, aber auch aus
 * einem formatierten Text einen Zeitpunkt.</p>
 */
public final class NotesHandler {

    //<editor-fold desc="CONSTANTS">
    /** Die Definition der Formatierung, nach der alle Zeitpunkte formatiert werden. */
    @NotNull
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy'T'HH-mm-ss");
    //</editor-fold>


    //<editor-fold desc="utility">

    /**
     * Gibt eine Liste aller Notizen zurück, die sich in einem bestimmten Notizbuch befinden. Dabei werden die Notizen
     * nach den Zeitpunkten sortiert, zu denen sie zuletzt bearbeitet wurden.
     *
     * @param notebook Das Notizbuch, dessen Notizen geladen werden sollen.
     *
     * @return Eine Liste aller Notizen eines bestimmten Notizbuchs.
     */
    @NotNull
    public static List<Note> getNotes(@NotNull final Notebook notebook) {
        final List<Note> notes = new ArrayList<>();

        for (@NotNull final File noteFile : getNoteFiles(notebook)) {
            try {
                final List<String> lines = Files.readAllLines(noteFile.toPath());
                final String title = lines.get(0);
                final LocalDateTime dateTime = LocalDateTime.parse(lines.get(1), FORMATTER);
                final List<String> noteLines = lines.subList(2, lines.size());

                final Note note = new Note(title, dateTime, noteLines, notebook);
                TextStyleHandler.loadTextStyle(note);
                notes.add(note);
            } catch (@NotNull final IOException e) {
                throw new RuntimeException(e);
            }
        }

        return notes
            .stream()
            .sorted((date1, date2) -> date1.getDateTime().isBefore(date2.getDateTime()) ? 1 :
                                      date1.getDateTime().equals(date2.getDateTime()) ? 0 : -1
            ).collect(Collectors.toList());
    }

    /**
     * Löscht eine bestimmte Notiz aus seinem Notizbuch.
     *
     * @param note Die Notiz, die aus seinem Notizbuch gelöscht werden soll.
     *
     * @return Wenn die Notiz erfolgreich gelöscht wurde, oder gar nicht mehr existierte {@code true}, ansonsten
     *     {@code false}.
     */
    public static boolean deleteNote(@NotNull final Note note) {
        final File noteFile = getNoteFile(note.getParentNotebook(), note.getDateTime());
        if (!noteFile.exists()) return true;

        return noteFile.delete();
    }

    /**
     * Speichert eine bestimmte Notiz in seinem Notizbuch ab.
     *
     * @param note Die Notiz, die abgespeichert werden soll.
     */
    public static void saveNote(@NotNull final Note note) {
        final File noteFile = new File(
            FileType.RAW.getFile(note.getParentNotebook()) + File.separator + getDateTimeText(note.getDateTime()) + ".txt"
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
                writer.write(line + "\n");
            }
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gibt einen bestimmten {@link LocalDateTime Zeitpunkt} als formatierten Text zurück.
     *
     * @param dateTime Der Zeitpunkt, der formatiert zurückgegeben werden soll.
     *
     * @return Ein bestimmter Zeitpunkt als formatierten Text.
     */
    @NotNull
    public static String getDateTimeText(@NotNull final LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    /**
     * Erzeugt einen {@link LocalDateTime Zeitpunkt} aus einem formatierten Text.
     *
     * @param dateTime Der formatierte Text eines Zeitpunkts.
     *
     * @return Ein Zeitpunkt, aus einem formatierten Text eines Zeitpunkts.
     */
    @NotNull
    public static LocalDateTime getDateTime(@NotNull final String dateTime) {
        return LocalDateTime.parse(dateTime, FORMATTER);
    }

    /**
     * Gibt eine Liste von Dateien zurück, die die Notizen eines bestimmten Notizbuchs enthalten.
     *
     * @param notebook Das Notizbuch, dessen Dateien geladen und zurückgegeben werden, die die Notizen beinhalten.
     *
     * @return Eine Liste von Dateien, die die Notizen eines bestimmten Notizbuchs enthalten.
     */
    @NotNull
    private static List<File> getNoteFiles(@NotNull final Notebook notebook) {
        final List<File> notes = new ArrayList<>();
        final File[] files = FileType.RAW.getFile(notebook).listFiles();

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

    /**
     * Gibt eine Datei aus einem Notizbuch zurück, die zu einem bestimmten Zeitpunkt zuletzt bearbeitet (oder erzeugt)
     * wurde.
     *
     * @param notebook Das Notizbuch, in welchem die Datei gesucht werden soll, welche zu einem bestimmten Zeitpunkt
     *                 zuletzt bearbeitet wurde.
     * @param dateTime Der Zeitpunkt, zu dem diese Datei zuletzt bearbeitet wurde.
     *
     * @return Eine Datei aus einem Notizbuch, die zu einem bestimmten Zeitpunkt zuletzt bearbeitet (oder erzeugt)
     *     wurde.
     */
    @Nullable
    private static File getNoteFile(
        @NotNull final Notebook notebook,
        @NotNull final LocalDateTime dateTime
    ) {
        final File noteFile = new File(FileType.RAW.getFile(notebook) + File.separator + getDateTimeText(dateTime) + ".txt");

        if (noteFile.exists()) return noteFile;
        return null;
    }
    //</editor-fold>

}
