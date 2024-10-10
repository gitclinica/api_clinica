package com.mzenteno.clinica.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mzenteno.clinica.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  
  boolean existsByUsuario(String usuario);

  boolean existsByCorreo(String correo);

  Optional<Usuario> findByUsuario(String usuario);

  Optional<Usuario> findByCorreo(String correo);

  List<Usuario> findByEstadoOrderByUsuarioDesc(boolean estado);
  
}