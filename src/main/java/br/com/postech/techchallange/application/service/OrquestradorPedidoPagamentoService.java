package br.com.postech.techchallange.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.enums.StatusPedidoEnum;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.PedidoProduto;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.OrquestradorPedidoPagamentoUseCase;
import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrquestradorPedidoPagamentoService implements OrquestradorPedidoPagamentoUseCase {

	private final GerenciarPedidoUseCase gerenciarPedido;
	private final GerenciarStatusPedidoUseCase gerenciarStatusPedido;
	private final PedidoProdutoRepositoryPort pedidoProdutoRepository;
	private final ProdutoRepositoryPort produtoRepository;
	private final PagamentoRepositoryPort pagamentoRepository;
	private final MercadoPagoPort mercadoPagoPort;
	private final GerenciarStatusPagamentoUseCase gerenciarStatusPagamento;

	@Override
	@Transactional
	public PedidoPagamentoResponse orquestrarPedidoPagamento(PedidoPagamentoRequest request) {
		StatusPedido status = gerenciarStatusPedido.buscarStatusPedidoPorNome(StatusPedidoEnum.RECEBIDO.getStatus());

		Pedido pedido = new Pedido(
				null,
				request.getIdCliente(),
				LocalDateTime.now(),
				status.getIdStatusPedido(),
				LocalDateTime.now()
		);

		pedido = gerenciarPedido.criarPedido(pedido);
		final Pedido finalPedido = pedido;

		List<PedidoProduto> itens = request.getProdutos().stream().map(prod -> {
			Produto produto = produtoRepository.buscarPorId(prod.getIdProduto()).get();
			if (Objects.isNull(produto)) {
				throw new EntityNotFoundException("Produto não encontrado");
			}

			return new PedidoProduto(
					null,
					finalPedido.getId(),
					prod.getIdProduto(),
					prod.getQuantidade(),
					produto.getPreco()
			);
		}).collect(Collectors.toList());

		itens.forEach(item -> this.pedidoProdutoRepository.salvarItemPedido(item));

		// Calcular total
		var total = itens.stream()
				.map(i -> i.getPrecoUnitario().multiply(java.math.BigDecimal.valueOf(i.getQuantidade())))
				.reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

		Long idStatusPagamento = this.gerenciarStatusPagamento
				.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.PENDENTE.getStatus())
				.getIdStatusPagamento();

		Pagamento pagamento = Pagamento.builder()
				.idPedido(pedido.getId())
				.metodoPagamento(request.getMetodoPagamento())
				.valorTotal(total)
				.dataPagamento(LocalDateTime.now())
				.idStatusPagamento(idStatusPagamento)
				.build();

		pagamento = this.pagamentoRepository.salvar(pagamento);
		var initPoint = this.mercadoPagoPort.criarPreferenciaPagamento(pagamento);

		return PedidoPagamentoResponse.builder()
				.idPedido(pedido.getId())
				.idPagamento(pagamento.getId())
				.metodoPagamento(pagamento.getMetodoPagamento())
				.status(StatusPagamentoEnum.PENDENTE.getStatus())
				.initPoint(initPoint.getInitPoint())
				.build();
	}
}