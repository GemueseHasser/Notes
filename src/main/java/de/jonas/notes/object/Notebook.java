package de.jonas.notes.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public final class Notebook {

    @NotNull
    private final String name;
    @NotNull
    private final LocalDateTime dateTime;

}
