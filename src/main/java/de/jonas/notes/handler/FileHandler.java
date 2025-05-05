package de.jonas.notes.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;

/**
 * Mithilfe des {@link FileHandler} werden alle Interaktionen mit Dateien außerhalb dieses Projekts geregelt.
 */
public final class FileHandler {

    //<editor-fold desc="utility">

    /**
     * Öffnet dem Benutzer ein Fenster, in welchem er eine Datei auswählen kann, die dann zurückgegeben wird. Dabei
     * werden nur Dateien angezeigt, die die entsprechende Endung hat. Wählt man eine Datei ganz ohne Endung aus, wird
     * die erste Endung der übergebenen Endungen automatisch dem Dateinamen beigefügt.
     *
     * @param title       Der Titel dieses Fensters, in dem man die Datei auswählen kann.
     * @param approveText Der Text auf dem Button, mit dem man die Auswahl bestätigen kann.
     * @param extensions  Alle Dateiendungen, welche man auswählen kann (Wählt man eine Datei ohne Endung, wird die
     *                    erste Dateiendung automatisch angefügt).
     *
     * @return Die ausgewählte Datei des Benutzers.
     */
    @Nullable
    public static File getFile(
        @NotNull final String title,
        @NotNull final String approveText,
        @NotNull final String @NotNull [] extensions
    ) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle(title);
        fileChooser.setAcceptAllFileFilterUsed(false);

        final StringBuilder extensionTextBuilder = new StringBuilder();
        for (@NotNull final String extension : extensions) {
            extensionTextBuilder.append("*.").append(extension);
        }

        fileChooser.setFileFilter(new FileNameExtensionFilter(extensionTextBuilder.toString(), extensions));
        fileChooser.setMultiSelectionEnabled(false);

        final int result = fileChooser.showDialog(null, approveText);

        if (result == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileChooser.getSelectedFile();

            for (@NotNull final String extension : extensions) {
                if (selectedFile.getName().endsWith(extension)) return selectedFile;
            }

            return new File(selectedFile + "." + extensions[0]);
        }

        return null;
    }

    /**
     * Löscht einen ganzen Ordner mitsamt aller Dateien, die sich in diesem Ordner befinden.
     *
     * @param file Der Ordner, der gelöscht werden soll. Wenn dieser Ordner eine Datei ist, wird einfach nur die Datei
     *             gelöscht.
     */
    public static void deleteDirectory(@NotNull final File file) {
        if (file.isDirectory()) {
            for (@NotNull final File subFile : file.listFiles()) {
                deleteDirectory(subFile);
            }
        }

        file.delete();
    }
    //</editor-fold>

}
