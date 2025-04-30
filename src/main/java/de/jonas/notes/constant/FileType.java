package de.jonas.notes.constant;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
public enum FileType {

    RESOURCES("Notes" + File.separator + "Resources"),
    STYLE("Notes" + File.separator + "Style"),
    RAW("Notes" + File.separator + "Raw");


    @NotNull
    private final File file;


    FileType(@NotNull final String fileName) {
        try {
            Files.createDirectories(Paths.get(fileName));
            this.file = new File(fileName);
        } catch (@NotNull final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
