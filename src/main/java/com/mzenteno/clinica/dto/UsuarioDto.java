package com.mzenteno.clinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UsuarioDto {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Usuario{
    private Long idUsuario;
    private char grupoUsuario;
    private String nombreCompleto;
    private String usuario;
    private boolean estado;
  }
  
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Create{
    @NotNull(message = "El Grupo de Usuario es obligatorio")
    private Character grupoUsuario;

    @NotNull(message = "El Nombre Completo es obligatorio")
    @NotBlank(message = "El Nombre Completo es obligatorio")
    @Size(min = 10, max = 50, message = "El Nombre Completo debe tener mínimo 10 caracteres y máximo 50")
    private String nombreCompleto;

    @NotNull(message = "El Nombre de Usuario es obligatorio")
    @NotBlank(message = "El Nombre de Usuario es obligatorio")
    @Size(min = 5, max = 20, message = "El Nombre de Usuario debe tener mínimo 5 caracteres y máximo 20")
    private String usuario;

    @NotNull(message = "La Clave no puede ser vacía")
    @NotBlank(message = "La Clave no puede ser vacía")
    @Size(min = 10, max = 30, message = "La Clave debe tener mínimo 10 caracteres y máximo 30")
    private String clave;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CambiarClave{
    @NotNull(message = "El Id de Usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "Nueva Clave no válida")
    @NotBlank(message = "Nueva Clave no válida")
    @Size(min = 8, max = 60, message = "El Nombre de Usuario debe tener mínimo 8 caracteres y máximo 30")
    private String clave;
  }

}
