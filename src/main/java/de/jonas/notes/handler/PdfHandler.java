package de.jonas.notes.handler;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Mithilfe dieses Handlers lässt sich ein Bild als PDF-Datei abspeichern. Dazu wird die IText-Core Bibliothek
 * verwendet.
 */
public final class PdfHandler {

    //<editor-fold desc="utility">

    /**
     * Speichert ein Bild als PDF-Datei ab. Dazu wird die IText-Core Bibliothek verwendet. Sollte das Bild höher sein
     * als eine DIN-A4 Seite, wird dieses Bild in mehrere Bilder unterteilt, die auf die Seiten passen.
     *
     * @param image      Das Bild, welches in dem PDF-Dokument dargestellt wird.
     * @param outputFile Die Datei, in der dieses PDF-Dokument gespeichert werden soll.
     */
    public static void saveImageAsPdf(
        @NotNull final BufferedImage image,
        @NotNull final File outputFile
    ) {
        // create pdf writer
        try (final PdfWriter pdfWriter = new PdfWriter(outputFile)) {
            // create new pdf document
            final PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            final Document document = new Document(pdfDocument, PageSize.A4);

            final int pageWidth = (int) PageSize.A4.getWidth();
            final int pageHeight = (int) (PageSize.A4.getHeight() - document.getTopMargin() - document.getBottomMargin());

            // calculate page amount
            final int pageAmount = image.getHeight() / pageHeight + 1;

            // render image on pages
            for (int i = 0; i < pageAmount; i++) {
                final int remainingHeight = image.getHeight() - (i * pageHeight);
                final int currentImageHeight = Math.min(remainingHeight, pageHeight);
                final BufferedImage currentPageImage = image.getSubimage(
                    0,
                    i * pageHeight,
                    pageWidth,
                    currentImageHeight
                );

                // create pdf compatible image
                final ImageData imageData = ImageDataFactory.create(currentPageImage, Color.WHITE);
                final Image pdfImage = new Image(imageData);

                document.add(pdfImage);

                if (i < pageAmount - 1) document.add(new AreaBreak());
            }

            // close document
            document.close();
        } catch (@NotNull final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    //</editor-fold>

}
