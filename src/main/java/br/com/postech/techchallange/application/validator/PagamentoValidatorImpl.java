package br.com.postech.techchallange.application.validator;

import org.springframework.stereotype.Component;

import br.com.postech.techchallange.domain.enums.StatusPedidoEnum;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.PagamentoValidatorPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PagamentoValidatorImpl implements PagamentoValidatorPort {

	private final GerenciarStatusPedidoUseCase gerenciarStatusPedidoUseCase;

	@Override
	public void validar(Pedido pedido, Pagamento pagamento) {
		if (pedido == null) {
			throw new IllegalArgumentException("Pedido não encontrado.");
		}

		StatusPedido statusPedido = gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(pedido.getIdStatusPedido());

		if (!StatusPedidoEnum.RECEBIDO.getStatus().equalsIgnoreCase(statusPedido.getNomeStatus())) {
			throw new IllegalStateException("Pagamento só pode ser feito para pedidos com status RECEBIDO.");
		}

		if (pagamento.getValorTotal() == null || pagamento.getValorTotal().compareTo(pagamento.getValorTotal()) != 0) {
			throw new IllegalArgumentException("Valor do pagamento não confere com o valor do pedido.");
		}
	}
}
