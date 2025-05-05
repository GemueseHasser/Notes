package de.jonas.notes.constant;

import de.jonas.notes.object.Notebook;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Ein {@link FileType} stellt einen Typen der Dateistruktur dar, um die generierten Dateien dieser Anwendung sortiert
 * (nach dem jeweiligen Typen) abzuspeichern.
 */
@RequiredArgsConstructor
public enum FileType {

    //<editor-fold desc="VALUES">
    /** Der Typ für die Ressourcen, die einer Notiz hinzugefügt wurden (Bilddateien). */
    RESOURCES("Resources"),
    /** Der Typ für die Style-Dateien, in denen Text-Formatierungen gespeichert werden. */
    STYLE("Style"),
    /**
     * Der Typ für die Dateien, in denen ausschließlich der Titel, der letzte Bearbeitungszeitpunkt und der Text der
     * Notiz abgespeichert werden.
     */
    RAW("Raw");
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Der Name des Ordners dieses Typen. */
    @NotNull
    private final String fileName;
    //</editor-fold>


    /**
     * Erzeugt auf der Grundlage eines bestimmten Notizbuchs den Ordner dieses {@link FileType Typen} - sofern dieser
     * nicht existiert - und gibt diesen zurück.
     *
     * @param notebook Das Notizbuch, für den dieser {@link File Ordner} zurückgegeben werden soll.
     *
     * @return Der Ordner dieses Typen auf der Grundlage eines bestimmten Notizbuchs.
     */
    public File getFile(@NotNull final Notebook notebook) {
        try {
            final File file = new File("Notes" + File.separator + notebook.getName() + File.separator + fileName);
            Files.createDirectories(file.toPath());
            return file;
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
