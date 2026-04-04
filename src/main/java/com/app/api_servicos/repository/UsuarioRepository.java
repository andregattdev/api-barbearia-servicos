package com.app.api_servicos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.api_servicos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);

}
