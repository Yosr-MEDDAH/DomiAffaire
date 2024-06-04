package com.domiaffaire.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationRequest {
    private String room_id;
    private List<Slot> slots;
}
