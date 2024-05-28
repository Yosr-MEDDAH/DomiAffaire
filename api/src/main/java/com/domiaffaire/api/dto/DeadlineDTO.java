package com.domiaffaire.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DeadlineDTO {
    private LocalDateTime dateBeginig ;
    private LocalDateTime limitedDate ;
    private BigDecimal netPayable;
    private int counterOfNotPaidPeriods;
}
