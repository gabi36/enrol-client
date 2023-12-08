package com.example.clientexistenceapi.controller;

import com.example.clientexistenceapi.ClientExistenceApiApplication;
import com.example.clientexistenceapi.repository.ClientRepository;
import com.example.clientexistenceapi.service.ClientService;
import com.example.clientexistenceapi.util.TestDataService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ClientExistenceApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerIT {

    private MockMvc mockMvc;

    @Autowired
    private TestDataService testDataService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ClientRepository clientRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
            .registerModule(new JavaTimeModule());

    @AfterEach
    public void tearDown() {
        clientRepository.deleteAll();
        clientRepository.flush();
    }

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        ClientController clientController = new ClientController(clientService);

        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    void checkClient_clientExists_status200() throws Exception {
        String cnpAsString = "1234567890123";
        long cnp = Long.parseLong(cnpAsString);
        testDataService.saveClient(cnp);
        MvcResult result = mockMvc.perform(get("http://localhost:8081/api/check-client/" + cnp))
                .andExpect(status().isOk()).andReturn();
        boolean clientExists = objectMapper.readValue(result.getResponse().getContentAsString(), Boolean.class);
        assertTrue(clientExists);
    }

    @Test
    void checkClient_clientNotExists_status200() throws Exception {
        String cnpAsString = "1234567890123";
        long cnp = Long.parseLong(cnpAsString);
        MvcResult result = mockMvc.perform(get("http://localhost:8081/api/check-client/" + cnp))
                .andExpect(status().isOk()).andReturn();
        boolean clientExists = objectMapper.readValue(result.getResponse().getContentAsString(), Boolean.class);
        assertFalse(clientExists);
    }
}