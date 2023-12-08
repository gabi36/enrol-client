package com.example.clientexistenceapi.repository;

import com.example.clientexistenceapi.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Boolean existsByCnp(long cnp);
}
