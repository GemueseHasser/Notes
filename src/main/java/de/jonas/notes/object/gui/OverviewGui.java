package de.jonas.notes.object.gui;

import de.jonas.notes.constant.ImageType;
import de.jonas.notes.object.Drawable;
import de.jonas.notes.object.Gui;
import de.jonas.notes.object.component.RoundButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public abstract class OverviewGui extends Gui implements Drawable {

    public static final int CREATE_BUTTON_SIZE = 70;
    @NotNull
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BUTTONS_MARGIN_TOP = 40;
    private static final int CREATE_BUTTON_MARGIN_BOTTOM = 50;


    @NotNull
    protected final GridBagConstraints constraints = new GridBagConstraints();
    @NotNull
    protected final JPanel buttonsPanel = new JPanel(new GridBagLayout());
    @NotNull
    private final String title;
    @NotNull
    private final String overviewName;


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


    public abstract void createButtonAction();

    public abstract void loadButtons(@NotNull final JPanel buttonsPanel);

    public void loadOverviewGui() {
        final JLayeredPane layeredPane = new JLayeredPane();
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

        final RoundButton createButton = new RoundButton("", 100, this);
        createButton.setBounds(
            WIDTH - (int) (CREATE_BUTTON_SIZE * 1.5),
            HEIGHT - CREATE_BUTTON_SIZE - CREATE_BUTTON_MARGIN_BOTTOM,
            CREATE_BUTTON_SIZE,
            CREATE_BUTTON_SIZE
        );
        createButton.setIcon(new ImageIcon(ImageType.ADD_NOTE_ICON.getImage()));
        createButton.setFont(TITLE_FONT.deriveFont(15F));
        createButton.addActionListener(e -> createButtonAction());

        loadButtons(buttonsPanel);
        scrollPane.setViewportView(buttonsPanel);

        layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(createButton, JLayeredPane.PALETTE_LAYER);
        this.add(layeredPane);
    }

    public void reloadButtons() {
        buttonsPanel.removeAll();
        loadButtons(buttonsPanel);
        buttonsPanel.revalidate();
        repaint();
    }

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
}
