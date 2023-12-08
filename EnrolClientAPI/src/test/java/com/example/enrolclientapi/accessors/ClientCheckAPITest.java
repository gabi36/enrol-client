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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientCheckAPITest {

    private final String CLIENT_CHECK_API = "http://localhost:8081/api/check-client/";

    @InjectMocks
    private ClientCheckAPI clientCheckAPI;

    @Mock
    private RestTemplate restTemplate;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientCheckAPI = new ClientCheckAPI(restTemplate);
    }

    @Test
    void clientAlreadyExists_ExistingClient_ReturnsTrue() {
        long cnp = 123;
        String url = CLIENT_CHECK_API + cnp;
        Boolean expectedResponse = true;

        ResponseEntity<Boolean> mockResponse = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(url, Boolean.class)).thenReturn(mockResponse);

        Boolean result = clientCheckAPI.clientAlreadyExists(cnp);

        assertTrue(result);
    }

    @Test
    void clientAlreadyExists_NonExistingClient_ReturnsFalse() {
        long cnp = 123;
        String url = CLIENT_CHECK_API + cnp;
        Boolean expectedResponse = false;

        ResponseEntity<Boolean> mockResponse = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(url, Boolean.class)).thenReturn(mockResponse);


        Boolean result = clientCheckAPI.clientAlreadyExists(cnp);

        assertFalse(result);
    }

    @Test
    void clientAlreadyExists_ThrowsException() {
        long cnp = 123;
        String url = CLIENT_CHECK_API + cnp;

        ResponseEntity<Boolean> mockResponse = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.getForEntity(url, Boolean.class)).thenReturn(mockResponse);

        assertThatThrownBy(() -> clientCheckAPI.clientAlreadyExists(cnp))
                .isInstanceOf(ClientException.class);
    }
}