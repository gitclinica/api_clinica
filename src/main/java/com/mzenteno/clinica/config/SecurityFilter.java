package com.mzenteno.clinica.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzenteno.clinica.entity.Usuario;
import com.mzenteno.clinica.error.exception.JwtAuthenticationException;
import com.mzenteno.clinica.repository.UsuarioRepository;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.mzenteno.clinica.dto.ErrorMessageDto;
@Component
public class SecurityFilter extends OncePerRequestFilter {
  @Autowired
  TokenService tokenService;

  @Autowired
  UsuarioRepository usuarioRepository;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
    String requestURI = request.getRequestURI();
    String httpMethod = request.getMethod();

    // Saltar el filtro para las rutas configuradas como permitAll en SecurityConfig
    if (requestURI.equals("/api/v1/auth/login") && httpMethod.equals("POST")) {
      filterChain.doFilter(request, response);
      return;
    }
    if (requestURI.equals("/api/v1/auth/enviar-correo-nuevo-password") && httpMethod.equals("POST")) {
      filterChain.doFilter(request, response);
      return;
    }
    if (requestURI.equals("/api/v1/auth/validar-token-nuevo-password") && httpMethod.equals("GET")) {
      filterChain.doFilter(request, response);
      return;
    }
    if (requestURI.equals("/api/v1/auth/cambiar-password") && httpMethod.equals("POST")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      var token = Optional.ofNullable(recoverToken(request))
        .orElseThrow(() -> new JwtAuthenticationException("Token de acceso no proporcionado"));
  
      var login = Optional.ofNullable(tokenService.validateToken(token))
        .orElseThrow(() -> new JwtAuthenticationException("Token de acceso no válido"));

      Usuario usuario = usuarioRepository.findByUsuario(login).orElseThrow(() -> new RuntimeException("User Not Found"));      
      var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
      var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);

      System.out.println("doFilterInternal");

    } catch (JwtAuthenticationException e) {
      handleException(response, e);
    }
  }

  private String recoverToken(HttpServletRequest request){
    var authHeader = request.getHeader("Authorization");
    if(authHeader == null) return null;
    return authHeader.replace("Bearer ", "");
  }

  private void handleException(HttpServletResponse response, JwtAuthenticationException ex) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    
    ErrorMessageDto responseError = new ErrorMessageDto(HttpStatus.UNAUTHORIZED.value(),"Authentication Exception", ex.getMessage(), new Date());
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(responseError);
    response.getWriter().write(json);
  }

}