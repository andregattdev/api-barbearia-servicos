package com.app.api_servicos.service;

import com.app.api_servicos.dto.AgendamentoDTO;
import com.app.api_servicos.dto.ClienteDTO;
import com.app.api_servicos.dto.ServicoDTO;
import com.app.api_servicos.model.Agendamento;
import com.app.api_servicos.model.Cliente;
import com.app.api_servicos.model.Servico;
import com.app.api_servicos.model.Usuario;
import com.app.api_servicos.repository.AgendamentoRepository;
import com.app.api_servicos.repository.ClienteRepository;
import com.app.api_servicos.repository.ServicoRepository;
import com.app.api_servicos.repository.UsuarioRepository;

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

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Conversão para DTO
    public AgendamentoDTO toDTO(Agendamento agendamento) {
        Cliente cliente = agendamento.getCliente();
        Servico servico = agendamento.getServico();

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(cliente.getId());
        clienteDTO.setNome(cliente.getNome());
        clienteDTO.setEmail(cliente.getEmail());
        clienteDTO.setTelefone(cliente.getTelefone());

        ServicoDTO servicoDTO = new ServicoDTO();
        servicoDTO.setId(servico.getId());
        servicoDTO.setNome(servico.getNome());
        servicoDTO.setPreco(servico.getPreco());
        servicoDTO.setDuracaoMinutos(servico.getDuracaoMinutos());

        AgendamentoDTO dto = new AgendamentoDTO();
        dto.setId(agendamento.getId());
        dto.setDataHoraInicio(agendamento.getDataHoraInicio());
        dto.setDataHoraFim(agendamento.getDataHoraFim());
        dto.setDescricao(agendamento.getDescricao());
        dto.setStatus(agendamento.getStatus());
        dto.setCliente(clienteDTO);
        dto.setServico(servicoDTO);

        return dto;
    }

    // Criar agendamento com regras de negócio
    public AgendamentoDTO agendar(Agendamento agendamento) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Servico servico = servicoRepository.findById(agendamento.getServico().getId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        Cliente cliente = clienteRepository.findByUsuario(usuarioLogado)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        agendamento.setCliente(cliente);
        agendamento.setUsuario(usuarioLogado);
        agendamento.setServico(servico);
        agendamento.setAtivo(true);
        agendamento.setStatus("CONFIRMADO");

        LocalDateTime fimCalculado = agendamento.getDataHoraInicio()
                .plusMinutes(servico.getDuracaoMinutos());
        agendamento.setDataHoraFim(fimCalculado);

        boolean ocupado = agendamentoRepository.existsByConflitoHorario(
                agendamento.getDataHoraInicio(),
                agendamento.getDataHoraFim());

        if (ocupado) {
            throw new RuntimeException("Este horário já está ocupado.");
        }

        Agendamento salvo = agendamentoRepository.save(agendamento);
        return toDTO(salvo);
    }

    // Listagens
    public List<AgendamentoDTO> listarAtivosDTO() {
        return agendamentoRepository.findByAtivoTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<AgendamentoDTO> listarPorUsuarioDTO(Usuario usuario) {
        return agendamentoRepository.findByUsuarioAndAtivoTrue(usuario)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<AgendamentoDTO> listarPorUsuarioDTOCompleto(Usuario usuario) {
        return agendamentoRepository.findByUsuario(usuario)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<AgendamentoDTO> listarPorDiaDTO(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(23, 59, 59);

        return agendamentoRepository.buscarConflitosNoPeriodo(inicio, fim)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Atualizações de status
    public AgendamentoDTO atualizarStatusDTO(Long id, String novoStatus) {
        Agendamento agendamento = atualizarStatus(id, novoStatus);
        return toDTO(agendamento);
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

    public void desativar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        agendamento.setAtivo(false);
        agendamento.setStatus("CANCELADO");
        agendamentoRepository.save(agendamento);
    }

    // Faturamento
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

    // Bloqueio de agenda
    @Transactional
    public void bloquearAgendaParaCompromisso(LocalDateTime inicio, LocalDateTime fim, String motivo) {
        List<Agendamento> agendamentosAfetados = agendamentoRepository.buscarConflitosNoPeriodo(inicio, fim);

        for (Agendamento a : agendamentosAfetados) {
            a.setAtivo(false);
            a.setStatus("CANCELADO_PELO_BARBEIRO");
        }

        Agendamento bloqueio = new Agendamento();
        bloqueio.setDataHoraInicio(inicio);
        bloqueio.setDataHoraFim(fim);
        bloqueio.setStatus("BLOQUEADO");
        bloqueio.setDescricao("Compromisso: " + motivo);
        bloqueio.setAtivo(true);

        agendamentoRepository.save(bloqueio);
    }
}
