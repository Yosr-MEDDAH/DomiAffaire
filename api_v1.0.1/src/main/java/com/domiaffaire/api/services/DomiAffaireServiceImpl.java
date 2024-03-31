package com.domiaffaire.api.services;

import com.domiaffaire.api.entities.Client;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import com.domiaffaire.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DomiAffaireServiceImpl implements DomiAffaireService {
    private final UserRepository userRepository;

    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findFirstByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }

}
