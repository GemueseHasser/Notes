package de.jonas.notes.object.gui;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import de.jonas.notes.Notes;
import de.jonas.notes.constant.ImageType;
import de.jonas.notes.constant.TextStyleType;
import de.jonas.notes.handler.FileHandler;
import de.jonas.notes.handler.NotesHandler;
import de.jonas.notes.handler.TextStyleHandler;
import de.jonas.notes.listener.CursorListener;
import de.jonas.notes.listener.TextStyleInteractListener;
import de.jonas.notes.object.Drawable;
import de.jonas.notes.object.Gui;
import de.jonas.notes.object.Note;
import de.jonas.notes.object.TextStyleInformation;
import de.jonas.notes.object.component.RoundButton;
import de.jonas.notes.object.component.RoundToggleButton;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class NoteGui extends Gui implements Drawable {

    @NotNull
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 22);
    @NotNull
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 18);
    private static final int WIDTH = 700;
    private static final int HEIGHT = 550;


    @Getter
    @NotNull
    private final List<RoundToggleButton> styleButtons = new ArrayList<>();
    @Getter
    @NotNull
    private final JTextPane textPane = new JTextPane();
    @NotNull
    private final JScrollPane scrollTextPane = new JScrollPane(textPane);
    @Getter
    @NotNull
    private final Note note;


    public NoteGui(@NotNull final Note note) throws BadLocationException {
        super(note.getTitle(), WIDTH, HEIGHT);
        this.addDrawable(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(5, 5));

        this.note = note;

        final JTextField titleField = new JTextField(note.getTitle());
        titleField.setHorizontalAlignment(JTextField.CENTER);
        titleField.setFont(TITLE_FONT);
        titleField.setPreferredSize(new Dimension(WIDTH, 50));
        titleField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        titleField.addMouseListener(new CursorListener(this));

        textPane.setFont(TEXT_FONT);
        textPane.setBorder(null);
        textPane.setPreferredSize(new Dimension(WIDTH, HEIGHT - 50));
        textPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        textPane.addMouseListener(new CursorListener(this));
        textPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent e) {
                super.focusLost(e);
                repaint();
            }
        });

        // load plain text
        for (@NotNull final String line : note.getLines()) {
            textPane.setText(textPane.getText() + line + "\n");
        }

        // format text
        final StyledDocument document = textPane.getStyledDocument();

        for (@NotNull final Map.Entry<TextStyleType, LinkedList<TextStyleInformation.StyleInformation>> styleEntry : note.getTextStyleInformation().getStyles().entrySet()) {
            final TextStyleType styleType = styleEntry.getKey();

            for (@NotNull final TextStyleInformation.StyleInformation styleInformation : styleEntry.getValue()) {
                for (int i = styleInformation.getStartPosition(); i < styleInformation.getEndPosition(); i++) {
                    final Style tempStyle = document.getStyle("" + i);
                    final Style style = tempStyle == null ? document.addStyle("" + i, null) : tempStyle;
                    styleType.expandStyle(style);

                    final String currentChar = document.getText(i, 1);
                    document.remove(i, 1);
                    document.insertString(i, currentChar, style);
                }
            }
        }

        if (note.getLines().isEmpty()) textPane.setText("");

        final JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        toolBar.setFloatable(false);

        for (@NotNull final TextStyleType styleType : TextStyleType.values()) {
            final RoundToggleButton toggleButton = new RoundToggleButton(styleType.getStyledTextAction(), 10, this);
            toggleButton.setText(styleType.getText());
            final TextStyleInteractListener textStyleInteractListener = new TextStyleInteractListener(
                this,
                styleType,
                toggleButton
            );
            toggleButton.addActionListener(textStyleInteractListener);

            // select default font size
            if (styleType == TextStyleType.H5) {
                toggleButton.setSelected(true);
                textStyleInteractListener.interactAction();
                toggleButton.setSelected(true);
            }

            styleButtons.add(toggleButton);
            toolBar.add(toggleButton);
            toolBar.addSeparator();
        }

        final JPanel noteOptionPanel = getNoteOptionPanel(titleField);

        this.add(titleField, BorderLayout.PAGE_START);
        this.add(scrollTextPane, BorderLayout.CENTER);
        this.add(noteOptionPanel, BorderLayout.SOUTH);
        this.add(toolBar, BorderLayout.WEST);
        this.pack();
    }

    @NotNull
    private JPanel getNoteOptionPanel(final JTextField titleField) {
        final JPanel noteOptionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        final RoundButton saveButton = getSaveButton(titleField);
        final RoundButton deleteButton = getDeleteButton();
        final RoundButton exportButton = getExportButton();

        noteOptionPanel.add(saveButton);
        noteOptionPanel.add(deleteButton);
        noteOptionPanel.add(exportButton);
        return noteOptionPanel;
    }

    @NotNull
    private RoundButton getExportButton() {
        final RoundButton exportButton = new RoundButton("Exportieren", 10, this);
        exportButton.addActionListener(e -> {
            final File exportFile = FileHandler.getFile(
                "Exportieren als PDF...",
                "Exportieren",
                new String[]{"pdf"}
            );
            assert exportFile != null;
            try (final PdfWriter pdfWriter = new PdfWriter(exportFile)) {
                final PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                final Document document = new Document(pdfDocument);

                final BufferedImage textImage = new BufferedImage(
                    textPane.getWidth(),
                    textPane.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
                );
                final Graphics2D g = textImage.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                textPane.paint(g);
                g.dispose();

                final ImageData imageData = ImageDataFactory.create(textImage, Color.WHITE);
                final Image image = new Image(imageData);

                document.add(image);
                document.close();
            } catch (@NotNull final IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return exportButton;
    }

    @NotNull
    private RoundButton getSaveButton(@NotNull final JTextField titleField) {
        final RoundButton saveButton = new RoundButton("Speichern", 10, this);
        saveButton.addActionListener(e -> {
            final Note newNote = new Note(
                titleField.getText(),
                LocalDateTime.now(),
                new ArrayList<>(Arrays.asList(textPane.getText().split("\n")))
            );

            // imitate button unselect
            for (@NotNull final RoundToggleButton styleButton : styleButtons) {
                if (!styleButton.isSelected()) continue;
                styleButton.setSelected(false);
                ((TextStyleInteractListener) styleButton.getActionListeners()[0]).interactAction();
            }

            NotesHandler.saveNote(newNote);
            TextStyleHandler.saveTextStyle(newNote, note.getTextStyleInformation());
            NotesHandler.deleteNote(note);
            TextStyleHandler.deleteTextStyle(note);
            Notes.getOverviewGui().reloadNotes();

            this.dispose();
        });
        return saveButton;
    }

    @NotNull
    private RoundButton getDeleteButton() {
        final RoundButton deleteButton = new RoundButton("Löschen", 10, this);
        deleteButton.addActionListener(e -> {
            final int delete = JOptionPane.showConfirmDialog(
                null,
                "Möchtest du diese Notiz wirklich löschen?",
                "Notiz löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(ImageType.DELETE_NOTE_ICON.getImage())
            );

            if (delete == JOptionPane.NO_OPTION) return;

            if (NotesHandler.deleteNote(note)) {
                Notes.getOverviewGui().reloadNotes();
            }

            TextStyleHandler.deleteTextStyle(note);
            this.dispose();
        });
        return deleteButton;
    }


    //<editor-fold desc="implementation">
    @Override
    public void draw(@NotNull final Graphics2D g) {
        if (!textPane.getText().trim().isEmpty()) return;

        g.setFont(TEXT_FONT);
        g.drawString(
            "Notizen hinzufügen...",
            scrollTextPane.getX() + 10,
            scrollTextPane.getY() + g.getFontMetrics().getAscent()
        );
    }
    //</editor-fold>
}
