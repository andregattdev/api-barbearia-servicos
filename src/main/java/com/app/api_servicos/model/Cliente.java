package com.app.api_servicos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "tb_cliente")
@Data 
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(unique = true) // Importante para não duplicar cadastros
    private String email;

    @Column(nullable = false, length = 20)
    private String telefone; // Formato esperado: "5511999999999" para facilitar WhatsApp

    private LocalDate dataNascimento;

    @Column(columnDefinition = "TEXT")
    private String observacoes; // Para o profissional anotar alergias, preferências, etc.

    private LocalDate dataCadastro = LocalDate.now();

    private boolean ativo = true; // Para desativar clientes sem excluir do banco de dados
}