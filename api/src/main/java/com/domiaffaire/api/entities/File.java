package com.domiaffaire.api.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "files")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class File {
    @Id
    private String id;
    private String name;
    private String type;
    private long size;
    private byte[] fileData;
    private boolean companyCreation;
}
