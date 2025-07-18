package br.com.postech.techchallange.adapter.in.websocket;

import br.com.postech.techchallange.domain.model.Pagamento;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.postech.techchallange.adapter.in.rest.request.WebhookPagamentoRequest;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PagamentoWebSocketHandler extends TextWebSocketHandler {
    private final GerenciarPagamentoUseCase pagamentoUseCase;
    private final GerenciarStatusPagamentoUseCase statusPagamentoUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Espera receber um JSON com o campo idPedido
        StatusPagamentoRequest request = objectMapper.readValue(message.getPayload(), StatusPagamentoRequest.class);
        Pagamento pagamento = pagamentoUseCase.buscarPagamentoPorPedido(request.getIdPedido());
        String resposta;
        if (pagamento == null) {
            resposta = "NÃO ENCONTRADO";
        } else {
            var status = statusPagamentoUseCase.buscarStatusPagamento(pagamento.getIdStatusPagamento());
            resposta = status != null ? status.getNomeStatus().getStatus() : pagamento.getIdStatusPagamento() + "";
        }
        session.sendMessage(new TextMessage(resposta));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Opcional: lógica ao conectar
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Opcional: lógica ao desconectar
    }
}
