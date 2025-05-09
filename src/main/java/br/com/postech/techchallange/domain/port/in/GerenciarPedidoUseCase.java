package br.com.postech.techchallange.domain.port.in;

import java.util.List;

import br.com.postech.techchallange.domain.model.Pedido;

public interface GerenciarPedidoUseCase {
	Pedido criarPedido(Pedido pedido);

	Pedido buscarPedido(Long id);

	List<Pedido> listarPedidos();
	
	Pedido atualizarPedido(Pedido pedido, Long statusId);
}
