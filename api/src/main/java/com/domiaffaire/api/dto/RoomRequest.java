package com.domiaffaire.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomRequest {
    private String name;
    private int nbrPlaces;
    private ArrayList<String> equipments;
}
