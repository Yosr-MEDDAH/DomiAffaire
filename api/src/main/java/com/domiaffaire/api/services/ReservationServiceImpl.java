package com.domiaffaire.api.services;

import com.domiaffaire.api.dto.*;
import com.domiaffaire.api.entities.Reservation;
import com.domiaffaire.api.entities.Room;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.ReservationStatus;
import com.domiaffaire.api.exceptions.ReservationNotFoundException;
import com.domiaffaire.api.exceptions.RoomNotFoundException;
import com.domiaffaire.api.exceptions.UserNotFoundException;
import com.domiaffaire.api.mappers.Mapper;
import com.domiaffaire.api.repositories.ReservationRepository;
import com.domiaffaire.api.repositories.RoomRepository;
import com.domiaffaire.api.repositories.UserRepository;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Override
    public String addReservation(RecommendationRequest recommendationRequest) throws UserNotFoundException {
        LocalDate date;
        LocalTime startTime;
        LocalTime endTime;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userRepository.findFirstByEmail(authentication.getName()).orElse(null);
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Authenticated user not found.");
        }
        for (Slot elmt : recommendationRequest.getSlots()) {
            Reservation reservation = new Reservation();
            reservation.setClient(authenticatedUser);
            Room room = roomRepository.findById(recommendationRequest.getRoom_id()).get();
            reservation.setRoom(room);
            date = LocalDate.parse(elmt.getDate());
            startTime = LocalTime.parse(elmt.getStart_time());
            endTime = LocalTime.parse(elmt.getEnd_time());

            // Setting dateBegining correctly
            LocalDateTime dateBegining = LocalDateTime.of(date, startTime);
            reservation.setDateBegining(dateBegining);

            // Setting dateEnding correctly
            LocalDateTime dateEnding = LocalDateTime.of(date, endTime);
            reservation.setDateEnding(dateEnding);

            // Saving the reservation
            reservationRepository.save(reservation);
        }
        return "Reservations are saved";
    }

    @Override
    public String acceptReservation(String id) throws ReservationNotFoundException {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        List<Reservation> reservations = reservationRepository.findAllByStatusIs(ReservationStatus.ACCEPTED);
        if(reservationOptional.isPresent()){
            Reservation reservation = reservationOptional.get();
            for(Reservation elmt:reservations){
                if(reservation.equals(elmt)){
                    return "The room is already reserved in this date";
                }
            }
            reservation.setStatus(ReservationStatus.ACCEPTED);
            reservationRepository.save(reservation);
            return "Reservation validated successfully";
        }else{
            throw new ReservationNotFoundException("Reservation not found");
        }
    }

    @Override
    public String rejectReservation(String id) throws ReservationNotFoundException {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if(reservationOptional.isPresent()){
            Reservation reservation = reservationOptional.get();
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationRepository.save(reservation);
            return "Reservation rejected successfully";
        }else{
            throw new ReservationNotFoundException("Reservation not found");
        }
    }

    @Override
    public void exportReservationsToCSV(String filePath) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try (CSVWriter writer = new CSVWriter(
                new FileWriter(filePath),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            writer.writeNext(new String[]{"room_id", "dateBegining", "dateEnding"});
            List<Reservation> reservations = reservationRepository.findAllByStatusIs(ReservationStatus.ACCEPTED);

            for (Reservation reservation : reservations) {
                writer.writeNext(new String[]{
                        reservation.getRoom().getId(),
                        reservation.getDateBegining().format(formatter),
                        reservation.getDateEnding().format(formatter)
                });
            }
            System.out.println("CSV file created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportRoomsToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("room_id,nbrPlaces,equipments\n");
            List<Room> rooms = roomRepository.findAll();

            for (Room room : rooms) {
                // Join equipments list into a single string with commas
                String equipments = room.getEquipments().stream()
                        .collect(Collectors.joining(","));

                // Add a single set of quotes around the equipments string
                equipments = "\"" + equipments + "\"";

                // Write the formatted line to the file
                writer.write(room.getId() + "," + room.getNbrPlaces() + "," + equipments + "\n");
            }
            System.out.println("CSV file created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RoomDTO addRoom(RoomRequest roomRequest) {
        Room room = new Room();
        room.setName(roomRequest.getName());
        room.setNbrPlaces(roomRequest.getNbrPlaces());
        room.setEquipments(roomRequest.getEquipments());
        roomRepository.save(room);
        return mapper.fromRoomToRoomDTO(room);
    }

    @Override
    public RoomDTO getRoomById(String id) throws RoomNotFoundException {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if(roomOptional.isPresent()){
            Room room = roomOptional.get();
            return mapper.fromRoomToRoomDTO(room);
        }else{
            throw new RoomNotFoundException("Room not found");
        }
    }

    @Override
    public RoomDTO updateRoom(String id, RoomRequest roomRequest) throws RoomNotFoundException {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if(roomOptional.isPresent()){
            Room room = roomOptional.get();
            room.setName(roomRequest.getName());
            room.setNbrPlaces(roomRequest.getNbrPlaces());
            room.setEquipments(roomRequest.getEquipments());
            roomRepository.save(room);
            return mapper.fromRoomToRoomDTO(room);
        }else{
            throw new RoomNotFoundException("Room not found");
        }
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAllByOrderByCreatedAtDesc();
        List<RoomDTO> roomDTOS = rooms.stream()
                .map(room -> mapper.fromRoomToRoomDTO(room))
                .collect(Collectors.toList());
        return roomDTOS;
    }

    @Override
    public void deleteRoom(String id) throws RoomNotFoundException{
        Optional<Room> roomOptional = roomRepository.findById(id);
        if(roomOptional.isPresent()){
            Room room = roomOptional.get();
            roomRepository.delete(room);
        }else{
            throw new RoomNotFoundException("Room not found");
        }
    }

    @Override
    public ReservationDTO getReservationById(String id) throws ReservationNotFoundException {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if(reservationOptional.isPresent()){
            Reservation reservation = reservationOptional.get();
            return mapper.fromReservationToReservationDTO(reservation);
        }else{
            throw new ReservationNotFoundException("Reservation not found");
        }
    }

    @Override
    public List<EquipmentCountDTO> getEquipmentDistribution() {
        List<Reservation> reservations = reservationRepository.findAllByStatusIsOrderByCreatedAtDesc(ReservationStatus.ACCEPTED);
        Map<String, Long> equipmentCount = new HashMap<>();

        for (Reservation reservation : reservations) {
            List<String> equipments = reservation.getRoom().getEquipments();
            for (String equipment : equipments) {
                equipmentCount.put(equipment, equipmentCount.getOrDefault(equipment, 0L) + 1);
            }
        }

        return equipmentCount.entrySet().stream()
                .map(entry -> new EquipmentCountDTO(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAllByStatusIsOrderByCreatedAtDesc(ReservationStatus.IN_PROGRESS);
        List<ReservationDTO> reservationDTOS = reservations.stream()
                .map(reservation -> mapper.fromReservationToReservationDTO(reservation))
                .collect(Collectors.toList());
        return reservationDTOS;
    }

}
