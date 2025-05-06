package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase;
import br.com.postech.techchallange.domain.port.out.StatusPedidoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GerenciarStatusPedidoService implements GerenciarStatusPedidoUseCase {

	private final StatusPedidoRepositoryPort repository;

	@Override
	public StatusPedido buscarStatusPedidoPorId(Long id) {
		return this.repository.buscarPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Status do pedido não encontrado para o ID: " + id));
	}

	@Override
	public StatusPedido buscarStatusPedidoPorNome(String nome) {
		return this.repository.buscarPorNome(nome).orElseThrow(
				() -> new EntityNotFoundException("Status do pedido não encontrado para o nome: " + nome));
	}
	
	@Override
	public List<StatusPedido> listarTodos() {
		return this.repository.listaTodos();
	}
}