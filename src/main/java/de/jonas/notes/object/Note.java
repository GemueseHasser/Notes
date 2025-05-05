package de.jonas.notes.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Stellt eine Notiz dar, die aus einem Titel, einem Zeitpunkt, zu dem diese Notiz zuletzt bearbeitet wurde, einer Liste
 * an Zeilen (Text), Style-Informationen (Formatierungen) und einem Notizbuch, in dem sich diese Notiz befindet,
 * besteht.
 */
@Getter
@RequiredArgsConstructor
public final class Note {

    //<editor-fold desc="LOCAL FIELDS">
    /** Der Titel dieser Notiz. */
    @NotNull
    private final String title;
    /** Der Zeitpunkt, zu dem diese Notiz zuletzt bearbeitet wurde. */
    @NotNull
    private final LocalDateTime dateTime;
    /** Der Text (die Zeilen) aus denen diese Notiz besteht. */
    @NotNull
    private final List<String> lines;
    /** Die Style-Informationen (Formatierungen), die diese Notiz besitzt. */
    @NotNull
    private final TextStyleInformation textStyleInformation = new TextStyleInformation();
    /** Das Notizbuch, in dem sich diese Notiz befindet. */
    @NotNull
    private final Notebook parentNotebook;
    //</editor-fold>

}
