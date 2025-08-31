package br.com.postech.techchallange.adapter.in.websocket;

import br.com.postech.techchallange.application.service.NotificacaoPagamentoService;
import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do PagamentoWebSocketHandler")
class PagamentoWebSocketHandlerTest {

    @Mock
    private GerenciarPagamentoUseCase pagamentoUseCase;

    @Mock
    private GerenciarStatusPagamentoUseCase statusPagamentoUseCase;

    @Mock
    private WebSocketSession session;

    @Mock
    private NotificacaoPagamentoService notificacaoPagamentoService;

    private PagamentoWebSocketHandler handler;

    @BeforeEach
    void setUp() {
        handler = new PagamentoWebSocketHandler(pagamentoUseCase, statusPagamentoUseCase, notificacaoPagamentoService);
    }

    @Nested
    @DisplayName("Testes de Conexão WebSocket")
    class ConexaoWebSocket {
        @Test
        @DisplayName("Deve estabelecer conexão com sucesso")
        void deveEstabelecerConexaoComSucesso() throws Exception {
            // Arrange
            Pagamento pagamento = Pagamento.builder()
                    .id(1L)
                    .idPedido(1L)
                    .valorTotal(BigDecimal.valueOf(50.00))
                    .idStatusPagamento(1L)
                    .build();

            StatusPagamento statusPagamento = StatusPagamento.builder()
                    .idStatusPagamento(1L)
                    .nomeStatus(StatusPagamentoEnum.PENDENTE)
                    .build();

            when(session.getUri()).thenReturn(new URI("/ws/pagamento/1"));
            when(session.isOpen()).thenReturn(true);
            when(pagamentoUseCase.buscarPagamentoPorPedido(1L)).thenReturn(pagamento);
            when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(statusPagamento);

            // Act
            handler.afterConnectionEstablished(session);

            // Assert
            verify(session, never()).close(any());
            verify(session).sendMessage(any(TextMessage.class));
        }

        @Test
        @DisplayName("Deve fechar conexão quando URI for inválida")
        void deveFecharConexaoQuandoUriInvalida() throws Exception {
            // Arrange
            when(session.getUri()).thenReturn(new URI("/ws/invalido"));

            // Act
            handler.afterConnectionEstablished(session);

            // Assert
            verify(session).close(CloseStatus.BAD_DATA);
            verify(session, never()).sendMessage(any());
        }

        @Test
        @DisplayName("Deve fechar conexão quando pagamento não existir")
        void deveFecharConexaoQuandoPagamentoNaoExistir() throws Exception {
            // Arrange
            when(session.getUri()).thenReturn(new URI("/ws/pagamento/1"));
            when(pagamentoUseCase.buscarPagamentoPorPedido(1L)).thenReturn(null);

            // Act
            handler.afterConnectionEstablished(session);

            // Assert
            verify(session).sendMessage(eq(new TextMessage("PAGAMENTO_NAO_ENCONTRADO")));
        }

        @Test
        @DisplayName("Deve extrair ID do pedido da URL corretamente")
        void deveExtrairIdPedidoDaUrlCorretamente() throws Exception {
            // Arrange
            Pagamento pagamento = Pagamento.builder()
                    .id(1L)
                    .idPedido(123L)
                    .valorTotal(BigDecimal.valueOf(100.00))
                    .idStatusPagamento(1L)
                    .build();

            StatusPagamento statusPagamento = StatusPagamento.builder()
                    .idStatusPagamento(1L)
                    .nomeStatus(StatusPagamentoEnum.FINALIZADO)
                    .build();

            when(session.getUri()).thenReturn(new URI("/ws/pagamento/123"));
            when(session.isOpen()).thenReturn(true);
            when(pagamentoUseCase.buscarPagamentoPorPedido(123L)).thenReturn(pagamento);
            when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(statusPagamento);

            // Act
            handler.afterConnectionEstablished(session);

            // Assert
            verify(pagamentoUseCase).buscarPagamentoPorPedido(123L);
        }
    }

    @Nested
    @DisplayName("Testes de Notificações")
    class NotificacoesTests {
        @Test
        @DisplayName("Deve notificar alteração de status com sucesso")
        void deveNotificarAlteracaoStatusComSucesso() throws Exception {
            // Arrange
            Long idPedido = 1L;
            Pagamento pagamento = Pagamento.builder()
                    .id(1L)
                    .idPedido(idPedido)
                    .valorTotal(BigDecimal.valueOf(50.00))
                    .idStatusPagamento(1L)
                    .build();

            StatusPagamento statusPagamento = StatusPagamento.builder()
                    .idStatusPagamento(1L)
                    .nomeStatus(StatusPagamentoEnum.FINALIZADO)
                    .build();

            // Primeiro estabelece a conexão
            when(session.getUri()).thenReturn(new URI("/ws/pagamento/" + idPedido));
            when(session.isOpen()).thenReturn(true);
            when(pagamentoUseCase.buscarPagamentoPorPedido(idPedido)).thenReturn(pagamento);
            when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(statusPagamento);

            handler.afterConnectionEstablished(session);
            reset(session); // Reset para verificar apenas a notificação

            when(session.isOpen()).thenReturn(true);

            // Act
            handler.onStatusPagamentoAlterado(idPedido);

            // Assert
            verify(session).sendMessage(any(TextMessage.class));
        }

