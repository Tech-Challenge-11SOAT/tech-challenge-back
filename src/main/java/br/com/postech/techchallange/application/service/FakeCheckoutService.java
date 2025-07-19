package br.com.postech.techchallange.application.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.postech.techchallange.domain.port.in.GerenciarClienteUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.postech.techchallange.adapter.in.rest.request.FakeCheckoutRequest;
import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.OrderResponseDTO;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.model.OrdemPagamento;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.in.CriarOrdemMercadoPagoUseCase;
import br.com.postech.techchallange.domain.port.in.FakeCheckoutUseCase;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FakeCheckoutService implements FakeCheckoutUseCase {

	private static final Logger log = LoggerFactory.getLogger(FakeCheckoutService.class);

	private final OrquestradorPedidoPagamentoService orquestradorPedidoPagamentoService;
	private final CriarOrdemMercadoPagoUseCase criarOrdemMercadoPagoUseCase;
	private final ProdutoRepositoryPort produtoRepositoryPort;
	private final GerenciarClienteUseCase gerenciarClienteUseCase;

	@Override
	public PedidoPagamentoResponse processarFakeCheckout(FakeCheckoutRequest request) {
		enviarParaFila(request);

		// Primeiro processa o pedido e pagamento normalmente
		PedidoPagamentoRequest pagamentoRequest = converterFakeCheckoutParaPedidoPagamento(request);
		PedidoPagamentoResponse response = orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(pagamentoRequest);

		// Após todas as validações serem bem-sucedidas, integra com Mercado Pago
		try {
			BigDecimal totalAmount = calcularTotalPedido(request);
			String payerEmail = determinarEmailPagador(request);

			log.info("Iniciando integração com Mercado Pago para pedido: {}", response.getIdPedido());

			OrdemPagamento ordemPagamento = criarOrdemMercadoPagoUseCase.criarOrdemPagamento(
				response.getIdPedido(),
				totalAmount,
				payerEmail
			);

			// Adiciona informações do Mercado Pago à resposta
			OrderResponseDTO orderResponse = OrderResponseDTO.builder()
				.orderId(ordemPagamento.getId())
				.externalReference(ordemPagamento.getExternalReference())
				.totalAmount(ordemPagamento.getTotalAmount())
				.status(ordemPagamento.getStatus())
				.qrCode(ordemPagamento.getQrCode())
				.qrCodeBase64(ordemPagamento.getQrCodeBase64())
				.ticketUrl(ordemPagamento.getTicketUrl())
				.dateCreated(ordemPagamento.getDateCreated())
				.dateLastUpdated(ordemPagamento.getDateLastUpdated())
				.build();

			response.setOrderResponse(orderResponse);

			log.info("Integração com Mercado Pago concluída com sucesso para pedido: {}", response.getIdPedido());

		} catch (Exception e) {
			log.error("Erro na integração com Mercado Pago para pedido: {}. Continuando sem integração.",
				response.getIdPedido(), e);
			// Não propaga o erro para não quebrar o fluxo principal
		}

		return response;
	}

	private BigDecimal calcularTotalPedido(FakeCheckoutRequest request) {
		return request.getProdutos().stream()
			.map(item -> {
				Produto produto = produtoRepositoryPort.buscarPorId(item.getIdProduto())
					.orElseThrow(() -> new RuntimeException("Produto não encontrado: " + item.getIdProduto()));
				return produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
			})
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private String determinarEmailPagador(FakeCheckoutRequest request) {
		if (request.getIdCliente() == null) return "test@gmail.com";

		var cliente = gerenciarClienteUseCase.buscarCliente(request.getIdCliente());
		if (cliente != null && cliente.getCpfCliente() != null) {
			return cliente.getEmailCliente();
		}

		return "test@gmail.com";
	}

	private PedidoPagamentoRequest converterFakeCheckoutParaPedidoPagamento(FakeCheckoutRequest request) {
		PedidoPagamentoRequest pagamentoRequest = new PedidoPagamentoRequest();

		if (request.getIdCliente() != null) {
			pagamentoRequest.setIdCliente(request.getIdCliente());
		}
		List<PedidoPagamentoRequest.ItemProdutoRequest> produtosConvertidos = request.getProdutos().stream()
				.map(prod -> {
					PedidoPagamentoRequest.ItemProdutoRequest item = new PedidoPagamentoRequest.ItemProdutoRequest();
					item.setIdProduto(prod.getIdProduto());
					item.setQuantidade(prod.getQuantidade());
					return item;
				}).collect(java.util.stream.Collectors.toList());
		pagamentoRequest.setProdutos(produtosConvertidos);
		pagamentoRequest.setMetodoPagamento(request.getMetodoPagamento());
		return pagamentoRequest;
	}

	public void enviarParaFila(FakeCheckoutRequest request) {
		// Simula envio para fila
		log.info("Fake checkout enviado para fila: Cliente={}, Produtos={}", request.getIdCliente(),
				request.getProdutos());
	}
}
