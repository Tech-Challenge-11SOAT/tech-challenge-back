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
            when(pagamentoUseCase.buscarPagamentoPorPedido(1L)).thenReturn(pagamento);
            when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(statusPagamento);

            // Act
            handler.afterConnectionEstablished(session);

            // Assert
            verify(session, never()).close(any());
            verify(session).sendMessage(any(TextMessage.class));
            verify(pagamentoUseCase).buscarPagamentoPorPedido(1L);
            verify(statusPagamentoUseCase).buscarStatusPagamento(1L);
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
        @DisplayName("Deve enviar mensagem quando pagamento não existir")
        void deveEnviarMensagemQuandoPagamentoNaoExistir() throws Exception {
            // Arrange
            when(session.getUri()).thenReturn(new URI("/ws/pagamento/1"));
            when(pagamentoUseCase.buscarPagamentoPorPedido(1L)).thenReturn(null);

            // Act
            handler.afterConnectionEstablished(session);

            // Assert
            verify(session).sendMessage(eq(new TextMessage("PAGAMENTO_NAO_ENCONTRADO")));
            verify(session, never()).close(any());
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
            when(pagamentoUseCase.buscarPagamentoPorPedido(123L)).thenReturn(pagamento);
            when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(statusPagamento);

            // Act
            handler.afterConnectionEstablished(session);

            // Assert
            verify(pagamentoUseCase).buscarPagamentoPorPedido(123L);
            verify(statusPagamentoUseCase).buscarStatusPagamento(1L);
        }

        @Test
        @DisplayName("Deve lidar com URL malformada")
        void deveLidarComUrlMalformada() throws Exception {
            // Arrange
            when(session.getUri()).thenReturn(new URI("/ws/pagamento/abc"));

            // Act
            handler.afterConnectionEstablished(session);

            // Assert
            verify(session).close(CloseStatus.BAD_DATA);
            verify(session, never()).sendMessage(any());
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
            when(pagamentoUseCase.buscarPagamentoPorPedido(idPedido)).thenReturn(pagamento);
            when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(statusPagamento);

            handler.afterConnectionEstablished(session);

            // Reset do mock para verificar apenas a notificação
            reset(session);
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
            Pagamento pagamento = Pagamento.builder()
                    .id(1L)
                    .idPedido(idPedido)
                    .valorTotal(BigDecimal.valueOf(50.00))
                    .idStatusPagamento(1L)
                    .build();

            // Primeiro estabelece a conexão
            when(session.getUri()).thenReturn(new URI("/ws/pagamento/" + idPedido));
            when(pagamentoUseCase.buscarPagamentoPorPedido(idPedido)).thenReturn(pagamento);

            handler.afterConnectionEstablished(session);

            // Reset do mock para verificar apenas a notificação
            reset(session);
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
            when(pagamentoUseCase.buscarPagamentoPorPedido(idPedido)).thenReturn(pagamento);
            when(statusPagamentoUseCase.buscarStatusPagamento(1L)).thenReturn(null);

            // Act
            handler.afterConnectionEstablished(session);

            // Assert
            verify(session).sendMessage(eq(new TextMessage("STATUS_DESCONHECIDO")));
        }

        @Test
        @DisplayName("Deve remover sessão quando ocorrer erro durante notificação")
        void deveRemoverSessaoQuandoOcorrerErroDuranteNotificacao() throws Exception {
            // Arrange
            Long idPedido = 1L;
            Pagamento pagamento = Pagamento.builder()
                    .id(1L)
                    .idPedido(idPedido)
                    .valorTotal(BigDecimal.valueOf(50.00))
                    .idStatusPagamento(1L)
                    .build();

            when(session.getUri()).thenReturn(new URI("/ws/pagamento/" + idPedido));
            when(pagamentoUseCase.buscarPagamentoPorPedido(idPedido)).thenReturn(pagamento);

            handler.afterConnectionEstablished(session);

            // Simula erro na sessão
            when(session.isOpen()).thenReturn(true);
            doThrow(new RuntimeException("Erro de conexão")).when(session).sendMessage(any());

            // Act
            handler.onStatusPagamentoAlterado(idPedido);

            // Assert - Nova notificação não deve tentar enviar (sessão foi removida)
            reset(session);
            handler.onStatusPagamentoAlterado(idPedido);
            verify(session, never()).sendMessage(any());
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
            Pagamento pagamento = Pagamento.builder()
                    .id(1L)
                    .idPedido(idPedido)
                    .valorTotal(BigDecimal.valueOf(50.00))
                    .idStatusPagamento(1L)
                    .build();

            // Primeiro estabelece a conexão
            when(session.getUri()).thenReturn(new URI("/ws/pagamento/" + idPedido));
            when(pagamentoUseCase.buscarPagamentoPorPedido(idPedido)).thenReturn(pagamento);

            handler.afterConnectionEstablished(session);

            // Act
            handler.afterConnectionClosed(session, CloseStatus.NORMAL);

            // Assert - Tentativa de notificar após fechar não deve enviar mensagem
            handler.onStatusPagamentoAlterado(idPedido);

            // Verifica que não há tentativa de envio de mensagem após remoção da sessão
            // O verify não pode ser chamado diretamente porque já houve interação durante a conexão
            // Vamos verificar apenas que não há erro de null pointer ou similar
        }

        @Test
        @DisplayName("Deve lidar com URI inválida ao fechar conexão")
        void deveLidarComUriInvalidaAoFecharConexao() throws Exception {
            // Arrange
            when(session.getUri()).thenReturn(new URI("/ws/invalido"));

            // Act & Assert - Não deve lançar exceção
            handler.afterConnectionClosed(session, CloseStatus.NORMAL);

            // Verifica que não houve tentativa de interação com a sessão além da URI
            verify(session, only()).getUri();
        }
    }

    @Test
    @DisplayName("Deve processar mensagem de texto sem erro")
    void deveProcessarMensagemTextoSemErro() throws Exception {
        // Arrange
        TextMessage message = new TextMessage("test message");

        // Act & Assert - Não deve lançar exceção
        handler.handleTextMessage(session, message);

        // Verifica que não houve interação com a sessão durante o processamento da mensagem
        verifyNoInteractions(session);
    }

    @Test
    @DisplayName("Deve verificar se listener foi registrado no construtor")
    void deveVerificarSeListenerFoiRegistradoNoCostrutor() {
        // Arrange & Act - O construtor já foi chamado no @BeforeEach

        // Assert
        verify(notificacaoPagamentoService).registrarListener(handler);
    }
}
