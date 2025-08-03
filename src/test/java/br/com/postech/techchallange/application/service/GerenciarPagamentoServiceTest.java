package br.com.postech.techchallange.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postech.techchallange.adapter.in.rest.request.WebhookPagamentoRequest;
import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.PagamentoValidatorPort;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GerenciarPagamentoServiceTest {

    @Mock
    private PagamentoRepositoryPort repository;

    @Mock
    private GerenciarStatusPagamentoUseCase gerenciarStatusPagamento;

    @Mock
    private GerenciarPedidoUseCase gerenciarPedidoUseCase;

    @Mock
    private PagamentoValidatorPort pagamentoValidator;

    @InjectMocks
    private GerenciarPagamentoService gerenciarPagamentoService;

    @Captor
    private ArgumentCaptor<Pagamento> pagamentoCaptor;

    @SuppressWarnings("unused")
	@Test
    @DisplayName("Deve criar pagamento com sucesso")
    void deveCriarPagamentoComSucesso() {
        // Arrange
        Long idPedido = 1L;
        Long idStatusPagamento = 1L;
        BigDecimal valor = BigDecimal.valueOf(100);
        String metodoPagamento = "PIX";

        Pagamento pagamento = Pagamento.builder()
                .idPedido(idPedido)
                .valorTotal(valor)
                .metodoPagamento(metodoPagamento)
                .build();

        Pedido pedido = new Pedido(idPedido, 1L, LocalDateTime.now(), 1L, LocalDateTime.now(), 1);

        StatusPagamento statusPagamentoPendente = StatusPagamento.builder()
                .idStatusPagamento(idStatusPagamento)
                .nomeStatus(StatusPagamentoEnum.PENDENTE)
                .build();

        when(gerenciarPedidoUseCase.buscarPedido(idPedido)).thenReturn(pedido);
        when(gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.PENDENTE.getStatus()))
                .thenReturn(statusPagamentoPendente);
        when(repository.salvar(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        Pagamento resultado = gerenciarPagamentoService.pagar(pagamento);

        // Assert
        verify(pagamentoValidator).validar(pedido, pagamento);
        verify(repository, times(2)).salvar(pagamentoCaptor.capture());

        Pagamento pagamentoSalvo = pagamentoCaptor.getValue();
        assertNotNull(pagamentoSalvo.getDataPagamento());
        assertEquals(idStatusPagamento, pagamentoSalvo.getIdStatusPagamento());
        assertEquals(idPedido, pagamentoSalvo.getIdPedido());
        assertEquals(valor, pagamentoSalvo.getValorTotal());
        assertEquals(metodoPagamento, pagamentoSalvo.getMetodoPagamento());
    }

    @Test
    @DisplayName("Deve buscar pagamento por ID com sucesso")
    void deveBuscarPagamentoPorIdComSucesso() {
        // Arrange
        Long id = 1L;
        Pagamento pagamento = Pagamento.builder().id(id).build();
        when(repository.buscarPorId(id)).thenReturn(Optional.of(pagamento));

        // Act
        Pagamento resultado = gerenciarPagamentoService.buscarPagamento(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    @DisplayName("Deve retornar null quando pagamento não encontrado por ID")
    void deveRetornarNullQuandoPagamentoNaoEncontradoPorId() {
        // Arrange
        Long id = 999L;
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // Act
        Pagamento resultado = gerenciarPagamentoService.buscarPagamento(id);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve listar todos os pagamentos")
    void deveListarTodosPagamentos() {
        // Arrange
        List<Pagamento> pagamentos = Arrays.asList(
                Pagamento.builder().id(1L).build(),
                Pagamento.builder().id(2L).build()
        );
        when(repository.listarTodos()).thenReturn(pagamentos);

        // Act
        List<Pagamento> resultado = gerenciarPagamentoService.listarPagamentos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Deve buscar pagamento por pedido com sucesso")
    void deveBuscarPagamentoPorPedidoComSucesso() {
        // Arrange
        Long idPedido = 1L;
        Pagamento pagamento = Pagamento.builder().idPedido(idPedido).build();
        when(repository.buscarPorIdPedido(idPedido)).thenReturn(Optional.of(pagamento));

        // Act
        Pagamento resultado = gerenciarPagamentoService.buscarPagamentoPorPedido(idPedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(idPedido, resultado.getIdPedido());
    }

    @Test
    @DisplayName("Deve retornar null quando pagamento não encontrado por pedido")
    void deveRetornarNullQuandoPagamentoNaoEncontradoPorPedido() {
        // Arrange
        Long idPedido = 999L;
        when(repository.buscarPorIdPedido(idPedido)).thenReturn(Optional.empty());

        // Act
        Pagamento resultado = gerenciarPagamentoService.buscarPagamentoPorPedido(idPedido);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve processar webhook com pagamento aprovado")
    void deveProcessarWebhookComPagamentoAprovado() {
        // Arrange
        Long idPedido = 1L;
        Long idStatusFinalizado = 2L;

        WebhookPagamentoRequest request = new WebhookPagamentoRequest();

        Pagamento pagamentoExistente = Pagamento.builder()
                .id(1L)
                .idPedido(idPedido)
                .build();

        StatusPagamento statusPagamentoFinalizado = StatusPagamento.builder()
                .idStatusPagamento(idStatusFinalizado)
                .nomeStatus(StatusPagamentoEnum.FINALIZADO)
                .build();

        when(repository.buscarPorIdPedido(idPedido)).thenReturn(Optional.of(pagamentoExistente));
        when(gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.FINALIZADO.getStatus()))
                .thenReturn(statusPagamentoFinalizado);
        when(repository.salvar(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        gerenciarPagamentoService.processarWebhookPagamento(request);

        // Assert
        verify(repository).salvar(pagamentoCaptor.capture());

        Pagamento pagamentoSalvo = pagamentoCaptor.getValue();
        assertEquals(idStatusFinalizado, pagamentoSalvo.getIdStatusPagamento());
        assertNotNull(pagamentoSalvo.getDataPagamento());
    }

    @Test
    @DisplayName("Deve processar webhook com pagamento recusado")
    void deveProcessarWebhookComPagamentoRecusado() {
        // Arrange
        Long idPedido = 1L;
        Long idStatusErro = 3L;

        WebhookPagamentoRequest request = new WebhookPagamentoRequest();

        Pagamento pagamentoExistente = Pagamento.builder()
                .id(1L)
                .idPedido(idPedido)
                .build();

        StatusPagamento statusPagamentoErro = StatusPagamento.builder()
                .idStatusPagamento(idStatusErro)
                .nomeStatus(StatusPagamentoEnum.ERRO)
                .build();

        when(repository.buscarPorIdPedido(idPedido)).thenReturn(Optional.of(pagamentoExistente));
        when(gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(StatusPagamentoEnum.ERRO.getStatus()))
                .thenReturn(statusPagamentoErro);
        when(repository.salvar(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        gerenciarPagamentoService.processarWebhookPagamento(request);

        // Assert
        verify(repository).salvar(pagamentoCaptor.capture());

        Pagamento pagamentoSalvo = pagamentoCaptor.getValue();
        assertEquals(idStatusErro, pagamentoSalvo.getIdStatusPagamento());
        assertNotNull(pagamentoSalvo.getDataPagamento());
    }

    @Test
    @DisplayName("Deve lançar exceção quando pagamento não encontrado no webhook")
    void deveLancarExcecaoQuandoPagamentoNaoEncontradoNoWebhook() {
        // Arrange
        Long idPedido = 999L;

        WebhookPagamentoRequest request = new WebhookPagamentoRequest();

        when(repository.buscarPorIdPedido(idPedido)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> gerenciarPagamentoService.processarWebhookPagamento(request));

        assertEquals("Pagamento não encontrado para o pedido informado", exception.getMessage());
        verify(repository, never()).salvar(any());
    }
}
