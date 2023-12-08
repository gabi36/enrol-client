package com.example.clientreputationapi.service;

public interface ClientReputationService {

    /**
     * Get client reputation
     *
     * @param cnp cnp of the client
     * @return client reputation
     */
    double clientReputationFormula(long cnp);
}
