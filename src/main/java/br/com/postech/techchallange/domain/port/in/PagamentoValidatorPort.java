package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;

public interface PagamentoValidatorPort {
	void validar(Pedido pedido, Pagamento pagamento);
}
