package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.domain.model.PedidoProduto;

import java.util.List;

public interface BuscarPedidoProdutoPorPedidoUseCase {
	List<PedidoProduto> buscarPorIdPedido(Long idPedido);
}
