package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.PedidoProduto;
import br.com.postech.techchallange.domain.port.in.BuscarPedidoProdutoPorPedidoUseCase;
import br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarPedidoProdutoPorPedidoService implements BuscarPedidoProdutoPorPedidoUseCase {

	private final PedidoProdutoRepositoryPort pedidoProdutoRepository;

	@Override
	public List<PedidoProduto> buscarPorIdPedido(Long idPedido) {
		return pedidoProdutoRepository.buscarPorIdPedido(idPedido);
	}
}
