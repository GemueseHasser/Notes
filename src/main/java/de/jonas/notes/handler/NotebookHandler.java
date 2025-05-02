package de.jonas.notes.handler;

import de.jonas.notes.Notes;
import de.jonas.notes.object.Notebook;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class NotebookHandler {

    @NotNull
    public static List<Notebook> loadNotebooks() {
        final List<Notebook> notebooks = new ArrayList<>();
        final File notebooksDir = new File("Notes");

        for (@NotNull final File notebookFile : notebooksDir.listFiles()) {
            if (!notebookFile.isDirectory()) continue;

            final String notebookName = notebookFile.getName();

            try {
                for (@NotNull final String line : Files.readAllLines(Notes.INFO_FILE.toPath())) {
                    final String[] parts = line.split(":", 2);

                    if (parts[0].equals(notebookName)) {
                        notebooks.add(new Notebook(parts[0], LocalDateTime.parse(parts[1], Notes.FORMATTER)));
                    }
                }
            } catch (@NotNull final IOException e) {
                throw new RuntimeException(e);
            }
        }

        return notebooks.stream()
            .sorted((date1, date2) -> date1.getLastAccess().isBefore(date2.getLastAccess()) ? 1 :
                                      date1.getLastAccess().equals(date2.getLastAccess()) ? 0 : -1
            ).collect(Collectors.toList());
    }

    public static void setLastAccessTimestamp(@NotNull final String notebookName) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(Notes.INFO_FILE, true))) {
            // save lines
            final List<String> lines = Files.readAllLines(Notes.INFO_FILE.toPath());

            // delete info content
            new FileWriter(Notes.INFO_FILE, false).close();

            // write updated info content
            for (@NotNull final String infoLine : lines) {
                final String[] parts = infoLine.split(":", 2);
                if (parts[0].equals(notebookName)) continue;

                writer.write(infoLine + "\n");
            }

            final File notebookFile = new File("Notes" + File.separator + notebookName);
            if (!notebookFile.exists()) return;

            writer.write(notebookName + ":" + LocalDateTime.now().format(Notes.FORMATTER) + "\n");
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
