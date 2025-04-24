package de.jonas.notes.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public final class Note {

    @NotNull
    private final String title;
    @NotNull
    private final LocalDateTime dateTime;
    @NotNull
    private final List<String> lines;
    @NotNull
    private final TextStyleInformation textStyleInformation = new TextStyleInformation();

}
