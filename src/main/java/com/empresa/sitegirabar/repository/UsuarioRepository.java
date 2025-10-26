package com.empresa.sitegirabar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.sitegirabar.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByUsuario(String usuario);

}
