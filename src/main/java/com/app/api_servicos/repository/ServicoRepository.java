package com.app.api_servicos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.api_servicos.model.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByAtivoTrue();

}
