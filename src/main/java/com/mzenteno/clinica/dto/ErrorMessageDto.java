package com.mzenteno.clinica.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorMessageDto {
  private int statusCode;       // El código de estado HTTP
  private String error;         // El tipo de error o descripción breve
  private String message;       // Descripción detallada del error
  private Date timeStamp;
}
