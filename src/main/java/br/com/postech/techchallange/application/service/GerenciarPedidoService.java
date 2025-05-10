package br.com.postech.techchallange.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.adapter.in.rest.response.PedidoCompletoResponse;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import br.com.postech.techchallange.domain.port.out.PedidoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GerenciarPedidoService implements GerenciarPedidoUseCase {

	private final PedidoRepositoryPort pedidoRepository;

	@Override
	public Pedido criarPedido(Pedido pedido) {
		return this.pedidoRepository.salvar(pedido);
	}

	@Override
	public Pedido buscarPedido(Long id) {
		return this.pedidoRepository.buscarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Pedido com id %s não encontrato", id)));
	}

	@Override
	public List<PedidoCompletoResponse> listarPedidos() {
		return this.pedidoRepository.listarTodos();
	}

	@Override
	public Pedido atualizarPedido(Pedido pedido, Long statusId) {
		return this.pedidoRepository.atualizar(pedido, statusId);
	}
}
