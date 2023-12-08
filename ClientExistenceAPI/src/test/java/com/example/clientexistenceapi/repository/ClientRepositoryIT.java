package com.example.clientexistenceapi.repository;

import com.example.clientexistenceapi.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepositoryIT {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void existsByCnp_userExists_returnTrue() {
        String cnpAsString = "1234567890123";
        long cnp = Long.parseLong(cnpAsString);
        saveClient(cnp);
        boolean clientAlreadyExists = clientRepository.existsByCnp(cnp);
        assertTrue(clientAlreadyExists);
    }

    @Test
    void existsByCnp_userNotExists_returnFalse() {
        String cnpAsString = "1234567890123";
        long cnp = Long.parseLong(cnpAsString);
        boolean clientAlreadyExists = clientRepository.existsByCnp(cnp);
        assertFalse(clientAlreadyExists);
    }

    public void saveClient(long cnp) {
        Client client = new Client();
        client.setSeries("ABC");
        client.setNumber(12345);
        client.setCnp(cnp);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setNationality("Some Nationality");
        client.setBirthPlace("Some Birthplace");
        client.setHome("Some Home Address");
        client.setExpirationDate(LocalDateTime.now());

        clientRepository.save(client);
    }
}