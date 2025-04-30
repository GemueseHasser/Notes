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

public final class PdfHandler {

    public static void saveImageAsPdf(
        @NotNull final BufferedImage image,
        @NotNull final File outputFile
    ) {
        try (final PdfWriter pdfWriter = new PdfWriter(outputFile)) {
            final PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            final Document document = new Document(pdfDocument, PageSize.A4);

            final int pageWidth = (int) PageSize.A4.getWidth();
            final int pageHeight = (int) (PageSize.A4.getHeight() - document.getTopMargin() - document.getBottomMargin());

            final int pageAmount = image.getHeight() / pageHeight + 1;

            for (int i = 0; i < pageAmount; i++) {
                final int remainingHeight = image.getHeight() - (i * pageHeight);
                final int currentImageHeight = Math.min(remainingHeight, pageHeight);
                final BufferedImage currentPageImage = image.getSubimage(0, i * pageHeight, pageWidth, currentImageHeight);
                final ImageData imageData = ImageDataFactory.create(currentPageImage, Color.WHITE);
                final Image pdfImage = new Image(imageData);

                document.add(pdfImage);

                if (i < pageAmount - 1) document.add(new AreaBreak());
            }

            document.close();
        } catch (@NotNull final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
