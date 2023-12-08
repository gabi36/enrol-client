package com.example.clientreputationapi.service;

import com.example.clientreputationapi.service.impl.ClientReputationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ClientReputationServiceTest {

    @InjectMocks
    private ClientReputationServiceImpl clientReputationService;

    @Test
    void clientReputationFormula_returnReputation(){
        long cnp = 123;
        double clientReputation = clientReputationService.clientReputationFormula(cnp);
        assertTrue(clientReputation >= 0 && clientReputation <= 150);
    }
}