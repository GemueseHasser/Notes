package de.jonas.notes.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * Ein Notizbuch, welches aus einem Namen und einem Zeitpunkt besteht, zu dem dieses Notizbuch (eine Notiz im Notizbuch)
 * zuletzt bearbeitet wurde.
 */
@Getter
@RequiredArgsConstructor
public final class Notebook {

    /** Der Name dieses Notizbuchs. */
    @NotNull
    private final String name;
    /** Der Zeitpunkt, zu dem dieses Notizbuch bzw. eine Notiz im Notizbuch zuletzt bearbeitet wurde. */
    @Setter
    @NotNull
    private LocalDateTime lastAccess;

}
