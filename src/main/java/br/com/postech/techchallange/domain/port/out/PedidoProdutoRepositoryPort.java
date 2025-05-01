package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.PedidoProduto;
import java.util.List;

public interface PedidoProdutoRepositoryPort {
	List<PedidoProduto> buscarPorIdPedido(Long idPedido);
}