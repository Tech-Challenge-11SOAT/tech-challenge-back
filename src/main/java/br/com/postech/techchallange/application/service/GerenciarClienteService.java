package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.Cliente;
import br.com.postech.techchallange.domain.port.in.GerenciarClienteUseCase;
import br.com.postech.techchallange.domain.port.out.ClienteRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GerenciarClienteService implements GerenciarClienteUseCase {

    private final ClienteRepositoryPort clienteRepository;

    @Override
    public Cliente cadastrarCliente(Cliente cliente) {
        return clienteRepository.salvar(cliente);
    }

    @Override
    public Cliente buscarCliente(Long id) {
        return clienteRepository.buscarPorId(id).orElse(null);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.listarTodos();
    }

}
