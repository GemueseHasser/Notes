package de.jonas.notes.object;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse {@code Gui} repräsentiert ein {@link JFrame} mit eigenem Zeichen-Objekt. Das {@code Gui} enthält ein
 * spezielles Zeichenfeld (implementiert als innere Klasse {@link Draw}), in das alle grafischen Elemente gezeichnet
 * werden können, die über das {@link Drawable}-Interface definiert sind.
 */
public class Gui extends JFrame {

    //<editor-fold desc="LOCAL FIELDS">
    /** Eine Liste aller grafischen Komponenten, die auf das Fenster gezeichnet werden sollen. */
    @NotNull
    private final List<Drawable> drawables = new ArrayList<>();
    /** Das eigene Zeichenfeld des Fensters, in dem alle {@link Drawable} Objekte gezeichnet werden. */
    @NotNull
    private final Draw draw = new Draw();
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt ein neues Fenster (Gui) und implementiert die Basislogik für die Darstellung und Verwaltung
     * von Grafikkomponenten.
     *
     * @param title      Der Titel des Fensters.
     * @param width      Die Breite des Fensters.
     * @param height     Die Höhe des Fensters.
     */
    public Gui(
        @NotNull final String title,
        @Range(from = 0, to = Integer.MAX_VALUE) final int width,
        @Range(from = 0, to = Integer.MAX_VALUE) final int height
    ) {
        super(title);
        super.setSize(width, height);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
        super.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                draw.setSize(
                    e.getComponent().getWidth(),
                    e.getComponent().getHeight()
                );
            }
        });

        draw.setBounds(
            0,
            0,
            width,
            height
        );

        super.add(draw);
    }
    //</editor-fold>


    /**
     * Fügt ein neues grafisches Element hinzu, das durch das Zeichen-Objekt gezeichnet wird.
     *
     * @param drawable Das grafische Element, das gezeichnet werden soll.
     */
    public void addDrawable(@NotNull final Drawable drawable) {
        drawables.add(drawable);
    }

    /**
     * Öffnet das Fenster und zeigt es dem Benutzer an.
     * Diese Methode sollte nach der Erstellung des Fensters aufgerufen werden.
     */
    public void open() {
        super.setVisible(true);
    }


    //<editor-fold desc="Draw">

    /**
     * Das Zeichenfeld des Fensters, in dem alle grafischen Elemente gezeichnet werden.
     * Diese Klasse überschreibt Funktionen von {@link JLabel}, um das Zeichnen zu erleichtern.
     */
    private final class Draw extends JLabel {

        //<editor-fold desc="implementation">
        @Override
        protected void paintComponent(@NotNull final Graphics g) {
            super.paintComponent(g);

            final Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            for (@NotNull final Drawable drawable : drawables) {
                drawable.draw(g2d);
            }
        }
        //</editor-fold>
    }
    //</editor-fold>

}
