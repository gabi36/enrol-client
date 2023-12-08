package com.example.clientexistenceapi.service;

import com.example.clientexistenceapi.repository.ClientRepository;
import com.example.clientexistenceapi.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void clientExistsByCNP_clientExists_returnTrue(){
        long cnp = 123;
        when(clientRepository.existsByCnp(cnp)).thenReturn(true);
        boolean clientExists = clientService.clientExistsByCNP(cnp);
        assertTrue(clientExists);
    }

    @Test
    void clientExistsByCNP_clientNotExists_returnFalse(){
        long cnp = 123;
        when(clientRepository.existsByCnp(cnp)).thenReturn(false);
        boolean clientExists = clientService.clientExistsByCNP(cnp);
        assertFalse(clientExists);
    }
}