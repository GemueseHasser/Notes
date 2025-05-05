package de.jonas.notes.object;

import de.jonas.notes.constant.TextStyleType;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stellt eine Style-Information dar, die alle Formatierungen in einer Notiz beinhaltet. Darunter die Schriftart, in der
 * diese Notiz verfasst wurde, alle Formatierungen, die individuell an dem Text vorgenommen wurden und alle Bilder, die
 * in der Notiz eingefügt wurden.
 */
@Getter
public final class TextStyleInformation {

    //<editor-fold desc="LOCAL FIELDS">
    /** Die Schriftart, in der diese Notiz verfasst wurde - Standard 'Monospaced'. */
    @Setter
    @NotNull
    private String fontFamily = "Monospaced";
    /** Alle individuellen Formatierungen, die durch den Benutzer vorgenommen wurden. */
    @NotNull
    private final Map<TextStyleType, List<Integer>> styles = new HashMap<>();
    /** Alle Bilder, die der Nutzer einer Notiz hinzugefügt hat. */
    @NotNull
    private final Map<Integer, File> images = new HashMap<>();
    //</editor-fold>

}
