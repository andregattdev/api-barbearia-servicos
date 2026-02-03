package com.app.api_servicos.controller;

import com.app.api_servicos.config.TokenService;
import com.app.api_servicos.model.Usuario;
import com.app.api_servicos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class AuthController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody LoginDTO dados) {
        // 1. Busca o usuário pelo login
        Usuario usuario = repository.findByLogin(dados.login())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Verifica se a senha enviada bate com a senha criptografada no banco
        if (passwordEncoder.matches(dados.senha(), usuario.getSenha())) {
            // 3. Gera o token
            String token = tokenService.gerarToken(usuario);
            return ResponseEntity.ok(new TokenResponseDTO(token));
        }

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}

// DTOs auxiliares (pode colocar em arquivos separados se preferir)
record LoginDTO(String login, String senha) { }
record TokenResponseDTO(String token) { }