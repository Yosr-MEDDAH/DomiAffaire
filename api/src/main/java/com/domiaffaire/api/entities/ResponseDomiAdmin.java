package com.domiaffaire.api.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "responses_domi_admin")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class ResponseDomiAdmin {
    @Id
    private String id;
    private LocalDateTime responseDate = LocalDateTime.now();
    @DBRef
    private File draftContract;
    @DBRef
    private User responsedBy;

}
