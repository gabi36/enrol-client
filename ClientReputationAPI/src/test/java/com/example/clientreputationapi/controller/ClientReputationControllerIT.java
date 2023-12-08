package com.example.clientreputationapi.controller;

import com.example.clientreputationapi.ClientReputationApiApplication;
import com.example.clientreputationapi.service.ClientReputationService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = ClientReputationApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientReputationControllerIT {

    private MockMvc mockMvc;

    @Autowired
    private ClientReputationService clientReputationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
            .registerModule(new JavaTimeModule());

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        ClientReputationController clientController = new ClientReputationController(clientReputationService);

        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setMessageConverters(jacksonMessageConverter).build();
        clientCheckAPI = new ClientCheckAPI(restTemplate);
    }

    @Test
    void clientReputation_status200() throws Exception {
        String cnpAsString = "1234567890123";
        long cnp = Long.parseLong(cnpAsString);
        MvcResult result = mockMvc.perform(get("http://localhost:8080/api/client-reputation/" + cnp))
                .andExpect(status().isOk()).andReturn();
        double clientReputation = objectMapper.readValue(result.getResponse().getContentAsString(), double.class);
        assertTrue(clientReputation >= 0 && clientReputation <= 150);
    }
}