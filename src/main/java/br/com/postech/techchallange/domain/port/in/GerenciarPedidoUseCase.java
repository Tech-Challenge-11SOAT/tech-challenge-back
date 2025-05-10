package br.com.postech.techchallange.domain.port.in;

import java.util.List;

import br.com.postech.techchallange.adapter.in.rest.response.PedidoCompletoResponse;
import br.com.postech.techchallange.domain.model.Pedido;

public interface GerenciarPedidoUseCase {
	Pedido criarPedido(Pedido pedido);

	Pedido buscarPedido(Long id);

	List<PedidoCompletoResponse> listarPedidos();
	
	List<PedidoCompletoResponse> listarPorStatus(Long statusId);
	
	Pedido atualizarPedido(Pedido pedido, Long statusId);
}
