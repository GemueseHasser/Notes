package de.jonas.notes.handler;

import de.jonas.notes.object.Notebook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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

            LocalDateTime time = null;

            final File rawFolder = new File(notebooksDir + File.separator + notebookName + File.separator + "Raw");

            if (!rawFolder.exists()) {
                notebooks.add(new Notebook(notebookName, LocalDateTime.now()));
                continue;
            }

            for (@NotNull final File rawFile : rawFolder.listFiles()) {
                if (!rawFile.getName().endsWith(".txt")) continue;

                if (time == null) {
                    time = NotesHandler.getDateTime(rawFile.getName().replace(".txt", ""));
                    continue;
                }

                if (time.isAfter(NotesHandler.getDateTime(rawFile.getName().replace(".txt", "")))) continue;
                time = NotesHandler.getDateTime(rawFile.getName().replace(".txt", ""));
            }

            notebooks.add(new Notebook(notebookName, time == null ? LocalDateTime.now() : time));
        }

        return notebooks;
    }

}
