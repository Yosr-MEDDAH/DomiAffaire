package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.Deadline;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeadlineRepository extends MongoRepository<Deadline,String> {
}
