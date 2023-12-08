package com.example.enrolclientapi.controller;

import com.example.enrolclientapi.EnrolClientApiApplication;
import com.example.enrolclientapi.dto.ClientDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = EnrolClientApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//These tests are currently failing. After researching online, I suspect that the issue is not directly related to the code itself
//but may be associated with the configuration of the integrated development environment (IDE). Despite my efforts to resolve it, I have not been successful so far
class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
            .registerModule(new JavaTimeModule());

    @Test
    void enrolClient_idExpired_status500() throws Exception {
        ClientDTO clientDTO = getClientDTO();
        clientDTO.setExpirationDate(LocalDateTime.now().minusDays(1));
        mockMvc.perform(post("http://localhost:8082/api/client")
                        .content(objectMapper.writeValueAsString(clientDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void enrolClient_invalidCNP_status500() throws Exception {
        ClientDTO clientDTO = getClientDTO();
        clientDTO.setCnp(123L);
        mockMvc.perform(post("http://localhost:8082/api/client")
                        .content(objectMapper.writeValueAsString(clientDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void enrolClient_generateEnrolmentDocument_status200() throws Exception {
        ClientDTO clientDTO = getClientDTO();

        mockCheckClientExistenceAPIResponse(clientDTO.getCnp());
        mockClientReputationAPIResponse(clientDTO.getCnp(), 50);


        MvcResult result = mockMvc.perform(post("http://localhost:8082/api/client")
                        .content(objectMapper.writeValueAsString(clientDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String pdfFileResourceName = "pdfs/enrolment_document.pdf";
        Resource resource = new ClassPathResource(pdfFileResourceName);
        byte[] expectedContent = resource.getInputStream().readAllBytes();

        byte[] resultAsByteArray = result.getResponse().getContentAsByteArray();
        Assertions.assertTrue(pdfFilesHaveEqualContent(expectedContent, resultAsByteArray));
    }

    @Test
    void enrolClient_generateDenialDocument_status200() throws Exception {
        ClientDTO clientDTO = getClientDTO();

        mockCheckClientExistenceAPIResponse(clientDTO.getCnp());
        mockClientReputationAPIResponse(clientDTO.getCnp(), 120);


        MvcResult result = mockMvc.perform(post("http://localhost:8082/api/client")
                        .content(objectMapper.writeValueAsString(clientDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String pdfFileResourceName = "pdfs/denial_document.pdf";
        Resource resource = new ClassPathResource(pdfFileResourceName);
        byte[] expectedContent = resource.getInputStream().readAllBytes();

        byte[] resultAsByteArray = result.getResponse().getContentAsByteArray();
        Assertions.assertTrue(pdfFilesHaveEqualContent(expectedContent, resultAsByteArray));
    }

    private ClientDTO getClientDTO() {
        String cnpAsString = "1234567890123";
        long cnp = Long.parseLong(cnpAsString);

        ClientDTO client = new ClientDTO();
        client.setSeries("ABC");
        client.setNumber(12345);
        client.setCnp(cnp);
        client.setFirstName("Gabi");
        client.setLastName("Vieriu");
        client.setNationality("Some Nationality");
        client.setBirthPlace("Some Birthplace");
        client.setHome("Some Home Address");
        client.setExpirationDate(LocalDateTime.now().plusDays(1));
        return client;
    }

    private void mockClientReputationAPIResponse(Long cnp, double clientReputation) {
        String CLIENT_REPUTATION_API = "http://localhost:8080/api/client-reputation/";
        String url = CLIENT_REPUTATION_API + cnp;

        ResponseEntity<Double> mockResponse = new ResponseEntity<>(clientReputation, HttpStatus.OK);
        when(restTemplate.getForEntity(url, Double.class)).thenReturn(mockResponse);
        when(restTemplate.getForEntity(Mockito.eq(url), Mockito.eq(Double.class))).thenReturn(mockResponse);
    }

    private void mockCheckClientExistenceAPIResponse(Long cnp) {
        String CLIENT_CHECK_API = "http://localhost:8081/api/check-client/";
        String url = CLIENT_CHECK_API + cnp;

        ResponseEntity<Boolean> mockResponse = new ResponseEntity<>(true, HttpStatus.OK);
        when(restTemplate.getForEntity(Mockito.eq(url), Mockito.eq(Boolean.class))).thenReturn(mockResponse);
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