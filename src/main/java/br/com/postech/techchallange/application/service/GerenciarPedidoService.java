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
		return this.pedidoRepository.salvar(pedido);
	}

	@Override
	public Pedido buscarPedido(Long id) {
		return this.pedidoRepository.buscarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Pedido com id %s não encontrato", id)));
	}

	@Override
	public List<PedidoCompletoResponse> listarPedidos() {
		return Optional.ofNullable(this.pedidoRepository.listarTodos())
				.filter(pedidos -> !pedidos.isEmpty())
				.orElseThrow(() -> new EntityNotFoundException("Nenhum pedido encontrado"));
	}
	
	@Override
	public List<PedidoCompletoResponse> listarPorStatus(Long statusId) {
		return Optional.ofNullable(this.pedidoRepository.listarPorStatus(statusId))
				.filter(pedidos -> !pedidos.isEmpty())
				.orElseThrow(() -> new EntityNotFoundException(String.format("Nenhum pedido encontrado para o status %s", statusId)));
	}

	@Override
	public Pedido atualizarPedido(Pedido pedido, Long statusId) {
		return this.pedidoRepository.atualizar(pedido, statusId);
	}
}
