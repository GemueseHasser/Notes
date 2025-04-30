package de.jonas.notes;

import de.jonas.notes.object.Notebook;
import de.jonas.notes.object.gui.NoteOverviewGui;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.swing.UIManager;
import java.time.LocalDateTime;

public class Notes {

    @SneakyThrows
    public static void main(@NotNull final String @NotNull [] args) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        final NoteOverviewGui overviewGui = new NoteOverviewGui(new Notebook("Test-Notebook", LocalDateTime.now()));
        overviewGui.open();
    }

}
