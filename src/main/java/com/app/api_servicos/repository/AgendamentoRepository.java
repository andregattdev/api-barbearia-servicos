package com.app.api_servicos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.api_servicos.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

        List<Agendamento> findByAtivoTrue();

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
