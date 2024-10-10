package com.mzenteno.clinica.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/home")
public class HomeController {
  
  @GetMapping(value = "/saludo", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> findAll() {
/*     BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String encodedPassword = passwordEncoder.encode("mzenteno");
    System.out.println(encodedPassword); */

    String saludo = "hola marcelo, como estas?";
    return ResponseEntity.status(HttpStatus.OK).body(saludo);
  }
}
 