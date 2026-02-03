package com.app.api_servicos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.api_servicos.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Ao passar Pageable, o Spring Data já entende que deve fazer a paginação
    Page<Cliente> findByAtivoTrue(Pageable pageable);
}
