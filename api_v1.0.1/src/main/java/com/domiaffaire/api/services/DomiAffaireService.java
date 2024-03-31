package com.domiaffaire.api.services;

import com.domiaffaire.api.entities.Client;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.exceptions.UserNotFoundException;

public interface DomiAffaireService {
    User findUserByEmail(String email) throws UserNotFoundException;
}