        @Test
        @DisplayName("Não deve notificar quando sessão estiver fechada")
        void naoDeveNotificarQuandoSessaoFechada() throws Exception {
            // Arrange
            Long idPedido = 1L;

            // Primeiro estabelece a conexão
            when(session.getUri()).thenReturn(new URI("/ws/pagamento/" + idPedido));
            when(session.isOpen()).thenReturn(true);

            Pagamento pagamento = Pagamento.builder()
                    .id(1L)
                    .idPedido(idPedido)
                    .valorTotal(BigDecimal.valueOf(50.00))
                    .idStatusPagamento(1L)
                    .build();

            when(pagamentoUseCase.buscarPagamentoPorPedido(idPedido)).thenReturn(pagamento);

            handler.afterConnectionEstablished(session);
            reset(session); // Reset para verificar apenas a notificação

            when(session.isOpen()).thenReturn(false);

            // Act
            handler.onStatusPagamentoAlterado(idPedido);

            // Assert
            verify(session, never()).sendMessage(any());
        }

        @Test
        @DisplayName("Deve enviar status desconhecido quando status não for encontrado")
        void deveEnviarStatusDesconhecidoQuandoStatusNaoEncontrado() throws Exception {
            // Arrange
            Long idPedido = 1L;
            Pagamento pagamento = Pagamento.builder()
                    .id(1L)
                    .idPedido(idPedido)
                    .valorTotal(BigDecimal.valueOf(50.00))
                    .idStatusPagamento(1L)
                    .build();

            when(session.getUri()).thenReturn(new URI("/ws/pagamento/" + idPedido));
            when(session.isOpen()).thenReturn(true);
            when(pagamentoUseCase.buscarPagamentoPorPedido(idPedido)).thenReturn(pagamento);
            when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(null);

            // Act
            handler.afterConnectionEstablished(session);

            // Assert
            verify(session).sendMessage(eq(new TextMessage("STATUS_DESCONHECIDO")));
        }
    }

    @Nested
    @DisplayName("Testes de Fechamento de Conexão")
    class FechamentoConexao {
        @Test
        @DisplayName("Deve remover sessão ao fechar conexão")
        void deveRemoverSessaoAoFecharConexao() throws Exception {
            // Arrange
            Long idPedido = 1L;

            // Primeiro estabelece a conexão
            when(session.getUri()).thenReturn(new URI("/ws/pagamento/" + idPedido));
            when(session.isOpen()).thenReturn(true);

            Pagamento pagamento = Pagamento.builder()
                    .id(1L)
                    .idPedido(idPedido)
                    .valorTotal(BigDecimal.valueOf(50.00))
                    .idStatusPagamento(1L)
                    .build();

            when(pagamentoUseCase.buscarPagamentoPorPedido(idPedido)).thenReturn(pagamento);

            handler.afterConnectionEstablished(session);

            // Act
            handler.afterConnectionClosed(session, CloseStatus.NORMAL);

            // Assert - Tentativa de notificar após fechar não deve enviar mensagem
            when(session.isOpen()).thenReturn(false);
            handler.onStatusPagamentoAlterado(idPedido);
            verify(session, never()).sendMessage(any());
        }

        @Test
        @DisplayName("Deve lidar com URI inválida ao fechar conexão")
        void deveLidarComUriInvalidaAoFecharConexao() throws Exception {
            // Arrange
            when(session.getUri()).thenReturn(new URI("/ws/invalido"));

            // Act & Assert - Não deve lançar exceção
            handler.afterConnectionClosed(session, CloseStatus.NORMAL);
        }
    }

    @Test
    @DisplayName("Deve lidar com erro de transporte")
    void deveLidarComErroDeTransporte() throws Exception {
        // Arrange
        Exception erro = new IOException("Erro de conexão");
        when(session.getUri()).thenReturn(new URI("/ws/pagamento/1"));

        // Act
        handler.handleTransportError(session, erro);

        // Assert
        verify(session).close(CloseStatus.SERVER_ERROR);
    }

    @Test
    @DisplayName("Deve processar mensagem de texto sem erro")
    void deveProcessarMensagemTextoSemErro() throws Exception {
        // Arrange
        TextMessage message = new TextMessage("test message");

        // Act & Assert - Não deve lançar exceção
        handler.handleTextMessage(session, message);
    }
}
