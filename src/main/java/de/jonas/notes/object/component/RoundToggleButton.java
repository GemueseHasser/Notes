package de.jonas.notes.object.component;

import de.jonas.notes.listener.CursorListener;
import de.jonas.notes.object.Gui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.swing.JToggleButton;
import javax.swing.text.StyledEditorKit;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

/**
 * Ein {@link RoundToggleButton} stellt eine Instanz eines {@link JToggleButton} dar, mit dem Unterschied, dass dieser
 * Button abgerundete Ecken hat bzw. dass die Ecken auch so abgerundet sein können, dass der Button rund ist.
 */
public final class RoundToggleButton extends JToggleButton {

    //<editor-fold desc="CONSTANTS">
    /** Die Hintergrundfarbe, die der Button annimmt, sobald er ausgewählt wurde. */
    @NotNull
    private static final Color SELECT_COLOR = Color.ORANGE;
    //</editor-fold>


    //<editor-fold desc="LOCAL FIELDS">
    /** Die Größe der abgerundeten Ecken dieses Buttons. */
    private final int rounding;
    /** Die Form dieses Buttons. */
    private Shape shape;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt eine neue Instanz eines {@link RoundToggleButton}. Ein {@link RoundToggleButton} stellt eine Instanz
     * eines {@link JToggleButton} dar, mit dem Unterschied, dass dieser Button abgerundete Ecken hat bzw. dass die
     * Ecken auch so abgerundet sein können, dass der Button rund ist.
     *
     * @param textAction Die {@link javax.swing.text.StyledEditorKit.StyledTextAction Aktion}, die ausgeführt wird,
     *                   solange der Button ausgewählt ist.
     * @param rounding   Die Größe der Rundungen der Ecken des Buttons.
     */
    public RoundToggleButton(
        @NotNull final StyledEditorKit.StyledTextAction textAction,
        @Range(from = 0, to = Integer.MAX_VALUE) final int rounding,
        @NotNull final Gui parent
    ) {
        super(textAction);
        this.rounding = rounding;

        super.setContentAreaFilled(false);
        super.setFocusable(false);
        super.setBackground(Color.LIGHT_GRAY);
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

        g2d.setColor(this.isSelected() ? SELECT_COLOR : getBackground());
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, rounding, rounding));

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(@NotNull final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setColor(this.isSelected() ? SELECT_COLOR : getBackground());
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
