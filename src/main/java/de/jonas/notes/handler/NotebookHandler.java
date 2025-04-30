package de.jonas.notes.handler;

import de.jonas.notes.Notes;
import de.jonas.notes.object.Notebook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class NotebookHandler {

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

        return notebooks;
    }

}
