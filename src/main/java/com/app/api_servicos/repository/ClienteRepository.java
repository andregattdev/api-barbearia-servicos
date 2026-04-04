package com.app.api_servicos.repository;

<<<<<<< HEAD
import com.app.api_servicos.model.Usuario;
=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.api_servicos.model.Cliente;

<<<<<<< HEAD
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Ao passar Pageable, o Spring Data já entende que deve fazer a paginação
    Page<Cliente> findByAtivoTrue(Pageable pageable);

    Optional<Cliente> findByUsuario(Usuario usuario);
=======
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Ao passar Pageable, o Spring Data já entende que deve fazer a paginação
    Page<Cliente> findByAtivoTrue(Pageable pageable);
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
}
