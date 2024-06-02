package com.domiaffaire.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomDTO {
    private String id;
    private String name;
    private int nbrPlaces;
    private List<String> equipments = new ArrayList<>();
}
