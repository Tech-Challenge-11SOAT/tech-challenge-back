package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.Pedido;
import java.util.Optional;
import java.util.List;

public interface PedidoRepositoryPort {
    Pedido salvar(Pedido pedido);
    Optional<Pedido> buscarPorId(Long id);
    List<Pedido> listarTodos();
}
