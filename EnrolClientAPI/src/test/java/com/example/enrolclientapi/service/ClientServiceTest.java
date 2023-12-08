package com.example.enrolclientapi.service;

import com.example.enrolclientapi.accessors.ClientCheckAPI;
import com.example.enrolclientapi.accessors.ClientReputationAPI;
import com.example.enrolclientapi.dto.ClientDTO;
import com.example.enrolclientapi.exceptions.ClientException;
import com.example.enrolclientapi.service.impl.ClientServiceImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientCheckAPI clientCheckAPI;

    @Mock
    private ClientReputationAPI clientReputationAPI;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void enrolClient_invalidCNP_ThrowsExceptopm(){
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setCnp(1L);
        assertThatThrownBy(() -> clientService.enrolClient(clientDTO))
                .isInstanceOf(ClientException.class);
    }

    @Test
    void enrolClient_idExpired_ThrowsExceptopm(){
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setExpirationDate(LocalDateTime.now().minusDays(1));
        assertThatThrownBy(() -> clientService.enrolClient(clientDTO))
                .isInstanceOf(ClientException.class);
    }

    @Test
    void enrolClient_validClient_GenerateEnrolmentDocument() throws IOException {
        String cnpAsString = "1234567890123";
        long cnp = Long.parseLong(cnpAsString);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setCnp(cnp);
        clientDTO.setExpirationDate(LocalDateTime.now().plusDays(1));
        clientDTO.setFirstName("Gabi");
        clientDTO.setLastName("Vieriu");

        double clientReputation = 50;
        boolean clientExists = true;

        when(clientReputationAPI.getClientReputation(clientDTO.getCnp())).thenReturn(clientReputation);
        when(clientCheckAPI.clientAlreadyExists(clientDTO.getCnp())).thenReturn(clientExists);

        String pdfFileResourceName = "pdfs/enrolment_document.pdf";
        Resource resource = new ClassPathResource(pdfFileResourceName);
        byte[] expectedContent = resource.getInputStream().readAllBytes();

        InputStreamResource result = clientService.enrolClient(clientDTO);
        byte[] resultAsByteArray = result.getInputStream().readAllBytes();
        Assertions.assertTrue(pdfFilesHaveEqualContent(expectedContent, resultAsByteArray));
    }

    @Test
    void enrolClient_validClient_GenerateDenialDocument() throws IOException {
        String cnpAsString = "1234567890123";
        long cnp = Long.parseLong(cnpAsString);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setCnp(cnp);
        clientDTO.setExpirationDate(LocalDateTime.now().plusDays(1));
        clientDTO.setFirstName("Gabi");
        clientDTO.setLastName("Vieriu");

        double clientReputation = 120;
        boolean clientExists = true;

        when(clientReputationAPI.getClientReputation(clientDTO.getCnp())).thenReturn(clientReputation);
        when(clientCheckAPI.clientAlreadyExists(clientDTO.getCnp())).thenReturn(clientExists);

        String pdfFileResourceName = "pdfs/denial_document.pdf";
        Resource resource = new ClassPathResource(pdfFileResourceName);
        byte[] expectedContent = resource.getInputStream().readAllBytes();

        InputStreamResource result = clientService.enrolClient(clientDTO);
        byte[] resultAsByteArray = result.getInputStream().readAllBytes();
        Assertions.assertTrue(pdfFilesHaveEqualContent(expectedContent, resultAsByteArray));
    }

    private Boolean pdfFilesHaveEqualContent(byte[] expectedDocumentContent, byte[] actualDocumentContent) {
        try {
            PDDocument expectedDocument = PDDocument.load(expectedDocumentContent);
            PDDocument actualDocument = PDDocument.load(actualDocumentContent);

            if (expectedDocument.getNumberOfPages() != actualDocument.getNumberOfPages()) {
                return false;
            }

            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String expectedDocumentText = pdfTextStripper.getText(expectedDocument);
            String actualDocumentText = pdfTextStripper.getText(actualDocument);

            // compare pdfs text
            Assertions.assertEquals(expectedDocumentText, actualDocumentText);
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}