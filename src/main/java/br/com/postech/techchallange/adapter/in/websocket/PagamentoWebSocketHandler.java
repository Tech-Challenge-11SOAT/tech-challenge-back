package br.com.postech.techchallange.adapter.in.websocket;

import br.com.postech.techchallange.application.service.NotificacaoPagamentoService;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class PagamentoWebSocketHandler extends TextWebSocketHandler
        implements NotificacaoPagamentoService.NotificacaoListener {

    private final GerenciarPagamentoUseCase pagamentoUseCase;
    private final GerenciarStatusPagamentoUseCase statusPagamentoUseCase;
    private final NotificacaoPagamentoService notificacaoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Mapa para armazenar sessões por idPedido
    private final Map<Long, WebSocketSession> sessoesPorPedido = new ConcurrentHashMap<>();
    private boolean listenerRegistrado = false;

    public PagamentoWebSocketHandler(GerenciarPagamentoUseCase pagamentoUseCase,
                                     GerenciarStatusPagamentoUseCase statusPagamentoUseCase,
                                     NotificacaoPagamentoService notificacaoService) {
        this.pagamentoUseCase = pagamentoUseCase;
        this.statusPagamentoUseCase = statusPagamentoUseCase;
        this.notificacaoService = notificacaoService;

        // Registra o listener no construtor
        this.notificacaoService.registrarListener(this);
        this.listenerRegistrado = true;
        log.info("WebSocketHandler registrado para receber notificações de pagamento");
    }

    @Override
    public void onStatusPagamentoAlterado(Long idPedido) {
        WebSocketSession session = sessoesPorPedido.get(idPedido);
        if (session != null && session.isOpen()) {
            try {
                enviarStatusAtual(session, idPedido);
                log.info("Status do pagamento enviado para o pedido: {}", idPedido);
            } catch (Exception e) {
                log.error("Erro ao enviar status do pagamento para o pedido {}: {}", idPedido, e.getMessage());
                // Remove sessão inválida
                sessoesPorPedido.remove(idPedido);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long idPedido = extrairIdPedidoDaUrl(session.getUri());

        if (idPedido != null) {
            sessoesPorPedido.put(idPedido, session);
            log.info("Cliente conectado para monitorar pedido: {}", idPedido);

            enviarStatusAtual(session, idPedido);
        } else {
            log.warn("Conexão rejeitada: idPedido não fornecido na URL");
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long idPedido = extrairIdPedidoDaUrl(session.getUri());
        if (idPedido != null) {
            sessoesPorPedido.remove(idPedido);
            log.info("Cliente desconectado do pedido: {}", idPedido);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.debug("Mensagem recebida: {}", message.getPayload());
    }

    private void enviarStatusAtual(WebSocketSession session, Long idPedido) throws Exception {
        Pagamento pagamento = pagamentoUseCase.buscarPagamentoPorPedido(idPedido);
        String resposta;

        if (pagamento == null) {
            resposta = "PAGAMENTO_NAO_ENCONTRADO";
        } else {
            var status = statusPagamentoUseCase.buscarStatusPagamento(pagamento.getIdStatusPagamento());
            resposta = status != null ? status.getNomeStatus().getStatus() : "STATUS_DESCONHECIDO";
        }

        session.sendMessage(new TextMessage(resposta));
    }

    private Long extrairIdPedidoDaUrl(URI uri) {
        try {
            String path = uri.getPath();
            String[] segments = path.split("/");
            // Espera URL no formato: /ws/pagamento/{idPedido}
            if (segments.length >= 3) {
                return Long.parseLong(segments[segments.length - 1]);
            }
        } catch (NumberFormatException e) {
            log.error("Erro ao extrair idPedido da URL: {}", uri.getPath());
        }
        return null;
    }
}
