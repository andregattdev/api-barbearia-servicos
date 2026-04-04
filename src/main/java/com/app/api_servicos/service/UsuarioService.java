package com.app.api_servicos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.api_servicos.dto.DadosRegistroDTO;
import com.app.api_servicos.model.Perfil;
import com.app.api_servicos.model.Usuario;
import com.app.api_servicos.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registrar(DadosRegistroDTO dados) {
        // Usa o seu findByLogin para validar
        if (repository.findByLogin(dados.login()).isPresent()) {
            throw new RuntimeException("Usuário já cadastrado!");
        }

        String senhaCripto = passwordEncoder.encode(dados.senha());

        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(dados.login());
        novoUsuario.setSenha(senhaCripto);
        novoUsuario.setPerfil(Perfil.CLIENTE); 

        repository.save(novoUsuario);
    }
}
