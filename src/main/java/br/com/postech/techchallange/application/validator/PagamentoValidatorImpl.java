package br.com.postech.techchallange.application.validator;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.postech.techchallange.domain.enums.StatusPedidoEnum;
import br.com.postech.techchallange.domain.exception.InvalidPaymentAmountException;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.PedidoProduto;
import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.PagamentoValidatorPort;
import br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PagamentoValidatorImpl implements PagamentoValidatorPort {

	private final GerenciarStatusPedidoUseCase gerenciarStatusPedidoUseCase;
	private final PedidoProdutoRepositoryPort pedidoProdutoRepositoryPort;

	@Override
	public void validar(Pedido pedido, Pagamento pagamento) {
		if (pedido == null) {
			throw new EntityNotFoundException("Pedido não encontrado.");
		}

		StatusPedido statusPedido = gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(pedido.getIdStatusPedido());

		if (!StatusPedidoEnum.RECEBIDO.getStatus().equalsIgnoreCase(statusPedido.getNomeStatus())) {
			throw new IllegalStateException("O pagamento é permitido apenas para pedidos com o status RECEBIDO.");
		}

		List<PedidoProduto> produtos = pedidoProdutoRepositoryPort.buscarPorIdPedido(pedido.getId());
		BigDecimal totalCalculado = produtos.stream()
				.map(prod -> prod.getPrecoUnitario().multiply(BigDecimal.valueOf(prod.getQuantidade())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		if (pagamento.getValorTotal() == null || pagamento.getValorTotal().compareTo(totalCalculado) != 0) {
			throw new InvalidPaymentAmountException();
		}
	}
}
