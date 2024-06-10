package com.domiaffaire.api.dto;


import com.domiaffaire.api.validators.NoBadWords;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientResponse {
  @NotBlank(message = "Objection argument should not be null")
  @NoBadWords(message="You have typed bad words")
  private String objectionArgument;
}
