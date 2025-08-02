package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.adapter.in.rest.request.FakeCheckoutRequest;
import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.OrderResponseDTO;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.model.OrdemPagamento;
import br.com.postech.techchallange.domain.port.in.CriarOrdemMercadoPagoUseCase;
import br.com.postech.techchallange.domain.port.in.FakeCheckoutUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarClienteUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FakeCheckoutService implements FakeCheckoutUseCase {

	private static final Logger log = LoggerFactory.getLogger(FakeCheckoutService.class);

	private final OrquestradorPedidoPagamentoService orquestradorPedidoPagamentoService;
	private final CriarOrdemMercadoPagoUseCase criarOrdemMercadoPagoUseCase;
	private final GerenciarClienteUseCase gerenciarClienteUseCase;
	private final GerenciarPagamentoUseCase pagamentoUseCase;

	@Override
	public PedidoPagamentoResponse processarFakeCheckout(FakeCheckoutRequest request) {
		PedidoPagamentoRequest pagamentoRequest = this.converterFakeCheckoutParaPedidoPagamento(request);
		PedidoPagamentoResponse response = this.orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(pagamentoRequest);

		// Após todas as validações serem bem-sucedidas, integra com Mercado Pago
		try {
			BigDecimal totalAmount = this.calcularTotalAmount(response);
			String payerEmail = this.getEmailPagador(request);

			log.info("Iniciando integração com Mercado Pago para pedido: {}", response.getIdPedido());

			OrdemPagamento ordemPagamento = this.criarOrdemMercadoPagoUseCase.criarOrdemPagamento(
				response.getIdPedido(),
				totalAmount,
				payerEmail
			);

			// Adiciona informações do Mercado Pago à resposta
			OrderResponseDTO orderResponse = OrderResponseDTO.builder()
				.orderId(ordemPagamento.getId())
					.type(ordemPagamento.getType())
					.status(ordemPagamento.getStatus())
					.statusDetail(ordemPagamento.getStatusDetail())
				.externalReference(ordemPagamento.getExternalReference())
				.totalAmount(ordemPagamento.getTotalAmount())
					.processingMode(ordemPagamento.getProcessingMode())
					.countryCode(ordemPagamento.getCountryCode())
					.userId(ordemPagamento.getUserId())
					.captureMode(ordemPagamento.getCaptureMode())
					.currency(ordemPagamento.getCurrency())
					.createdDate(ordemPagamento.getDateCreated())
					.lastUpdatedDate(ordemPagamento.getDateLastUpdated())
					.applicationId(ordemPagamento.getApplicationId())
					.paymentId(ordemPagamento.getPaymentId())
					.paymentStatus(ordemPagamento.getPaymentStatus())
					.paymentStatusDetail(ordemPagamento.getPaymentStatusDetail())
					.paymentAmount(ordemPagamento.getPaymentAmount())
					.expirationTime(ordemPagamento.getExpirationTime())
					.dateOfExpiration(ordemPagamento.getDateOfExpiration())
					.referenceId(ordemPagamento.getReferenceId())
					.paymentMethodId(ordemPagamento.getPaymentMethodId())
					.paymentMethodType(ordemPagamento.getPaymentMethodType())
				.qrCode(ordemPagamento.getQrCode())
				.qrCodeBase64(ordemPagamento.getQrCodeBase64())
				.ticketUrl(ordemPagamento.getTicketUrl())
				.build();

			response.setOrderResponse(orderResponse);

			log.info("Integração com Mercado Pago concluída com sucesso para pedido: {}", response.getIdPedido());
		} catch (Exception e) {
			log.error("Erro na integração com Mercado Pago para pedido: {}. Continuando sem integração.", response.getIdPedido(), e);
			// Não propaga o erro para não quebrar o fluxo principal
		}

		return response;
	}

	private BigDecimal calcularTotalAmount(PedidoPagamentoResponse response) {
		var pagamento = this.pagamentoUseCase.buscarPagamento(response.getIdPagamento());
		if (pagamento != null) {
			return pagamento.getValorTotal();
		}

		throw new RuntimeException("Pagamento não encontrado para o ID: " + response.getIdPagamento());
	}

	private String getEmailPagador(FakeCheckoutRequest request) {
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
}
