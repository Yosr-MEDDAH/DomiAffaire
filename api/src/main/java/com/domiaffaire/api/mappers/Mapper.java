package com.domiaffaire.api.mappers;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class Mapper {
    public UserDTO fromUserToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }


    public ClientDTO fromUserToClientDTO(User user){
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(user,clientDTO);
        return clientDTO;
    }

    public AccountantDTO fromUserToAccountantDTO(User user){
        AccountantDTO comptableDTO = new AccountantDTO();
        BeanUtils.copyProperties(user,comptableDTO);
        return comptableDTO;
    }

    public FileDTO fromFileToFileDTO(File file){
        FileDTO fileDTO = new FileDTO();
        BeanUtils.copyProperties(file,fileDTO);
        return fileDTO;
    }

    public ConsultationRequestDTO fromConsultationRequestToConsultationRequestDTO(ConsultationRequest consultationRequest){
        ConsultationRequestDTO consultationRequestDTO = new ConsultationRequestDTO();
        BeanUtils.copyProperties(consultationRequest,consultationRequestDTO);
        return consultationRequestDTO;
    }


    public ChatDTO fromChatToChatDTO(Chat chat){
        ChatDTO chatDTO = new ChatDTO();
        BeanUtils.copyProperties(chat,chatDTO);
        return chatDTO;
    }

    public DomiciliationRequestDTO fromDomiciliationRequestToDomiciliationRequestDTO(DomiciliationRequest domiciliationRequest){
        DomiciliationRequestDTO domiciliationRequestDTO = new DomiciliationRequestDTO();
        BeanUtils.copyProperties(domiciliationRequest,domiciliationRequestDTO);
        return domiciliationRequestDTO;
    }


    public ResponseDomiAdminDTO fromResponseDomiAdminToResponseDomiAdminDTO(ResponseDomiAdmin responseDomiAdmin){
        ResponseDomiAdminDTO responseDomiAdminDTO = new ResponseDomiAdminDTO();
        BeanUtils.copyProperties(responseDomiAdmin,responseDomiAdminDTO);
        return responseDomiAdminDTO;
    }

    public PackDTO fromPackToPackDTO(Pack pack){
        PackDTO packDTO = new PackDTO();
        BeanUtils.copyProperties(pack,packDTO);
        return packDTO;
    }

    public BlogDTO fromBlogToBlogDTO(Blog blog){
        BlogDTO blogDTO = new BlogDTO();
        BeanUtils.copyProperties(blog,blogDTO);
        return blogDTO;
    }

    public BlogCategoryDTO fromBlogCategoryToBlogCategoryDTO(BlogCategory blogCategory){
        BlogCategoryDTO blogCategoryDTO = new BlogCategoryDTO();
        BeanUtils.copyProperties(blogCategory,blogCategoryDTO);
        return blogCategoryDTO;
    }

    public FaqDTO fromFaqToFaqDTO(Faq faq){
        FaqDTO faqDTO = new FaqDTO();
        BeanUtils.copyProperties(faq,faqDTO);
        return faqDTO;
    }

    public RoomDTO fromRoomToRoomDTO(Room room){
        RoomDTO roomDTO = new RoomDTO();
        BeanUtils.copyProperties(room,roomDTO);
        return roomDTO;
    }

    public DeadlineDTO fromDeadlineToDeadlineDTO(Deadline deadline){
        DeadlineDTO deadlineDTO = new DeadlineDTO();
        BeanUtils.copyProperties(deadline,deadlineDTO);
        return deadlineDTO;
    }


    public ReservationDTO fromReservationToReservationDTO(Reservation reservation){
        ReservationDTO reservationDTO = new ReservationDTO();
        BeanUtils.copyProperties(reservation,reservationDTO);
        return reservationDTO;
    }


}
