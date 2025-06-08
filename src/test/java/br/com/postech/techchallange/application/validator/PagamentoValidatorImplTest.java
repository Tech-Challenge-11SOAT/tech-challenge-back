package br.com.postech.techchallange.application.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postech.techchallange.domain.enums.StatusPedidoEnum;
import br.com.postech.techchallange.domain.exception.InvalidPaymentAmountException;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.PedidoProduto;
import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPedidoUseCase;
import br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PagamentoValidatorImplTest {

	@Mock
	private GerenciarStatusPedidoUseCase gerenciarStatusPedidoUseCase;

	@Mock
	private PedidoProdutoRepositoryPort pedidoProdutoRepositoryPort;

	@InjectMocks
	private PagamentoValidatorImpl pagamentoValidator;

	private Pedido pedido;
	private Pagamento pagamento;
	private StatusPedido statusPedido;
	private List<PedidoProduto> produtos;

	@BeforeEach
	void setUp() {
		pedido = new Pedido();
		pedido.setId(1L);
		pedido.setIdStatusPedido(1L);

		pagamento = new Pagamento();
		pagamento.setValorTotal(new BigDecimal("50.00"));

		statusPedido = new StatusPedido();
		statusPedido.setNomeStatus(StatusPedidoEnum.RECEBIDO_NAO_PAGO.getStatus());

		PedidoProduto produto1 = new PedidoProduto();
		produto1.setPrecoUnitario(new BigDecimal("20.00"));
		produto1.setQuantidade(1);

		PedidoProduto produto2 = new PedidoProduto();
		produto2.setPrecoUnitario(new BigDecimal("30.00"));
		produto2.setQuantidade(1);

		produtos = Arrays.asList(produto1, produto2);
	}

	@Test
	void validar_QuandoPedidoNulo_DeveLancarEntityNotFoundException() {
		assertThrows(EntityNotFoundException.class, () -> pagamentoValidator.validar(null, pagamento));
	}

	@Test
	void validar_QuandoStatusNaoRecebidoNaoPago_DeveLancarIllegalStateException() {
		statusPedido.setNomeStatus(StatusPedidoEnum.EM_ANDAMENTO.getStatus());
		when(gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(pedido.getIdStatusPedido())).thenReturn(statusPedido);

		assertThrows(IllegalStateException.class, () -> pagamentoValidator.validar(pedido, pagamento));
	}

	@Test
	void validar_QuandoValorTotalNulo_DeveLancarInvalidPaymentAmountException() {
		pagamento.setValorTotal(null);
		when(gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(pedido.getIdStatusPedido())).thenReturn(statusPedido);
		when(pedidoProdutoRepositoryPort.buscarPorIdPedido(pedido.getId())).thenReturn(produtos);

		assertThrows(InvalidPaymentAmountException.class, () -> pagamentoValidator.validar(pedido, pagamento));
	}

	@Test
	void validar_QuandoValorTotalDiferenteDoCalculado_DeveLancarInvalidPaymentAmountException() {
		pagamento.setValorTotal(new BigDecimal("100.00"));
		when(gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(pedido.getIdStatusPedido())).thenReturn(statusPedido);
		when(pedidoProdutoRepositoryPort.buscarPorIdPedido(pedido.getId())).thenReturn(produtos);

		assertThrows(InvalidPaymentAmountException.class, () -> pagamentoValidator.validar(pedido, pagamento));
	}

	@Test
    void validar_QuandoTodosOsCamposValidos_DevePassarNaValidacao() {
        when(gerenciarStatusPedidoUseCase.buscarStatusPedidoPorId(pedido.getIdStatusPedido()))
                .thenReturn(statusPedido);
        when(pedidoProdutoRepositoryPort.buscarPorIdPedido(pedido.getId()))
                .thenReturn(produtos);

        assertDoesNotThrow(() -> pagamentoValidator.validar(pedido, pagamento));
    }
}
