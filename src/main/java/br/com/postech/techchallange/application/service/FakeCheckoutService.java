package br.com.postech.techchallange.application.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.postech.techchallange.adapter.in.rest.request.FakeCheckoutRequest;
import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.port.in.FakeCheckoutUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FakeCheckoutService implements FakeCheckoutUseCase {

	private static final Logger log = LoggerFactory.getLogger(FakeCheckoutService.class);

	private final OrquestradorPedidoPagamentoService orquestradorPedidoPagamentoService;

	@Override
	public PedidoPagamentoResponse processarFakeCheckout(FakeCheckoutRequest request) {
		enviarParaFila(request);
		PedidoPagamentoRequest pagamentoRequest = converterFakeCheckoutParaPedidoPagamento(request);
		return orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(pagamentoRequest);
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
