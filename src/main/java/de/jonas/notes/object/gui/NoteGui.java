package de.jonas.notes.object.gui;

import de.jonas.notes.Notes;
import de.jonas.notes.constant.FileType;
import de.jonas.notes.constant.ImageType;
import de.jonas.notes.constant.TextStyleType;
import de.jonas.notes.handler.FileHandler;
import de.jonas.notes.handler.NotebookHandler;
import de.jonas.notes.handler.NotesHandler;
import de.jonas.notes.handler.PdfHandler;
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

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
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
    @NotNull
    private final OverviewGui overviewGui;


    public NoteGui(
        @NotNull final OverviewGui overviewGui,
        @NotNull final Note note
    ) throws BadLocationException {
        super(note.getTitle(), WIDTH, HEIGHT);
        this.addDrawable(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(5, 5));

        this.overviewGui = overviewGui;
        this.note = note;

        final JPanel titlePanel = new JPanel(new GridLayout(2, 1));

        final JTextField titleField = new JTextField(note.getTitle());
        titleField.setHorizontalAlignment(JTextField.CENTER);
        titleField.setFont(TITLE_FONT);
        titleField.setPreferredSize(new Dimension(WIDTH, 50));
        titleField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        titleField.addMouseListener(new CursorListener(this));

        final JToolBar utilityToolbar = new JToolBar();
        utilityToolbar.addSeparator(new Dimension(30, 0));
        utilityToolbar.setFloatable(false);

        final RoundButton insertButton = new RoundButton("Bild einfügen", 10, this);
        insertButton.addActionListener(e -> {
            final File rawImageFile = FileHandler.getFile(
                "Bild auswählen...",
                "Auswählen",
                new String[]{"png", "jpg"}
            );
            assert rawImageFile != null;
            final BufferedImage rawImage;
            try {
                rawImage = ImageIO.read(rawImageFile);
            } catch (@NotNull final IOException | IllegalArgumentException ignored) {
                return;
            }

            final JTextField widthField = new JTextField(5);
            final JTextField heightField = new JTextField(5);

            final JPanel imageSizePanel = new JPanel();
            imageSizePanel.add(new JLabel("Breite: "));
            imageSizePanel.add(widthField);
            imageSizePanel.add(new JLabel("Höhe: "));
            imageSizePanel.add(heightField);

            final int insertImageResult = JOptionPane.showOptionDialog(
                null,
                imageSizePanel,
                "Bild einfügen",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                new ImageIcon(ImageType.INSERT_IMAGE_ICON.getImage()),
                new String[]{"Einfügen", "Abbrechen"},
                "Einfügen"
            );

            if (insertImageResult == 1) return;

            final int width = Integer.parseInt(widthField.getText());
            final int height = Integer.parseInt(heightField.getText());
            final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D graphics = image.createGraphics();
            graphics.drawImage(rawImage, 0, 0, width, height, null);
            graphics.dispose();

            final File imageFile = new File(
                FileType.RESOURCES.getFile(note.getParentNotebook()) + File.separator + rawImageFile.getName()
            );
            try {
                ImageIO.write(image, "png", imageFile);
            } catch (@NotNull final IOException ex) {
                throw new RuntimeException(ex);
            }

            final int position = textPane.isFocusOwner() ? textPane.getCaretPosition() : textPane.getText().length();
            note.getTextStyleInformation().getImages().put(position, imageFile);
            textPane.insertIcon(new ImageIcon(image));
        });

        utilityToolbar.add(insertButton);

        titlePanel.add(titleField);
        titlePanel.add(utilityToolbar);

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

        // load images
        for (@NotNull final Map.Entry<Integer, File> imageEntry : note.getTextStyleInformation().getImages().entrySet()) {
            try {
                final int position = imageEntry.getKey();
                final BufferedImage image = ImageIO.read(imageEntry.getValue());

                final String before = textPane.getText().substring(0, position);
                final String after = textPane.getText().substring(position);

                textPane.setText(before);
                textPane.insertIcon(new ImageIcon(image));
                textPane.getDocument().insertString(position + 1, after, null);
            } catch (@NotNull final IOException ignored) {
            }
        }

        final StyledDocument document = textPane.getStyledDocument();

        // format text
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

        final JToolBar styleToolbar = new JToolBar(JToolBar.VERTICAL);
        styleToolbar.setFloatable(false);

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
            styleToolbar.add(toggleButton);
            styleToolbar.addSeparator();
        }

        final JPanel noteOptionPanel = getNoteOptionPanel(titleField);

        this.add(titlePanel, BorderLayout.PAGE_START);
        this.add(scrollTextPane, BorderLayout.CENTER);
        this.add(noteOptionPanel, BorderLayout.SOUTH);
        this.add(styleToolbar, BorderLayout.WEST);
        this.pack();
    }

    @NotNull
    private JPanel getNoteOptionPanel(@NotNull final JTextField titleField) {
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
            final BufferedImage textImage = new BufferedImage(
                textPane.getWidth(),
                textPane.getHeight(),
                BufferedImage.TYPE_INT_ARGB
            );
            final Graphics2D textImageGraphics = Notes.getImageGraphics(textImage);
            textPane.paint(textImageGraphics);
            textImageGraphics.dispose();

            try {
                PdfHandler.saveImageAsPdf(textImage, exportFile);
            } catch (@NotNull final IllegalArgumentException ignored) {
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
                new ArrayList<>(Arrays.asList(textPane.getText().split("\n"))),
                note.getParentNotebook()
            );

            // imitate button unselect
            for (@NotNull final RoundToggleButton styleButton : styleButtons) {
                if (!styleButton.isSelected()) continue;
                styleButton.setSelected(false);
                ((TextStyleInteractListener) styleButton.getActionListeners()[0]).interactAction();
            }

            final List<Integer> removedImagePositions = new ArrayList<>();

            // check removed images
            for (final int imagePosition : note.getTextStyleInformation().getImages().keySet()) {
                final Element element = textPane.getStyledDocument().getCharacterElement(imagePosition);
                final Icon icon = StyleConstants.getIcon(element.getAttributes());

                if (icon == null) {
                    removedImagePositions.add(imagePosition);
                }
            }

            // remove removed images
            for (final int imagePosition : removedImagePositions) {
                note.getTextStyleInformation().getImages().remove(imagePosition);
            }

            NotesHandler.saveNote(newNote);
            TextStyleHandler.saveTextStyle(newNote, note.getTextStyleInformation());
            NotesHandler.deleteNote(note);
            TextStyleHandler.deleteTextStyle(note);
            overviewGui.reloadButtons();

            NotebookHandler.setLastAccessTimestamp(note.getParentNotebook().getName());

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
                new ImageIcon(ImageType.DELETE_ICON.getImage())
            );

            if (delete == JOptionPane.NO_OPTION) return;

            if (NotesHandler.deleteNote(note)) {
                overviewGui.reloadButtons();
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
