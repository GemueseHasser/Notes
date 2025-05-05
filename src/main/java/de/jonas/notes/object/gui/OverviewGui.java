package de.jonas.notes.object.gui;

import de.jonas.notes.constant.ImageType;
import de.jonas.notes.object.Drawable;
import de.jonas.notes.object.Gui;
import de.jonas.notes.object.component.RoundButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Ein {@link OverviewGui} stellt eine Instanz eines {@link Gui Fensters} dar und bietet eine Übersicht an, die aus
 * einem Titel, einem Übersicht-Namen und einer undefinierten Anzahl an Buttons besteht, welche in der Klasse
 * implementiert werden, die dieses {@link OverviewGui} erzeugt. Die Buttons stellen dann Elemente dar, für die diese
 * Übersicht erzeugt wird. In diesem Fall sind das entweder Notizbücher oder Notizen. Der Titel wird allerdings mittig
 * oben auf dem Fenster angezeigt.
 */
public abstract class OverviewGui extends Gui implements Drawable {

    //<editor-fold desc="CONSTANTS">
    /** Die Breite dieses Fensters. */
    public static final int WIDTH = 800;
    /** Die Höhe dieses Fensters. */
    public static final int HEIGHT = 600;
    /** Die Größe des Buttons, um ein neues Element zu dieser Übersicht hinzuzufügen. */
    public static final int CREATE_BUTTON_SIZE = 70;
    /** Der Abstand zur unteren Fensterkante des Buttons, um ein neues Element zu dieser Übersicht hinzuzufügen. */
    public static final int CREATE_BUTTON_MARGIN_BOTTOM = 50;
    /** Die Schriftart, in der der Titel dieser Übersicht dargestellt wird. */
    @NotNull
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    /** Der Abstand der Elemente der Übersicht zur oberen Fensterkante. */
    private static final int BUTTONS_MARGIN_TOP = 40;
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Die Eigenschaften des {@link GridBagLayout}, in dem die Elemente dieser Übersicht angezeigt werden. */
    @NotNull
    protected final GridBagConstraints constraints = new GridBagConstraints();
    /** Das Panel, in dem sich die Elemente (Buttons) dieser Übersicht befinden. */
    @NotNull
    protected final JPanel buttonsPanel = new JPanel(new GridBagLayout());
    /**
     * Das {@link JLayeredPane}, in dem sich diese Übersicht befindet, damit bspw. der Button, um neue Elemente
     * hinzuzufügen, über den Elementen dieser Übersicht angezeigt werden kann.
     */
    @NotNull
    private final JLayeredPane layeredPane = new JLayeredPane();
    /** Der Titel dieses Fensters. */
    @NotNull
    private final String title;
    /** Der Name der Übersicht. */
    @NotNull
    private final String overviewName;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt eine neue Instanz eines {@link OverviewGui}. Ein {@link OverviewGui} stellt eine Instanz eines
     * {@link Gui Fensters} dar und bietet eine Übersicht an, die aus einem Titel, einem Übersicht-Namen und einer
     * undefinierten Anzahl an Buttons besteht, welche in der Klasse implementiert werden, die dieses
     * {@link OverviewGui} erzeugt. Die Buttons stellen dann Elemente dar, für die diese Übersicht erzeugt wird. In
     * diesem Fall sind das entweder Notizbücher oder Notizen. Der Titel wird allerdings mittig oben auf dem Fenster
     * angezeigt.
     *
     * @param title        Der Titel dieses Fensters.
     * @param overviewName Der Name der Übersicht.
     */
    public OverviewGui(
        @NotNull final String title,
        @NotNull final String overviewName
    ) {
        super("", WIDTH, HEIGHT);
        this.addDrawable(this);
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setResizable(false);

        this.title = title;
        this.overviewName = overviewName;
    }
    //</editor-fold>


    /**
     * Die Aktion, die ausgeführt werden soll, wenn der Benutzer auf den Button klickt, um ein neues Element zu dieser
     * Übersicht hinzuzufügen.
     */
    public abstract void createButtonAction();

    /**
     * Fügt alle Elemente zu dem Panel hinzu, die bereits bei Initialisierung dieser Übersicht als Elemente angezeigt
     * werden sollen.
     *
     * @param buttonsPanel Das Panel, in dem sich alle Elemente dieser Übersicht befinden.
     */
    public abstract void loadButtons(@NotNull final JPanel buttonsPanel);

    /**
     * Lädt alle essenziellen Komponenten dieser Übersicht.
     */
    public void loadOverviewGui() {
        layeredPane.setBounds(0, 0, WIDTH, HEIGHT);
        layeredPane.setLayout(null);

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(
            0,
            BUTTONS_MARGIN_TOP,
            WIDTH,
            HEIGHT - 2 * BUTTONS_MARGIN_TOP
        );

        constraints.insets = new Insets(5, 5, 5, 5);

        final RoundButton createButton = new RoundButton(ImageType.ADD_NOTE_ICON.getImage(), 100, this);
        createButton.setBounds(
            WIDTH - (int) (CREATE_BUTTON_SIZE * 1.5),
            HEIGHT - CREATE_BUTTON_SIZE - CREATE_BUTTON_MARGIN_BOTTOM,
            CREATE_BUTTON_SIZE,
            CREATE_BUTTON_SIZE
        );
        createButton.addActionListener(e -> createButtonAction());

        loadButtons(buttonsPanel);
        scrollPane.setViewportView(buttonsPanel);

        layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(createButton, JLayeredPane.PALETTE_LAYER);
        this.add(layeredPane);
    }

    /**
     * Lädt alle Elemente dieser Übersicht neu.
     */
    protected void reloadButtons() {
        buttonsPanel.removeAll();
        loadButtons(buttonsPanel);
        buttonsPanel.revalidate();
        repaint();
    }

    /**
     * Fügt eine Komponente auf der höchsten Ebene dieses Fensters zu dem {@code layeredPane} hinzu, wodurch diese
     * Komponente dann über den Elementen dieser Übersicht angezeigt wird.
     *
     * @param component Die Komponente, die hinzugefügt werden soll.
     */
    protected void addTopLayerComponent(@NotNull final Component component) {
        layeredPane.add(component, JLayeredPane.PALETTE_LAYER);
    }

    //<editor-fold desc="implementation">
    @Override
    public void draw(@NotNull final Graphics2D g) {
        g.setFont(TITLE_FONT);
        g.drawString(
            title,
            this.getWidth() / 2 - g.getFontMetrics().stringWidth(title) / 2,
            5 + g.getFontMetrics().getAscent()
        );
        g.drawLine(0, BUTTONS_MARGIN_TOP - 2, this.getWidth(), BUTTONS_MARGIN_TOP - 2);

        if (buttonsPanel.getComponentCount() > 0) return;

        final String text = "Du hast noch keine " + overviewName + " erstellt.";
        g.drawString(
            text,
            WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2,
            HEIGHT / 2 - g.getFontMetrics().getAscent()
        );
    }
    //</editor-fold>
}
