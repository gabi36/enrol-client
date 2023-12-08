package com.example.enrolclientapi.accessors;

import com.example.enrolclientapi.exceptions.ClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientReputationAPITest {

    private final String CLIENT_REPUTATION_API = "http://localhost:8080/api/client-reputation/";

    @InjectMocks
    private ClientReputationAPI clientReputationAPI;

    @Mock
    private RestTemplate restTemplate;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientReputationAPI = new ClientReputationAPI(restTemplate);
    }

    @Test
    void getClientReputation_ReturnsClientReputation() {
        long cnp = 123;
        String url = CLIENT_REPUTATION_API + cnp;
        double expectedResponse = 50;

        ResponseEntity<Double> mockResponse = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(url, Double.class)).thenReturn(mockResponse);

        double result = clientReputationAPI.getClientReputation(cnp);

        assertTrue(result >= 0 && result <= 150);
    }

    @Test
    void getClientReputation_ThrowsException() {
        long cnp = 123;
        String url = CLIENT_REPUTATION_API + cnp;

        ResponseEntity<Double> mockResponse = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.getForEntity(url, Double.class)).thenReturn(mockResponse);

        assertThatThrownBy(() -> clientReputationAPI.getClientReputation(cnp))
                .isInstanceOf(ClientException.class);
    }

}