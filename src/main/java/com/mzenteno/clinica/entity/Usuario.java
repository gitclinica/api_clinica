package com.mzenteno.clinica.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Usuario implements Serializable {

  @Id
  @Column(name = "id_usuario")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idUsuario;

  @NotNull
  @Column(name = "grupo_usuario")
  private Character grupoUsuario;
  
  @NotNull
  @Length(max = 50)
  @Column(name = "nombre_completo")
  private String nombreCompleto;

  @NotNull
  @Length(max = 60)
  @Column(name = "correo")
  private String correo;
  
  @NotNull
  @Length(max = 20)
  @Column(name = "usuario")
  private String usuario;
  
  @NotNull
  @Length(max = 100)
  @Column(name = "clave")
  private String clave;
  
  @Column(name = "estado")
  private boolean estado;
  
}