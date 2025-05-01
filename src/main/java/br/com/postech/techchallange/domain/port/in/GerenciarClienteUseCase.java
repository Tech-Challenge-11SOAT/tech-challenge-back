package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.domain.model.Cliente;
import java.util.List;

public interface GerenciarClienteUseCase {
    Cliente cadastrarCliente(Cliente cliente);
    Cliente buscarCliente(Long id);
    List<Cliente> listarClientes();
}
