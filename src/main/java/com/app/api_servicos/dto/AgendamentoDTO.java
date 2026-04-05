package com.app.api_servicos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoDTO {

    private Long id;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String descricao;
    private String status;

    private ClienteDTO cliente;
    private ServicoDTO servico;
}
