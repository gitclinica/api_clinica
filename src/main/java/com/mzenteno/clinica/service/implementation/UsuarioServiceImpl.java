package com.mzenteno.clinica.service.implementation;

import java.util.List;
import org.modelmapper.MappingException;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.mzenteno.clinica.entity.Usuario;
import com.mzenteno.clinica.error.exception.BadRequestException;
import com.mzenteno.clinica.error.exception.ConflictException;
import com.mzenteno.clinica.error.exception.InternalServerErrorException;
import com.mzenteno.clinica.error.exception.NotFoundException;
import com.mzenteno.clinica.dto.UsuarioDto;
import com.mzenteno.clinica.dto.UsuarioDto.CambiarClave;
import com.mzenteno.clinica.repository.UsuarioRepository;
import com.mzenteno.clinica.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
  @Autowired
  UsuarioRepository usuarioRepository;  
  @Autowired
  ModelMapper modelMapper;

  @Override
  public UsuarioDto.Usuario create(UsuarioDto.Create bodyDto) {    
    verificarUsuarioNoExiste(bodyDto.getUsuario());

    try {
      Usuario usuario = modelMapper.map(bodyDto, Usuario.class);
      usuario.setEstado(true);

      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      usuario.setClave(passwordEncoder.encode(bodyDto.getClave()));

      usuario = usuarioRepository.save(usuario);

      return modelMapper.map(usuario, UsuarioDto.Usuario.class);
    } catch (DataIntegrityViolationException e) {
        throw new BadRequestException("Datos inválidos o incompletos");
    } catch (MappingException e) {
        throw new BadRequestException("Error en el mapeo de datos");
    } catch (Exception e) {
        throw new InternalServerErrorException("Error inesperado al crear el registro " + e.getMessage());
    }
  }

  @Override
  public List<UsuarioDto.Usuario> findAll() {
    try {
      List<Usuario> usuarios = usuarioRepository.findByEstadoOrderByUsuarioDesc(true);

      return usuarios.stream()
        .map(usuario -> modelMapper.map(usuario, UsuarioDto.Usuario.class))
        .collect(Collectors.toList()); 
    } catch (Exception e) {
      throw new InternalServerErrorException("Error al obtener los registros: " + e.getMessage());
    }
  }

  @Override
  public UsuarioDto.Usuario findById(Long id) {
    Usuario usuario = usuarioRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));    

    return modelMapper.map(usuario, UsuarioDto.Usuario.class);
  }

  private void verificarUsuarioNoExiste(String usuario){
    if (usuarioRepository.existsByUsuario(usuario)) {
      throw new ConflictException("El usuario " + usuario +  " ya existe");
    }
  }

  @Override
  public void modificarClave(CambiarClave bodyDto) {
    try {
      Usuario usuario = usuarioRepository.findById(bodyDto.getIdUsuario())
        .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      usuario.setClave(passwordEncoder.encode(bodyDto.getClave()));

      usuarioRepository.save(usuario);
    } catch (DataIntegrityViolationException e) {
      throw new BadRequestException("Datos inválidos o incompletos");
    } catch (MappingException e) {
      throw new BadRequestException("Error en el mapeo de datos");
    } catch (Exception e) {
      throw new InternalServerErrorException("Error inesperado al modificar el registro " + e.getMessage());
    }
  }

  @Override
  public UsuarioDto.Usuario findByCorreo(String correo) {
    Usuario usuario = usuarioRepository.findByCorreo(correo)
    .orElseThrow(() -> new NotFoundException("El correo: " + correo + " no se encuentra registrado"));

    return modelMapper.map(usuario, UsuarioDto.Usuario.class);
  }
  
}