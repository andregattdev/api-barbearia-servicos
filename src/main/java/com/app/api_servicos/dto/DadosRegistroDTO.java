package com.app.api_servicos.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosRegistroDTO(
    
    @NotBlank 
    String login, 

    @NotBlank 
    String senha
) {}
