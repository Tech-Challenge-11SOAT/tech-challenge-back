package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.adapter.in.rest.request.FakeCheckoutRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;

public interface FakeCheckoutUseCase {

	PedidoPagamentoResponse processarFakeCheckout(FakeCheckoutRequest request);
}
