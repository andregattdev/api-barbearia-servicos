package com.app.api_servicos.repository;

import java.time.LocalDateTime;
import java.util.List;

<<<<<<< HEAD
import com.app.api_servicos.model.Usuario;
=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.api_servicos.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

        List<Agendamento> findByAtivoTrue();

<<<<<<< HEAD
        List<Agendamento> findByUsuarioAndAtivoTrue(Usuario usuario);

=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
        @Query("SELECT a FROM Agendamento a WHERE a.ativo = true " +
                        "AND (a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio) " +
                        "ORDER BY a.dataHoraInicio ASC")
        List<Agendamento> buscarConflitosNoPeriodo(LocalDateTime inicio, LocalDateTime fim);

        // Verifica se existe algum agendamento que conflita com o horário desejado
        @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE " +
                        "(a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio)")
        boolean isHorarioOcupado(LocalDateTime inicio, LocalDateTime fim);

        @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.ativo = true AND " +
                        "(:inicio < a.dataHoraFim AND :fim > a.dataHoraInicio)")
        boolean existsByConflitoHorario(LocalDateTime inicio, LocalDateTime fim);

        // Esta consulta soma o preço de todos os serviços cujos agendamentos estão
        // concluídos e ativos
        @Query("SELECT SUM(s.preco) FROM Agendamento a JOIN a.servico s WHERE a.ativo = true AND a.status = 'CONCLUIDO'")
        Double calcularFaturamentoTotal();

        @Query("SELECT SUM(s.preco) FROM Agendamento a JOIN a.servico s " +
                        "WHERE a.ativo = true " +
                        "AND a.status = 'CONCLUIDO' " +
                        "AND a.dataHoraInicio >= :inicio " +
                        "AND a.dataHoraInicio <= :fim")
        Double somarFaturamentoPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

}
