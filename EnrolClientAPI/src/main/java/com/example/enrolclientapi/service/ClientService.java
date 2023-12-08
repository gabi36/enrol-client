package com.example.enrolclientapi.service;

import com.example.enrolclientapi.dto.ClientDTO;
import org.springframework.core.io.InputStreamResource;

public interface ClientService {

    /**
     * Get pdf document
     *
     * @param clientDTO object with client information
     * @return pdf document
     */
    InputStreamResource enrolClient(ClientDTO clientDTO);
}
