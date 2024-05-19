package com.domiaffaire.api.repositories;


import com.domiaffaire.api.entities.ResponseClient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseClientRepository extends MongoRepository<ResponseClient,String> {
}
