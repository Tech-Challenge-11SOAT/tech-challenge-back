package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.adapter.in.rest.request.AtualizarPedidoRequest;
import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.model.Pedido;

public interface OrquestradorPedidoPagamentoUseCase {
	PedidoPagamentoResponse orquestrarPedidoPagamento(PedidoPagamentoRequest request);

	Pedido atualizarPedidoPagamento(AtualizarPedidoRequest request);
}
