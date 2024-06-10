package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.exceptions.ReservationNotFoundException;
import com.domiaffaire.api.exceptions.RoomNotFoundException;
import com.domiaffaire.api.exceptions.UserNotFoundException;

import java.util.List;

public interface ReservationService {
    String addReservation(RecommendationRequest recommendationRequest) throws UserNotFoundException;
    String acceptReservation(String id)throws ReservationNotFoundException;
    String rejectReservation(String id)throws ReservationNotFoundException;
    void exportReservationsToCSV(String filePath);
    void exportRoomsToCSV(String filePath);
    RoomDTO addRoom(RoomRequest roomRequest);
    RoomDTO getRoomById(String id)throws RoomNotFoundException;
    RoomDTO updateRoom(String id,RoomRequest roomRequest)throws RoomNotFoundException;
    List<RoomDTO> getAllRooms();
    void deleteRoom(String id)throws RoomNotFoundException;
    List<ReservationDTO> getAllReservations();
    ReservationDTO getReservationById(String id)throws ReservationNotFoundException;

    List<EquipmentCountDTO> getEquipmentDistribution();
}
