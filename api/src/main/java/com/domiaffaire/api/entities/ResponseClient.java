package com.domiaffaire.api.entities;

import com.domiaffaire.api.enums.ClientResponse;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "response_clients")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class ResponseClient {
    @Id
    private String id;
    private LocalDateTime responseDate = LocalDateTime.now();
    private ClientResponse response;
    private String objectionArgument;
}
