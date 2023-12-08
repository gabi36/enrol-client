package com.example.clientexistenceapi.service;

public interface ClientService {

    /**
     * Check if the client already exists
     *
     * @param cnp cnp of the client
     * @return true if the client exists and false if not
     */
    Boolean clientExistsByCNP(long cnp);
}
