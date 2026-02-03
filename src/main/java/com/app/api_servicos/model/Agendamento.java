package com.app.api_servicos.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_agendamento")
@Getter
@Setter
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    private String descricao; // Detalhes adicionais do agendamento

    private String status; // Ex: "CONFIRMADO", "CANCELADO", "CONCLUIDO"

    private boolean ativo = true; // Para o Soft Delete

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}