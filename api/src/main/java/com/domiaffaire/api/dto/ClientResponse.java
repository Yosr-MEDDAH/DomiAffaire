package com.domiaffaire.api.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientResponse {
  @NotBlank(message = "Objection argument should not be null")
  private String objectionArgument;
}
