package com.example.enrolclientapi.Util;

import com.example.enrolclientapi.dto.ClientDTO;
import com.example.enrolclientapi.exceptions.ClientException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DocumentGeneratorUtil {

    private static final String BANK_NAME = "BT";

    public static InputStreamResource generateEnrolmentDocument(ClientDTO clientDTO) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Add title
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Enrolment Document");
                contentStream.endText();

                // Add bank details
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 650);
                contentStream.showText("Bank Name: " + BANK_NAME + "; ");
                contentStream.showText("Bank Signature: " );
                contentStream.endText();

                // Add client details
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 600);
                contentStream.showText("Client Name: " + clientDTO.getFirstName() + " " +clientDTO.getLastName() + "; ");
                contentStream.showText("Client Signature: " );
                contentStream.endText();
            }

            // Save the PDF to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            byteArrayOutputStream.close();

            // Convert byte array to InputStreamResource
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            return new InputStreamResource(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClientException("Failed to generate enrolment document for client!");
        }
    }

    public static InputStreamResource generateDenialDocument(ClientDTO clientDTO) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Add title
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Denial Document; ");
                contentStream.showText("Reason: Risky candidate");
                contentStream.endText();

                // Add bank details
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 650);
                contentStream.showText("Bank Name: " + BANK_NAME + "; ");
                contentStream.showText("Bank Signature: " );
                contentStream.endText();

                // Add client details
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 600);
                contentStream.showText("Client Name: " + clientDTO.getFirstName() + " " + clientDTO.getLastName() + "; ");
                contentStream.showText("Client Signature: " );
                contentStream.endText();
            }

            // Save the PDF to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            byteArrayOutputStream.close();

            // Convert byte array to InputStreamResource
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            return new InputStreamResource(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClientException("Failed to generate denial document for client!");
        }
    }
}
