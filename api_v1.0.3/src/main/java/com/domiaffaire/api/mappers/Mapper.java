package com.domiaffaire.api.mappers;

import com.domiaffaire.api.dto.AdminDTO;
import com.domiaffaire.api.dto.ClientDTO;
import com.domiaffaire.api.dto.ComptableDTO;
import com.domiaffaire.api.dto.UserDTO;
import com.domiaffaire.api.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class Mapper {
    public UserDTO fromUserToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }

    public User fromUserDTOtoUser(UserDTO userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        return user;
    }

    public ClientDTO fromUserToClientDTO(User user){
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(user,clientDTO);
        return clientDTO;
    }

    public ComptableDTO fromUserToComptableDTO(User user){
        ComptableDTO comptableDTO = new ComptableDTO();
        BeanUtils.copyProperties(user,comptableDTO);
        return comptableDTO;
    }

    public AdminDTO fromUserToAdminDTO(User user){
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(user,adminDTO);
        return adminDTO;
    }
}
