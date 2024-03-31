package com.domiaffaire.api.repositories;

import com.domiaffaire.api.entities.Client;
import com.domiaffaire.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String email);
    Optional<Client> findByEmail(String email);
}
