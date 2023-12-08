package com.example.enrolclientapi.service.impl;

import com.example.enrolclientapi.Util.DocumentGeneratorUtil;
import com.example.enrolclientapi.accessors.ClientCheckAPI;
import com.example.enrolclientapi.accessors.ClientReputationAPI;
import com.example.enrolclientapi.dto.ClientDTO;
import com.example.enrolclientapi.exceptions.ClientException;
import com.example.enrolclientapi.mapper.ClientMapper;
import com.example.enrolclientapi.model.Client;
import com.example.enrolclientapi.repository.ClientRepository;
import com.example.enrolclientapi.service.ClientService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientReputationAPI clientReputationAPI;

    private final ClientCheckAPI clientCheckAPI;

    private final ClientRepository clientRepository;


    public ClientServiceImpl(ClientReputationAPI clientReputationAPI, ClientCheckAPI clientCheckAPI, ClientRepository clientRepository) {
        this.clientReputationAPI = clientReputationAPI;
        this.clientCheckAPI = clientCheckAPI;
        this.clientRepository = clientRepository;
    }

    @Override
    public InputStreamResource enrolClient(ClientDTO clientDTO) {
        validateClient(clientDTO);
        Double clientReputation = clientReputationAPI.getClientReputation(clientDTO.getCnp());
        boolean clientAlreadyExists = clientCheckAPI.clientAlreadyExists(clientDTO.getCnp());
        if(!clientAlreadyExists){
            saveClient(clientDTO);
        }
        return generateDocument(clientDTO, clientReputation);
    }

    //dummy cnp validation :)
    private void validateClient(ClientDTO clientDTO) {
        String cnpAsString = String.valueOf(clientDTO.getCnp());
        if(cnpAsString.length() != 13) {
            throw new ClientException("CNP is not valid!");
        }
        if(clientDTO.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new ClientException("ID is expired!");
        }
    }

    private InputStreamResource generateDocument(ClientDTO clientDTO, Double clientReputation) {
        if(clientReputation <= 100) {
            return DocumentGeneratorUtil.generateEnrolmentDocument(clientDTO);
        }
        return DocumentGeneratorUtil.generateDenialDocument(clientDTO);
    }

    private void saveClient(ClientDTO clientDTO){
        Client newClient = ClientMapper.INSTANCE.clientDtoToClient(clientDTO);
        clientRepository.save(newClient);
    }
}
