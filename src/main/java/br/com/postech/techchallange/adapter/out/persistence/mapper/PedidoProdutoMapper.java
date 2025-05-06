package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoProdutoEntity;
import br.com.postech.techchallange.domain.model.PedidoProduto;

public class PedidoProdutoMapper {

	public static PedidoProduto toDomain(PedidoProdutoEntity entity) {
		return new PedidoProduto(
				entity.getId(),
				entity.getIdPedido(),
				entity.getIdProduto(),
				entity.getQuantidade(),
				entity.getPrecoUnitario(
		));
	}

	public static PedidoProdutoEntity toEntity(PedidoProduto pedidoProduto) {
		return new PedidoProdutoEntity(
				pedidoProduto.getId(),
				pedidoProduto.getIdPedido(),
				pedidoProduto.getIdProduto(),
				pedidoProduto.getQuantidade(),
				pedidoProduto.getPrecoUnitario()
		);
	}
}