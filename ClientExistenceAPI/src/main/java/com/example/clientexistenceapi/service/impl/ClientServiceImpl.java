package com.example.clientexistenceapi.service.impl;

import com.example.clientexistenceapi.repository.ClientRepository;
import com.example.clientexistenceapi.service.ClientService;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Boolean clientExistsByCNP(long cnp) {
        return clientRepository.existsByCnp(cnp);
    }
}
