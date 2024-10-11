package com.mzenteno.clinica.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mzenteno.clinica.dto.UsuarioDto;
import com.mzenteno.clinica.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

  @Autowired
  UsuarioService usuarioService;

  @GetMapping
  public ResponseEntity<List<UsuarioDto.Usuario>> findAll() {
    List<UsuarioDto.Usuario> usuarios = usuarioService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(usuarios);
  }
 
  @PostMapping
  public ResponseEntity<UsuarioDto.Usuario> create(@Valid @RequestBody UsuarioDto.Create bodyDto) {
    UsuarioDto.Usuario nuevoUsuario = usuarioService.create(bodyDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UsuarioDto.Usuario> findById(@Valid @PathVariable Long id) {
    UsuarioDto.Usuario usuario = usuarioService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(usuario);
  }
  
}