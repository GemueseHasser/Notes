package de.jonas.notes.object;

import org.jetbrains.annotations.NotNull;

import java.awt.Graphics2D;

/**
 * Ein {@link Drawable} stellt eine Schnittstelle dar, welche zu einem {@link Gui} hinzugefügt werden kann, um
 * grafische Komponenten auf das Gui zu zeichnen.
 */
public interface Drawable {

    /**
     * Zeichnet alle grafischen Komponenten auf das Gui, zu dem dieses {@link Drawable} hinzugefügt wurde.
     *
     * @param g Das Grafik-Objekt, welches genutzt wird, um alle Grafiken auf das Fenster zu zeichnen.
     */
    void draw(@NotNull final Graphics2D g);

}
