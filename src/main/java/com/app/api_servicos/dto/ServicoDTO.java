package com.app.api_servicos.dto;

import lombok.Data;

@Data
public class ServicoDTO {
    private Long id;
    private String nome;
    private Double preco;
    private Integer duracaoMinutos;
}
