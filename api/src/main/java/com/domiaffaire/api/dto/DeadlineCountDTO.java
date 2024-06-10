package com.domiaffaire.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeadlineCountDTO {
    private String delai;
    private int count;
}
