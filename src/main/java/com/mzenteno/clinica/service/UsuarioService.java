package com.mzenteno.clinica.service;

import java.util.List;
import com.mzenteno.clinica.dto.UsuarioDto;

public interface UsuarioService {

  UsuarioDto.Usuario create(UsuarioDto.Create bodyDto);
  
  List<UsuarioDto.Usuario> findAll();

  UsuarioDto.Usuario findById(Long id);

  UsuarioDto.Usuario findByCorreo(String correo);

  public void modificarClave(UsuarioDto.CambiarClave bodyDto);
  
}
