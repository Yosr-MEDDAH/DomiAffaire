package com.domiaffaire.api.mappers;

import com.domiaffaire.api.dto.AdminDTO;
import com.domiaffaire.api.dto.ClientDTO;
import com.domiaffaire.api.entities.Admin;
import com.domiaffaire.api.entities.Client;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class Mapper {
    public ClientDTO fromClient(Client client){
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(client,clientDTO);
        return clientDTO;
    }

    public Client fromClientDTO(ClientDTO clientDTO){
        Client client = new Client();
        BeanUtils.copyProperties(clientDTO,client);
        return client;
    }

    public AdminDTO fromAdmin(Admin admin){
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(admin,adminDTO);
        return adminDTO;
    }

    public Admin fromAdminDTO(AdminDTO adminDTO){
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminDTO,admin);
        return admin;
    }
}
