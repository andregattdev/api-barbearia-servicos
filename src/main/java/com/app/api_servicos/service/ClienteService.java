package com.app.api_servicos.service;

// CORREÇÃO DOS IMPORTS: Use sempre o pacote .data.domain para paginação
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.api_servicos.model.Cliente;
import com.app.api_servicos.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    
    public Page<Cliente> listarTodos(Pageable pageable) {
        return clienteRepository.findByAtivoTrue(pageable);
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente atualizar(Long id, Cliente dadosAtualizados) {
        Cliente clienteExistente = buscarPorId(id);
        clienteExistente.setNome(dadosAtualizados.getNome());
        clienteExistente.setEmail(dadosAtualizados.getEmail());
        clienteExistente.setTelefone(dadosAtualizados.getTelefone());
        return clienteRepository.save(clienteExistente);
    }

    public void desativar(Long id) {
        Cliente cliente = buscarPorId(id);
        cliente.setAtivo(false);
        // CORREÇÃO: Usamos o repository para salvar a alteração
        clienteRepository.save(cliente); 
    }
}