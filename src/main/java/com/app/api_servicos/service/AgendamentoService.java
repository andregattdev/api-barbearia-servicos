package com.app.api_servicos.service;

import com.app.api_servicos.model.Agendamento;
import com.app.api_servicos.model.Cliente;
import com.app.api_servicos.model.Servico;
import com.app.api_servicos.model.Usuario;
import com.app.api_servicos.repository.AgendamentoRepository;
import com.app.api_servicos.repository.ClienteRepository;
import com.app.api_servicos.repository.ServicoRepository;

<<<<<<< HEAD
import com.app.api_servicos.repository.UsuarioRepository;
=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

<<<<<<< HEAD
    @Autowired
    private UsuarioRepository usuarioRepository;

=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
    public List<Agendamento> listarAtivos() {
        return agendamentoRepository.findByAtivoTrue();
    }

    public Agendamento agendar(Agendamento agendamento) {
        // 1. Identifica QUEM está agendando pelo Token
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // 2. Busca o serviço para saber a duração e o preço
        Servico servico = servicoRepository.findById(agendamento.getServico().getId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

<<<<<<< HEAD
        // 3. Vincula cliente ao usuário logado
        Cliente cliente = clienteRepository.findByUsuario(usuarioLogado)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        agendamento.setCliente(cliente);
        agendamento.setUsuario(usuarioLogado);
=======
        // 3. Define os dados automáticos
        agendamento.setUsuario(usuarioLogado); // O dono do agendamento é o cara do Token!
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
        agendamento.setServico(servico);
        agendamento.setAtivo(true);
        agendamento.setStatus("CONFIRMADO");

        // 4. Cálculo de tempo
        LocalDateTime fimCalculado = agendamento.getDataHoraInicio()
                .plusMinutes(servico.getDuracaoMinutos());
        agendamento.setDataHoraFim(fimCalculado);

        // 5. Validação de conflito
        boolean ocupado = agendamentoRepository.existsByConflitoHorario(
                agendamento.getDataHoraInicio(),
                agendamento.getDataHoraFim());

        if (ocupado) {
            throw new RuntimeException("Este horário já está ocupado.");
        }

        return agendamentoRepository.save(agendamento);
    }

<<<<<<< HEAD


=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
    public void desativar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        agendamento.setAtivo(false);
        agendamento.setStatus("CANCELADO");
        agendamentoRepository.save(agendamento);
    }

    public Agendamento atualizarStatus(Long id, String novoStatus) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        agendamento.setStatus(novoStatus);
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento concluirAgendamento(Long id) {
        Agendamento a = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        a.setStatus("CONCLUIDO");
        return agendamentoRepository.save(a);
    }

    public List<Agendamento> listarPorDia(LocalDate data) {
    LocalDateTime inicio = data.atStartOfDay();
    LocalDateTime fim = data.atTime(23, 59, 59);
    
    return agendamentoRepository.buscarConflitosNoPeriodo(inicio, fim); 
}

    public Double obterFaturamentoTotal() {
        Double total = agendamentoRepository.calcularFaturamentoTotal();
        return (total != null) ? total : 0.0;
    }

    
    public Double calcularFaturamentoPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio.isAfter(fim)) {
            throw new RuntimeException("A data inicial não pode ser maior que a final.");
        }
        Double total = agendamentoRepository.somarFaturamentoPorPeriodo(inicio, fim);
        return (total != null) ? total : 0.0;
    }

    public Double faturamentoDeHoje() {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fim = LocalDate.now().atTime(23, 59, 59);
        return calcularFaturamentoPorPeriodo(inicio, fim);
    }

    @Transactional
    public void bloquearAgendaParaCompromisso(LocalDateTime inicio, LocalDateTime fim, String motivo) {
        // 1. Busca todos os agendamentos que "batem" com o horário do compromisso
        List<Agendamento> agendamentosAfetados = agendamentoRepository.buscarConflitosNoPeriodo(inicio, fim);

        // 2. Cancela os agendamentos dos clientes (Aqui o Angular poderia disparar um
        // aviso)
        for (Agendamento a : agendamentosAfetados) {
            a.setAtivo(false);
            a.setStatus("CANCELADO_PELO_BARBEIRO");
        }

        // 3. Cria um registro de "BLOQUEIO" para ninguém conseguir marcar pelo site
        Agendamento bloqueio = new Agendamento();
        bloqueio.setDataHoraInicio(inicio);
        bloqueio.setDataHoraFim(fim);
        bloqueio.setStatus("BLOQUEADO"); // Status especial
        bloqueio.setDescricao("Compromisso: " + motivo);
        bloqueio.setAtivo(true);

        agendamentoRepository.save(bloqueio);
    }

<<<<<<< HEAD
    public List<Agendamento> listarPorUsuario(Usuario usuario) {
        return agendamentoRepository.findByUsuarioAndAtivoTrue(usuario);
    }


=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
}