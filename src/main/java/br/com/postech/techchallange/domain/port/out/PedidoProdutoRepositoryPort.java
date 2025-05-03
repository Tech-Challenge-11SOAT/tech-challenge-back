package br.com.postech.techchallange.domain.port.out;

import java.util.List;

import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoProdutoEntity;
import br.com.postech.techchallange.domain.model.PedidoProduto;

public interface PedidoProdutoRepositoryPort {
	List<PedidoProduto> buscarPorIdPedido(Long idPedido);

	PedidoProdutoEntity salvarItemPedido(PedidoProduto item);
}