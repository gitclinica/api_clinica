package com.mzenteno.clinica.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mzenteno.clinica.config.TokenService;
import com.mzenteno.clinica.dto.JwtDto;
import com.mzenteno.clinica.dto.UsuarioDto;
import com.mzenteno.clinica.entity.Usuario;
import com.mzenteno.clinica.error.exception.BadRequestException;
import com.mzenteno.clinica.error.exception.JwtAuthenticationException;
import com.mzenteno.clinica.error.exception.NotFoundException;
import com.mzenteno.clinica.repository.UsuarioRepository;
import com.mzenteno.clinica.service.EmailService;
import com.mzenteno.clinica.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UsuarioService usuarioService;
  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final EmailService emailService;
  private final ModelMapper modelMapper;

  @PostMapping("/login")
  public ResponseEntity<JwtDto.JwtResponse> login(@Valid @RequestBody JwtDto.JwtRequest body) {
    Usuario usuario = usuarioRepository.findByUsuario(body.getUserName())
      .orElseThrow(() -> new NotFoundException("Usuario con nombre: " + body.getUserName() + " no encontrado"));

    if(passwordEncoder.matches(body.getPassword(), usuario.getClave())) {
      String token = this.tokenService.generateToken(usuario);
      return ResponseEntity.status(HttpStatus.OK).body(new JwtDto.JwtResponse(usuario.getUsuario(), token));
    }

    throw new JwtAuthenticationException("Nombre de Usuario o Clave no válidos.");
  }

  @PostMapping(value = "/enviar-correo-nuevo-password", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> RecuperarPassword(@RequestParam String correo){
    try {
      Usuario usuario = usuarioRepository.findByCorreo(correo)
      .orElseThrow(() -> new NotFoundException("El correo: " + correo + " no se encuentra registrado"));

      String urlRecover = "http://localhost:5173/cambiar-password?token=" + this.tokenService.generateToken(usuario);
      emailService.sendEmail(correo, "Restablecer contraseña", "<html>\n" +
      "  <body>\n" +
      "    <h1>Restablecer tu contraseña</h1>\n" +
      "    <p>Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.</p>\n" +
      "    <p>Por favor, haz clic en el siguiente enlace para establecer una nueva contraseña:</p>" +
      "    <a href=\" " + urlRecover + " \">Restablecer mi contraseña</a>\n" +
      "    <p>Este enlace solo es válido dentro de los próximos 30 minutos.</p>\n" +
      "    <p>Saludos.</p>\n" +
      "  </body>\n" +
      "</html>");

      return ResponseEntity.status(HttpStatus.OK).body("Correo enviado correctamente");  
    } catch (Exception e) {
      throw new BadRequestException("Ocurrió un problema al enviar el correo");
    }
  }

  @GetMapping("/validar-token-nuevo-password")
  public ResponseEntity<UsuarioDto.Usuario> validarTokenNuevaClave(@RequestParam("token") String token){
    try {
      String usuarioToken = this.tokenService.validateToken(token);

      Usuario usuario = usuarioRepository.findByUsuario(usuarioToken)
        .orElseThrow(() -> new NotFoundException("Usuario con nombre: " + usuarioToken + " no encontrado"));
  
      UsuarioDto.Usuario usuarioDto = modelMapper.map(usuario, UsuarioDto.Usuario.class);
      return ResponseEntity.status(HttpStatus.OK).body(usuarioDto);  
    } catch (Exception e) {
      throw new BadRequestException("Token no válido");
    }
  }

  @PostMapping(value = "/cambiar-password", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> modificarClave(@Valid @RequestBody UsuarioDto.CambiarClave bodyDto){
    usuarioService.modificarClave(bodyDto);
    return ResponseEntity.status(HttpStatus.OK).body("Se modificó la contraseña exitosamente");
  }

}