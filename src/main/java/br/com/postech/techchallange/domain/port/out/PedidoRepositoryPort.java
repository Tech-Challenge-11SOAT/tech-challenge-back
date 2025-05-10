package br.com.postech.techchallange.domain.port.out;

import java.util.List;
import java.util.Optional;

import br.com.postech.techchallange.adapter.in.rest.response.PedidoCompletoResponse;
import br.com.postech.techchallange.domain.model.Pedido;

public interface PedidoRepositoryPort {

	Pedido salvar(Pedido pedido);

	Optional<Pedido> buscarPorId(Long id);

	List<PedidoCompletoResponse> listarTodos();
	
	List<PedidoCompletoResponse> listarPorStatus(Long statusId);

	Pedido atualizar(Pedido pedido, Long statusId);
}
