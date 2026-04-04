package com.app.api_servicos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.api_servicos.model.Servico;
import com.app.api_servicos.repository.ServicoRepository;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> listarAtivos() {
        return servicoRepository.findByAtivoTrue();
    }

    public Servico salvar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public Servico atualizar(Long id, Servico novosDados) {
        Servico existente = servicoRepository.findById(id).orElseThrow();
        existente.setNome(novosDados.getNome());
        existente.setPreco(novosDados.getPreco());
        existente.setDuracaoMinutos(novosDados.getDuracaoMinutos());
        return servicoRepository.save(existente);
    }

    // Método solicitado: desativar em vez de excluir fisicamente
    public void desativar(Long id) {
        Servico servico = servicoRepository.findById(id).orElseThrow();
        servico.setAtivo(false);
        servicoRepository.save(servico);
    }
}
