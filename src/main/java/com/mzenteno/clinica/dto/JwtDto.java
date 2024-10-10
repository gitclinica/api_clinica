package com.mzenteno.clinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class JwtDto {

  @Data
  @Builder
  @AllArgsConstructor
  public static class JwtRequest{
    @NotNull(message = "El Nombre de Usuario es obligatorio")
    @NotBlank(message = "El Nombre de Usuario es obligatorio")
    @Size(min = 5, max = 20, message = "El Nombre de Usuario debe tener mínimo 5 caracteres y máximo 20.")
    private String UserName;

    @NotNull(message = "El Password es obligatorio")
    @NotBlank(message = "El Password es obligatorio")
    @Size(min = 8, max = 50, message = "El Password debe tener mínimo 8 caracteres y máximo 50.")
    private String Password;
  }

  @Data
  @Builder
  @AllArgsConstructor
  public static class JwtResponse {
    private String UserName;
    private String Token;
  }
  
}
