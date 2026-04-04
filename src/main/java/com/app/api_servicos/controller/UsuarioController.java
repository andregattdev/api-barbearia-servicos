package com.app.api_servicos.controller;

import com.app.api_servicos.service.UsuarioService;
import com.app.api_servicos.dto.DadosRegistroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/registrar")
    public ResponseEntity registrar(@RequestBody DadosRegistroDTO dados) {
        try {
            service.registrar(dados);
            return ResponseEntity.ok().build(); // Retorna 200 OK se tudo der certo
        } catch (RuntimeException e) {
            // Se o usuário já existir (erro lançado no Service), retorna 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}