package br.com.postech.techchallange.adapter.out.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoProdutoEntity;
import br.com.postech.techchallange.adapter.out.persistence.mapper.PedidoProdutoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.PedidoProdutoJpaRepository;
import br.com.postech.techchallange.domain.model.PedidoProduto;
import br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoProdutoRepositoryAdapter implements PedidoProdutoRepositoryPort {

	private final PedidoProdutoJpaRepository repository;

	@Override
	public List<PedidoProduto> buscarPorIdPedido(Long idPedido) {
		return this.repository.findByIdPedido(idPedido).stream()
				.map(PedidoProdutoMapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public PedidoProdutoEntity salvarItemPedido(PedidoProduto item) {
		return this.repository.save(PedidoProdutoMapper.toEntity(item));
	}
}