package com.example.enrolclientapi.accessors;

import com.example.enrolclientapi.exceptions.ClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientCheckAPI {

    private final String CLIENT_CHECK_API = "http://localhost:8081/api/check-client/";

    private final RestTemplate restTemplate;

    public ClientCheckAPI(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Boolean clientAlreadyExists(Long cnp) {
        String url = CLIENT_CHECK_API + cnp;

        ResponseEntity<Boolean> response = restTemplate.getForEntity(
                url,
                Boolean.class
        );
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        }
        throw new ClientException("Failed to check if client already exists!");
    }
}
