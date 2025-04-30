package de.jonas.notes;

import de.jonas.notes.object.Notebook;
import de.jonas.notes.object.gui.OverviewGui;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.swing.UIManager;
import java.time.LocalDateTime;

public class Notes {

    @SneakyThrows
    public static void main(@NotNull final String @NotNull [] args) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        final OverviewGui overviewGui = new OverviewGui(new Notebook("Test-Notebook", LocalDateTime.now()));
        overviewGui.open();
    }

}
