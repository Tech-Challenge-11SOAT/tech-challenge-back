package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;

public interface OrquestradorPedidoPagamentoUseCase {
	PedidoPagamentoResponse orquestrarPedidoPagamento(PedidoPagamentoRequest request);
}
