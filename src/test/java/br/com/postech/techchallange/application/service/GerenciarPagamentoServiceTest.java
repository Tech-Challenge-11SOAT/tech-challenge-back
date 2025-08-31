package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.PagamentoValidatorPort;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Gerenciador de Pagamentos")
class GerenciarPagamentoServiceTest {

    @Mock
    private PagamentoRepositoryPort repository;

    @Mock
    private GerenciarStatusPagamentoUseCase gerenciarStatusPagamento;

    @Mock
    private GerenciarPedidoUseCase gerenciarPedidoUseCase;

    @Mock
    private PagamentoValidatorPort pagamentoValidator;

    @Mock
    private NotificacaoPagamentoService notificacaoService;

    private GerenciarPagamentoService service;

    @BeforeEach
    void setUp() {
        service = new GerenciarPagamentoService(repository, gerenciarStatusPagamento,
                gerenciarPedidoUseCase, pagamentoValidator, notificacaoService);
    }

    @Nested
    @DisplayName("Testes de Pagamento")
    class PagamentoTests {
        @Test
        @DisplayName("Deve realizar pagamento com sucesso")
        void deveRealizarPagamentoComSucesso() {
            Long idPedido = 1L;
            Pagamento pagamento = criarPagamento(idPedido);
            Pedido pedido = criarPedido(idPedido);

            when(gerenciarPedidoUseCase.buscarPedido(idPedido)).thenReturn(pedido);
            when(gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.PENDENTE.getStatus()))
                    .thenReturn(criarStatusPagamento(1L, StatusPagamentoEnum.PENDENTE.getStatus()));
            when(repository.salvar(any(Pagamento.class))).thenReturn(pagamento);

            Pagamento resultado = service.pagar(pagamento);

            assertNotNull(resultado);
            assertEquals(idPedido, resultado.getIdPedido());
            verify(pagamentoValidator).validar(pedido, pagamento);
            verify(repository, times(2)).salvar(any(Pagamento.class));
        }

        @Test
        @DisplayName("Deve buscar pagamento por ID")
        void deveBuscarPagamentoPorId() {
            Long id = 1L;
            Pagamento pagamento = criarPagamento(1L);
            when(repository.buscarPorId(id)).thenReturn(Optional.of(pagamento));

            Pagamento resultado = service.buscarPagamento(id);

            assertNotNull(resultado);
            assertEquals(id, resultado.getIdPedido());
        }

        @Test
        @DisplayName("Deve retornar nulo quando pagamento não existe")
        void deveRetornarNuloQuandoPagamentoNaoExiste() {
            when(repository.buscarPorId(999L)).thenReturn(Optional.empty());

            Pagamento resultado = service.buscarPagamento(999L);

            assertNull(resultado);
        }
    }

    private Pagamento criarPagamento(Long idPedido) {
        Pagamento pagamento = new Pagamento();
        pagamento.setIdPedido(idPedido);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setValorTotal(new BigDecimal("100.00"));
        return pagamento;
    }

    private Pedido criarPedido(Long id) {
        return new Pedido(
                id,
                1L, // idCliente
                LocalDateTime.now(),
                1L, // idStatusPedido
                LocalDateTime.now(),
                1  // filaPedido
        );
    }

    private StatusPagamento criarStatusPagamento(Long id, String status) {
        return StatusPagamento.builder()
                .idStatusPagamento(id)
                .nomeStatus(StatusPagamentoEnum.of(status))
                .build();
    }
}
