package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Cliente salvar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    List<Cliente> listarTodos();
}
