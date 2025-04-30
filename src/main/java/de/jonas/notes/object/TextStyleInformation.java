package de.jonas.notes.object;

import de.jonas.notes.constant.TextStyleType;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Getter
public final class TextStyleInformation {


    @NotNull
    private final Map<TextStyleType, LinkedList<StyleInformation>> styles = new HashMap<>();
    @NotNull
    private final Map<Integer, File> images = new HashMap<>();


    @Getter
    @Setter
    public static final class StyleInformation {
        private int startPosition;
        private int endPosition;
    }

}
