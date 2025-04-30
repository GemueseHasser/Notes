package de.jonas.notes;

import de.jonas.notes.object.gui.OverviewGui;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.swing.UIManager;

public class Notes {

    @Getter
    private static OverviewGui overviewGui;


    @SneakyThrows
    public static void main(@NotNull final String @NotNull [] args) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        overviewGui = new OverviewGui();
        overviewGui.open();
    }

}
