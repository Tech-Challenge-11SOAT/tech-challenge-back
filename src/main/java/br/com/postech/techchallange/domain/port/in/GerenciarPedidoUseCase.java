package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.domain.model.Pedido;
import java.util.List;

public interface GerenciarPedidoUseCase {
	Pedido criarPedido(Pedido pedido);

	Pedido buscarPedido(Long id);

	List<Pedido> listarPedidos();
}
