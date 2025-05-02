package de.jonas.notes.object;

import de.jonas.notes.constant.TextStyleType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public final class TextStyleInformation {


    @NotNull
    private final Map<TextStyleType, List<Integer>> styles = new HashMap<>();
    @NotNull
    private final Map<Integer, File> images = new HashMap<>();

}
