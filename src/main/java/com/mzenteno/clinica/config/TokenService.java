package com.mzenteno.clinica.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mzenteno.clinica.entity.Usuario;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class TokenService {
  @Value("${api.security.token.secret}")
  private String secret;
    public String generateToken(Usuario usuario){
      try {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
          .withIssuer("login-auth-api")
          .withSubject(usuario.getUsuario())
          .withExpiresAt(this.generateExpirationDate())
          .sign(algorithm);
        return token;
      } catch (JWTCreationException exception){
        throw new RuntimeException("Error while authenticating");
      }
    }

    public String validateToken(String token){
      try {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
          .withIssuer("login-auth-api")
          .build()
          .verify(token)
          .getSubject();
      } catch (JWTVerificationException exception) {
        throw new RuntimeException("Error while validate");
      }
    }

    private Instant generateExpirationDate(){
      return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
