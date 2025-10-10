package com.example.pdf;

import com.example.examplefeature.Task;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFGenerator {

    public static byte[] generateTaskListPDF(List<Task> tasks) {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
                contentStream.setLeading(20f);
                contentStream.newLineAtOffset(50, 700);

                contentStream.showText("Lista de Tasks:");
                contentStream.newLine();
                contentStream.newLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

                for (Task task : tasks) {
                    contentStream.showText("Descrição: " + task.getDescription());
                    contentStream.newLine();
                    contentStream.showText("Data de Criação: " + formatter.format(task.getCreationDate()));
                    contentStream.newLine();
                    contentStream.showText("Data Limite: " + (task.getDueDate() != null ? task.getDueDate() : "Nunca"));
                    contentStream.newLine();
                    contentStream.newLine();
                }

                contentStream.endText();
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}

