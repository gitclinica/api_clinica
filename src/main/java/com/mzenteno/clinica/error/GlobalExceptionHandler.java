package com.mzenteno.clinica.error;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mzenteno.clinica.dto.ErrorMessageDto;
import com.mzenteno.clinica.error.exception.BadRequestException;
import com.mzenteno.clinica.error.exception.ConflictException;
import com.mzenteno.clinica.error.exception.InternalServerErrorException;
import com.mzenteno.clinica.error.exception.JwtAuthenticationException;
import com.mzenteno.clinica.error.exception.NotFoundException;

import jakarta.validation.constraints.NotNull;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @SuppressWarnings("null")
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    System.out.println("handleHttpMessageNotReadable");

    Throwable cause = ex.getCause();
      Map<String, String> errorResponse = new HashMap<>();

      if (cause instanceof InvalidFormatException) {
          InvalidFormatException e = (InvalidFormatException) cause;
          String fieldName = e.getPath().get(0).getFieldName();
          errorResponse.put(fieldName, String.format("El valor '%s' no es válido para el campo '%s'", e.getValue(), fieldName));
      } else {
          errorResponse.put("error", "Error de formato en el cuerpo de la solicitud");
      }

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @SuppressWarnings("null")
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, @NotNull WebRequest request) {
    System.out.println("handleMethodArgumentNotValid");
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage())
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @SuppressWarnings("null")
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Object> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    System.out.println("handleTypeMismatchException");

    Map<String, String> errorResponse = new HashMap<>();
    String fieldName = ex.getName();
    errorResponse.put(fieldName, String.format("El valor '%s' no es válido para el campo '%s'", ex.getName(), ex.getRequiredType().getSimpleName()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorMessageDto> handleResourceNotFoundException(NotFoundException e){
    ErrorMessageDto response = new ErrorMessageDto(HttpStatus.NOT_FOUND.value(), "Not Found", e.getMessage(), new Date());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  } 

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorMessageDto> handleBadRequestException(BadRequestException e){
    ErrorMessageDto response = new ErrorMessageDto(HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(), new Date());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorMessageDto> handleConflictException(ConflictException e){
    ErrorMessageDto response = new ErrorMessageDto(HttpStatus.CONFLICT.value(), "Conflict", e.getMessage(), new Date());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<ErrorMessageDto> handleInternalServerErrorException(InternalServerErrorException e) {
    ErrorMessageDto response = new ErrorMessageDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage(), new Date());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(JwtAuthenticationException.class)
  public ResponseEntity<ErrorMessageDto> handleAuthenticationCredentialsNotFoundException(JwtAuthenticationException e){
    ErrorMessageDto response = new ErrorMessageDto(HttpStatus.UNAUTHORIZED.value(), "Authentication Exception", e.getMessage(), new Date());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

}