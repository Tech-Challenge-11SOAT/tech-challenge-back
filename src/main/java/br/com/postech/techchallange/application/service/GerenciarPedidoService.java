package br.com.postech.techchallange.application.service;

import java.util.List;
import java.util.Optional;

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
		if (pedido.getIdCliente() == null) {
			pedido.setFilaPedido(this.gerarProximoNumeroFilaPedidoAnonimo());
		}
		return this.pedidoRepository.salvar(pedido);
	}

	@Override
	public Pedido buscarPedido(Long id) {
		return this.pedidoRepository.buscarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Pedido com id %s não encontrato", id)));
	}

	@Override
	public List<PedidoCompletoResponse> listarPedidos() {
		List<PedidoCompletoResponse> pedidos = Optional.ofNullable(this.pedidoRepository.listarTodos())
				.filter(list -> !list.isEmpty())
				.orElseThrow(() -> new EntityNotFoundException("Nenhum pedido encontrado"));
		return pedidos.stream().map(p -> p.cliente() != null && p.cliente().id() == null
				? new PedidoCompletoResponse(p.id(), p.dataPedido(), p.dataStatus(), null, p.status(), p.filaPedido())
				: p).toList();
	}

	@Override
	public List<PedidoCompletoResponse> listarPorStatus(Long statusId) {
		List<PedidoCompletoResponse> pedidos = Optional.ofNullable(this.pedidoRepository.listarPorStatus(statusId))
				.filter(list -> !list.isEmpty()).orElseThrow(() -> new EntityNotFoundException(
						String.format("Nenhum pedido encontrado para o status %s", statusId)));
		return pedidos.stream().map(p -> p.cliente() != null && p.cliente().id() == null
				? new PedidoCompletoResponse(p.id(), p.dataPedido(), p.dataStatus(), null, p.status(), p.filaPedido())
				: p).toList();
	}

	@Override
	public Pedido atualizarPedido(Pedido pedido, Long statusId) {
		return this.pedidoRepository.atualizar(pedido, statusId);
	}

	/**
	 * Gera o próximo número sequencial para filaPedido de clientes anônimos (1 a
	 * 500).
	 */
	private Integer gerarProximoNumeroFilaPedidoAnonimo() {
		Integer maxFila = pedidoRepository.buscarMaxFilaPedidoAnonimo();
		if (maxFila == null || maxFila >= 500) {
			return 1;
		}
		return maxFila + 1;
	}
}
