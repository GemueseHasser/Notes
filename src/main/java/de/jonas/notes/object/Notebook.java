package de.jonas.notes.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public final class Notebook {

    @NotNull
    private final String name;
    @Setter
    @NotNull
    private LocalDateTime lastAccess;

}
