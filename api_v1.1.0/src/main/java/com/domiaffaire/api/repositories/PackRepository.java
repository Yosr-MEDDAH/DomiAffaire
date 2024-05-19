package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.Deadline;
import com.domiaffaire.api.entities.Pack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackRepository extends MongoRepository<Pack,String> {
    Optional<Pack> findByDesignation(String designation);
}
