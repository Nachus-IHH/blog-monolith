package com.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
