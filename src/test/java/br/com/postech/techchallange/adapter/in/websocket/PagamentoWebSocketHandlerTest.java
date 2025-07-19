package br.com.postech.techchallange.adapter.in.websocket;

import br.com.postech.techchallange.application.service.NotificacaoPagamentoService;
import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Teste do PagamentoWebSocketHandler")
class PagamentoWebSocketHandlerTest {

    @Mock
    private GerenciarPagamentoUseCase pagamentoUseCase;

    @Mock
    private GerenciarStatusPagamentoUseCase statusPagamentoUseCase;

    @Mock
    private WebSocketSession session;

    @Mock
    private NotificacaoPagamentoService notificacaoPagamentoService;

    @InjectMocks
    private PagamentoWebSocketHandler handler;

    private Pagamento pagamento;
    private StatusPagamento statusPagamento;

    @BeforeEach
    void setUp() {
        pagamento = new Pagamento();
        pagamento.setIdPedido(1L);
        pagamento.setIdStatusPagamento(1L);

        statusPagamento = StatusPagamento.builder()
                .idStatusPagamento(1L)
                .nomeStatus(StatusPagamentoEnum.PENDENTE)
                .build();
    }

    @Test
    @DisplayName("Deve conectar cliente e enviar status atual do pagamento")
    void deveConectarClienteEEnviarStatusAtual() throws Exception {
        // Arrange
        URI uri = URI.create("ws://localhost:8080/ws/pagamento/1");
        when(session.getUri()).thenReturn(uri);
        when(session.isOpen()).thenReturn(true);
        when(pagamentoUseCase.buscarPagamentoPorPedido(1L)).thenReturn(pagamento);
        when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(statusPagamento);

        // Act
        handler.afterConnectionEstablished(session);

        // Assert
        verify(session).sendMessage(new TextMessage("PENDENTE"));
        verify(pagamentoUseCase).buscarPagamentoPorPedido(1L);
        verify(statusPagamentoUseCase).buscarStatusPagamento(1L);
    }

    @Test
    @DisplayName("Deve rejeitar conexão quando idPedido não for fornecido")
    void deveRejeitarConexaoSemIdPedido() throws Exception {
        // Arrange
        URI uri = URI.create("ws://localhost:8080/ws/pagamento/");
        when(session.getUri()).thenReturn(uri);

        // Act
        handler.afterConnectionEstablished(session);

        // Assert
        verify(session).close(CloseStatus.BAD_DATA);
        verify(pagamentoUseCase, never()).buscarPagamentoPorPedido(anyLong());
    }

    @Test
    @DisplayName("Deve notificar cliente quando status do pagamento for atualizado")
    void deveNotificarClienteQuandoStatusForAtualizado() throws Exception {
        // Arrange - Primeiro conecta o cliente
        URI uri = URI.create("ws://localhost:8080/ws/pagamento/1");
        when(session.getUri()).thenReturn(uri);
        when(session.isOpen()).thenReturn(true);
        when(pagamentoUseCase.buscarPagamentoPorPedido(1L)).thenReturn(pagamento);
        when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(statusPagamento);

        handler.afterConnectionEstablished(session);
        reset(session); // Limpa as interações anteriores

        // Arrange - Atualiza o status para FINALIZADO
        statusPagamento.setNomeStatus(StatusPagamentoEnum.FINALIZADO);
        when(session.isOpen()).thenReturn(true);
        when(pagamentoUseCase.buscarPagamentoPorPedido(1L)).thenReturn(pagamento);
        when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(statusPagamento);

        // Act
        this.notificacaoPagamentoService.notificarMudancaStatus(1L);

        // Assert
        verify(session).sendMessage(new TextMessage("FINALIZADO"));
    }

    @Test
    @DisplayName("Deve enviar mensagem de pagamento não encontrado quando pagamento não existir")
    void deveEnviarMensagemPagamentoNaoEncontrado() throws Exception {
        // Arrange
        URI uri = URI.create("ws://localhost:8080/ws/pagamento/999");
        when(session.getUri()).thenReturn(uri);
        when(session.isOpen()).thenReturn(true);
        when(pagamentoUseCase.buscarPagamentoPorPedido(999L)).thenReturn(null);

        // Act
        handler.afterConnectionEstablished(session);

        // Assert
        verify(session).sendMessage(new TextMessage("PAGAMENTO_NAO_ENCONTRADO"));
    }

    @Test
    @DisplayName("Deve remover sessão quando conexão for fechada")
    void deveRemoverSessaoQuandoConexaoForFechada() throws Exception {
        // Arrange
        URI uri = URI.create("ws://localhost:8080/ws/pagamento/1");
        when(session.getUri()).thenReturn(uri);
        when(session.isOpen()).thenReturn(true);
        when(pagamentoUseCase.buscarPagamentoPorPedido(1L)).thenReturn(pagamento);
        when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(statusPagamento);

        // Conecta primeiro
        handler.afterConnectionEstablished(session);

        // Act - Fecha a conexão
        handler.afterConnectionClosed(session, CloseStatus.NORMAL);

        // Arrange - Tenta notificar após fechamento
        when(session.isOpen()).thenReturn(false);

        // Act - Tenta notificar (não deve enviar mensagem)
        this.notificacaoPagamentoService.notificarMudancaStatus(1L);

        // Assert - Verifica que não tentou enviar mensagem após fechamento
        verify(session, times(1)).sendMessage(any(TextMessage.class)); // Só a primeira vez ao conectar
    }
}
