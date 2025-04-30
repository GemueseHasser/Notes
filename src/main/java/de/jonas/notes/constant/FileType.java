package de.jonas.notes.constant;

import de.jonas.notes.object.Notebook;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RequiredArgsConstructor
public enum FileType {

    RESOURCES("Resources"),
    STYLE("Style"),
    RAW("Raw");


    @NotNull
    private final String fileName;


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
