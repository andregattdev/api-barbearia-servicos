package com.app.api_servicos.controller;

import com.app.api_servicos.model.Agendamento;
<<<<<<< HEAD
import com.app.api_servicos.model.Usuario;
=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
import com.app.api_servicos.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.security.core.context.SecurityContextHolder;
=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin("*") // Essencial para o Angular não ser bloqueado pelo navegador
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping
    public List<Agendamento> listar() {
        return agendamentoService.listarAtivos();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Agendamento agendamento) {
        try {
            Agendamento novoAgendamento = agendamentoService.agendar(agendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAgendamento);
        } catch (RuntimeException e) {
            // Retorna o erro de conflito de horário com status 400 (Bad Request)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public Agendamento atualizarStatus(@PathVariable Long id, @RequestParam String novoStatus) {
        return agendamentoService.atualizarStatus(id, novoStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        agendamentoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/faturamento")
    public Double getFaturamento() {
        return agendamentoService.obterFaturamentoTotal();
    }

    @GetMapping("/faturamento/periodo")
    public ResponseEntity<Double> getFaturamentoPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        return ResponseEntity.ok(agendamentoService.calcularFaturamentoPorPeriodo(inicio, fim));
    }

    @GetMapping("/faturamento/hoje")
    public ResponseEntity<Double> getFaturamentoHoje() {
        return ResponseEntity.ok(agendamentoService.faturamentoDeHoje());
    }

    @GetMapping("/faturamento/total") 
    public Double getFaturamentoTotal() {
        return agendamentoService.obterFaturamentoTotal();
    }

<<<<<<< HEAD
    @GetMapping("/meus")
    public ResponseEntity<List<Agendamento>> listarMeusAgendamentos() {
        // Recupera o usuário logado do contexto de segurança
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Usuario usuario) {
            List<Agendamento> meusAgendamentos = agendamentoService.listarPorUsuario(usuario);
            return ResponseEntity.ok(meusAgendamentos);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
}