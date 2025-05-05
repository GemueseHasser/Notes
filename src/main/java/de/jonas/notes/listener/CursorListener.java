package de.jonas.notes.listener;

import de.jonas.notes.object.Gui;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mithilfe dieses Listener wird der {@link Cursor Mauszeiger} für ein bestimmtes {@link Gui Fenster} auf den Mauszeiger
 * gesetzt, der in dem Komponenten gesetzt ist, zu dem dieser Listener hinzugefügt wird.
 */
@RequiredArgsConstructor
public final class CursorListener extends MouseAdapter {

    //<editor-fold desc="LOCAL FIELDS">
    /** Das Fenster, in dem der Mauszeiger aktualisiert werden soll. */
    @NotNull
    private final Gui gui;
    //</editor-fold>


    //<editor-fold desc="implementation">
    @Override
    public void mouseEntered(@NotNull final MouseEvent e) {
        super.mouseEntered(e);

        gui.getRootPane().setCursor(e.getComponent().getCursor());
    }

    @Override
    public void mouseExited(@NotNull final MouseEvent e) {
        super.mouseExited(e);

        gui.getRootPane().setCursor(Cursor.getDefaultCursor());
    }
    //</editor-fold>

}
