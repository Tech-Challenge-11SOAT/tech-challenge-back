package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.out.PedidoRepositoryPort;
import br.com.postech.techchallange.adapter.out.persistence.mapper.PedidoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.PedidoJpaRepository;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

	private final PedidoJpaRepository jpaRepository;

	public PedidoRepositoryAdapter(PedidoJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Pedido salvar(Pedido pedido) {
		var entity = PedidoMapper.toEntity(pedido);
		return PedidoMapper.toDomain(jpaRepository.save(entity));
	}

	@Override
	public Optional<Pedido> buscarPorId(Long id) {
		return jpaRepository.findById(id).map(PedidoMapper::toDomain);
	}

	@Override
	public List<Pedido> listarTodos() {
		return jpaRepository.findAll()
				.stream()
				.map(PedidoMapper::toDomain)
				.toList();
	}

	@Override
	public Pedido atualizar(Pedido pedido, Long statusId) {
		var entity = PedidoMapper.toEntity(pedido);
		entity.setIdStatusPedido(statusId);
		entity.setDataStatus(LocalDateTime.now());

		return Optional.ofNullable(jpaRepository.save(entity))
				.map(PedidoMapper::toDomain)
				.orElseThrow(() -> new IllegalStateException("Falha ao atualizar o pedido com ID: " + pedido.getId()));
	}
}
