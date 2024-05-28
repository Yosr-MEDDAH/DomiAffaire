package com.domiaffaire.api.dto;

import lombok.Data;

@Data
public class PackDTO {
    private String id;
    private String designation;
    private float price;
    private String description;
    private int userCount;
}

