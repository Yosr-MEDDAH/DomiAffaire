package com.domiaffaire.api.dto;

import lombok.Data;

@Data
public class FileDTO {
    private String id;
    private String name;
    private String type;
    private long size;
    private byte[] fileData;
    private boolean companyCreation;
}
