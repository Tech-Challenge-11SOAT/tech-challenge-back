package br.com.postech.techchallange.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.postech.techchallange.adapter.out.persistence.mapper.StatusPedidoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.StatusPedidoJpaRepository;
import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.out.StatusPedidoRepositoryPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StatusPedidoRepositoryAdapter implements StatusPedidoRepositoryPort {

	private final StatusPedidoJpaRepository repository;

	@Override
	public Optional<StatusPedido> buscarPorId(Long id) {
		return this.repository.findById(id)
				.map(StatusPedidoMapper::toDomain);
	}

	@Override
	public Optional<StatusPedido> buscarPorNome(String nome) {
		return this.repository.findByNomeStatusIgnoreCase(nome)
				.map(StatusPedidoMapper::toDomain);
	}
	
	@Override
	public List<StatusPedido> listaTodos() {
		return this.repository.findAll()
				.stream()
				.map(StatusPedidoMapper::toDomain)
				.toList();
	}
}