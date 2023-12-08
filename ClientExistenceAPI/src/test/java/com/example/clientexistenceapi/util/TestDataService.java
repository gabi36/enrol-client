package com.example.clientexistenceapi.util;

import com.example.clientexistenceapi.model.Client;
import com.example.clientexistenceapi.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TestDataService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public void saveClient(long cnp) {
        Client client = new Client();
        client.setId(1L);
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
