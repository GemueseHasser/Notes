package de.jonas.notes.object.component;

import de.jonas.notes.listener.CursorListener;
import de.jonas.notes.object.Gui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Ein {@link RoundButton} stellt eine Instanz eines {@link JButton Buttons} dar, mit dem Unterschied, dass dieser
 * Button abgerundete Ecken hat bzw. dass die Ecken auch so abgerundet sein können, dass der Button rund ist.
 */
public final class RoundButton extends JButton {

    //<editor-fold desc="CONSTANTS">
    /** Die Hintergrundfarbe, die der Button in dem Moment annimmt, während er angeklickt wird. */
    @NotNull
    private static final Color CLICK_COLOR = Color.ORANGE;
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Die Größe der abgerundeten Ecken dieses Buttons. */
    private final int rounding;
    /** Die Form dieses Buttons. */
    private Shape shape;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt eine neue Instanz eines {@link RoundButton}. Ein {@link RoundButton} stellt eine Instanz eines
     * {@link JButton Buttons} dar, mit dem Unterschied, dass dieser Button abgerundete Ecken hat bzw. dass die Ecken
     * auch so abgerundet sein können, dass der Button rund ist.
     *
     * @param text     Der Text, der auf diesem Button angezeigt wird.
     * @param rounding Die Größe der Rundungen der Ecken des Buttons.
     */
    public RoundButton(
        @NotNull final String text,
        @Range(from = 0, to = Integer.MAX_VALUE) final int rounding,
        @NotNull final Gui parent
    ) {
        super(text);
        this.rounding = rounding;

        super.setContentAreaFilled(false);
        super.setFocusable(false);
        super.setBackground(Color.LIGHT_GRAY);
        super.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        super.addMouseListener(new CursorListener(parent));
    }

    public RoundButton(
        @NotNull final BufferedImage image,
        @Range(from = 0, to = Integer.MAX_VALUE) final int rounding,
        @NotNull final Gui parent
    ) {
        super();
        this.rounding = rounding;

        super.setIcon(new ImageIcon(image));
        super.setContentAreaFilled(false);
        super.setFocusable(false);
        super.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        super.addMouseListener(new CursorListener(parent));
    }
    //</editor-fold>


    //<editor-fold desc="implementation">
    @Override
    protected void paintComponent(@NotNull final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setColor(getModel().isArmed() ? CLICK_COLOR : getBackground());
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, rounding, rounding));

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(@NotNull final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setColor(getModel().isArmed() ? CLICK_COLOR : getBackground());
        g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, rounding, rounding));
    }

    @Override
    public boolean contains(
        @Range(from = 0, to = Integer.MAX_VALUE) final int x,
        @Range(from = 0, to = Integer.MAX_VALUE) final int y
    ) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            this.shape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), rounding, rounding);
        }

        return shape.contains(x, y);
    }
    //</editor-fold>

}
