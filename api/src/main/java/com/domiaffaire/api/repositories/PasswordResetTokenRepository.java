package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken,String> {
    PasswordResetToken findByToken(String token);
}
