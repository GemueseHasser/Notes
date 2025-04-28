package de.jonas.notes.listener;

import de.jonas.notes.object.Gui;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@RequiredArgsConstructor
public final class CursorListener extends MouseAdapter {

    @NotNull
    private final Gui gui;


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

}
