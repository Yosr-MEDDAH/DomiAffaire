package com.domiaffaire.api.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "deadlines")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class Deadline {
    @Id
    private String id;
    private LocalDateTime dateBeginig = LocalDateTime.now();
    private LocalDateTime limitedDate ;
    private BigDecimal netPayable;
    private float packPrice;
    private int counterOfNotPaidPeriods;
}
