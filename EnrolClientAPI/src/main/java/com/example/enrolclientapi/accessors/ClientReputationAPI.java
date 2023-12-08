package com.example.enrolclientapi.accessors;

import com.example.enrolclientapi.exceptions.ClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientReputationAPI {

    private final String CLIENT_REPUTATION_API = "http://localhost:8080/api/client-reputation/";

    private final RestTemplate restTemplate;

    public ClientReputationAPI(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Double getClientReputation(Long cnp) {
        String url = CLIENT_REPUTATION_API + cnp;

        ResponseEntity<Double> response = restTemplate.getForEntity(
                url,
                Double.class
        );
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        }
        throw new ClientException("Failed to get client reputation!");
    }
}
