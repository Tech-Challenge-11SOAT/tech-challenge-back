package br.com.postech.techchallange.domain.port.in;

import java.util.List;

import br.com.postech.techchallange.domain.model.StatusPedido;

public interface GerenciarStatusPedidoUseCase {
	StatusPedido buscarStatusPedidoPorId(Long id);

	StatusPedido buscarStatusPedidoPorNome(String nome);

	List<StatusPedido> listarTodos();
}