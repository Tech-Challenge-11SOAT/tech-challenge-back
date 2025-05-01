package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.StatusPedido;

import java.util.List;
import java.util.Optional;

public interface StatusPedidoRepositoryPort {
	Optional<StatusPedido> buscarPorId(Long id);

	Optional<StatusPedido> buscarPorNome(String nome);

	List<StatusPedido> listaTodos();
}