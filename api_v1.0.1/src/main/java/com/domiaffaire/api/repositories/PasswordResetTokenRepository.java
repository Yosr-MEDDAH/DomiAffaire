package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.PasswordResetToken;
import com.domiaffaire.api.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);
}
