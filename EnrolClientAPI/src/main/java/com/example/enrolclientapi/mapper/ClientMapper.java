package com.example.enrolclientapi.mapper;

import com.example.enrolclientapi.dto.ClientDTO;
import com.example.enrolclientapi.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);
    Client clientDtoToClient(ClientDTO clientDTO);
    ClientDTO clientToClientDto(Client client);
}
