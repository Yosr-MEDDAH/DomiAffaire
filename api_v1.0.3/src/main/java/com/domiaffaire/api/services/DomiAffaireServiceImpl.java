package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.UserRole;
import com.domiaffaire.api.exceptions.NoContentException;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import com.domiaffaire.api.exceptions.WrongPasswordException;
import com.domiaffaire.api.mappers.Mapper;
import com.domiaffaire.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DomiAffaireServiceImpl implements DomiAffaireService {
    private final UserRepository userRepository;
    private final Mapper mapper;


    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findFirstByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user;
        } else {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = users.stream()
                .map(user->mapper.fromUserToUserDTO(user))
                .collect(Collectors.toList());
        return userDTOS;
    }



    @Override
    public List<ClientDTO> findAllClientsArchived() throws NoContentException {
        List<User> users = userRepository.findAllByUserRole(UserRole.CLIENT);
        List<User> clientsArchived = new ArrayList<>();
        for(User elmt:users){
            if(!elmt.isNotArchived()){
                clientsArchived.add(elmt);
            }
        }
        if (clientsArchived.isEmpty()) {
            throw new NoContentException("No clients found.");
        }
        List<ClientDTO> clientsDTOS = clientsArchived.stream()
                .map(client->mapper.fromUserToClientDTO(client))
                .collect(Collectors.toList());
        return clientsDTOS;
    }

    @Override
    public List<ClientDTO> findAllClients() throws NoContentException {
        List<User> users = userRepository.findAllByUserRole(UserRole.CLIENT);

        if (users.isEmpty()) {
            throw new NoContentException("No clients found.");
        }
        else{
            List<User> clients= new ArrayList<>();
            for(User elmt:users){
                if(elmt.isEnabled() && elmt.isNotArchived()){
                    clients.add(elmt);
                }
            }
            return clients.stream()
                    .map(client -> mapper.fromUserToClientDTO(client))
                    .collect(Collectors.toList());
        }
    }


    @Override
    public List<ComptableDTO> findAllComptables() throws NoContentException {
        List<User> users = userRepository.findAllByUserRole(UserRole.COMPTABLE);

        if (users.isEmpty()) {
            throw new NoContentException("No clients found.");
        }else{
            return users.stream()
                    .map(user -> mapper.fromUserToComptableDTO(user))
                    .collect(Collectors.toList());
        }


    }


    @Override
    public void archiveProfile(String id) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setNotArchived(false);
            userRepository.save(user);
        }
        else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public void unarchiveProfile(String id) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setNotArchived(true);
            userRepository.save(user);
        }
        else{
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public UserDTO updateUser(UpdateProfileRequest updateProfileRequest, byte[] imageBytes, String role, String id) {
        if (role.equals(UserRole.CLIENT.toString())) {
            User client = userRepository.findById(id).get();
            client.setId(id);
            client.setEmail(client.getEmail());
            client.setFirstName(updateProfileRequest.getFirstName());
            client.setLastName(updateProfileRequest.getLastName());
            client.setImage(imageBytes);
            client.setPwd(client.getPwd());
            client.setPhoneNumber(updateProfileRequest.getPhoneNumber());
            client.setBirthDate(updateProfileRequest.getBirthDate());
            client.setUserRole(UserRole.CLIENT);
            User createdClient = userRepository.save(client);
            UserDTO createdClientDto = new UserDTO();
            BeanUtils.copyProperties(createdClient, createdClientDto);
            return createdClientDto;
        } else if (role.equals(UserRole.COMPTABLE.toString())) {
            User comptable =userRepository.findById(id).get();
            comptable.setId(id);
            comptable.setFirstName(updateProfileRequest.getFirstName());
            comptable.setLastName(updateProfileRequest.getLastName());
            comptable.setImage(imageBytes);
            comptable.setPwd(comptable.getPwd());
            comptable.setPhoneNumber(updateProfileRequest.getPhoneNumber());
            comptable.setBirthDate(updateProfileRequest.getBirthDate());
            comptable.setUserRole(UserRole.COMPTABLE);
            User createdComptable = userRepository.save(comptable);
            UserDTO createdcomptableDto = new UserDTO();
            BeanUtils.copyProperties(createdComptable, createdcomptableDto);
            return createdcomptableDto;
        } else {
            return null;
        }
    }

    @Override
    public UserDTO updateAdmin(UpdateAdminProfileRequest updateAdminProfileRequest, byte[] imageBytes, String id) {
        User admin = userRepository.findById(id).get();
        if(admin!=null){
            admin.setId(id);
            admin.setEmail(admin.getEmail());
            admin.setPwd(admin.getPwd());
            admin.setName(updateAdminProfileRequest.getUsername());
            admin.setImage(imageBytes);
            admin.setUserRole(UserRole.ADMIN);
            User createdAdmin = userRepository.save(admin);
            UserDTO createdAdminDTO = new UserDTO();
            BeanUtils.copyProperties(createdAdmin,createdAdminDTO);
            return createdAdminDTO;
        }
        return null;
    }

    @Override
    public void changePassword(String id, ChangePasswordRequest changePasswordRequest) throws WrongPasswordException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setId(id);
            boolean passwordMatches = new BCryptPasswordEncoder().matches(changePasswordRequest.getOldPassword(), user.getPassword());
            if(!passwordMatches){
                throw new WrongPasswordException("False old password");
            }else{
                if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())){
                    throw new WrongPasswordException("Passwords does not match");
                }else{
                    user.setPwd(new BCryptPasswordEncoder().encode(changePasswordRequest.getNewPassword()));
                    userRepository.save(user);
                }
            }

        }
    }



}
