package com.app.api_servicos.controller;

import com.app.api_servicos.dto.AgendamentoDTO;
import com.app.api_servicos.model.Usuario;
import com.app.api_servicos.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin("*") // Essencial para o Angular não ser bloqueado pelo navegador
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    // ADMIN vê todos os agendamentos ativos
    @GetMapping
    public List<AgendamentoDTO> listar() {
        return agendamentoService.listarAtivosDTO();
    }

    // Qualquer usuário logado pode criar
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody com.app.api_servicos.model.Agendamento agendamento) {
        try {
            AgendamentoDTO novoAgendamento = agendamentoService.agendar(agendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAgendamento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ADMIN altera status
    @PatchMapping("/{id}/status")
    public AgendamentoDTO atualizarStatus(@PathVariable Long id, @RequestParam String novoStatus) {
        return agendamentoService.atualizarStatusDTO(id, novoStatus);
    }

    // ADMIN ou CLIENTE cancela
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        agendamentoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    // CLIENTE vê seus próprios agendamentos ativos
    @GetMapping("/meus")
    public ResponseEntity<List<AgendamentoDTO>> listarMeusAgendamentos() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Usuario usuario) {
            List<AgendamentoDTO> meusAgendamentos = agendamentoService.listarPorUsuarioDTO(usuario);
            return ResponseEntity.ok(meusAgendamentos);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // ADMIN pode consultar faturamento
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

    // ADMIN consulta agendamentos por dia
    @GetMapping("/dia")
    public List<AgendamentoDTO> listarPorDia(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return agendamentoService.listarPorDiaDTO(data);
    }
}
