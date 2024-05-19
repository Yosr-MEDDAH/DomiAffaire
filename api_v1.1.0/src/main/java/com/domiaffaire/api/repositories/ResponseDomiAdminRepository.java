package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.Deadline;
import com.domiaffaire.api.entities.ResponseDomiAdmin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseDomiAdminRepository extends MongoRepository<ResponseDomiAdmin,String> {
}
