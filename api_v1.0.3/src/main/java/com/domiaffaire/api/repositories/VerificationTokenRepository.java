package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);
}
