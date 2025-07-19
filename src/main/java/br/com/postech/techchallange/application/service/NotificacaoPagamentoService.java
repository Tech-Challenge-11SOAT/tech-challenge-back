package br.com.postech.techchallange.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class NotificacaoPagamentoService {

    private final List<NotificacaoListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Registra um listener para receber notificações de mudança de status
     */
    public void registrarListener(NotificacaoListener listener) {
        listeners.add(listener);
        log.debug("Listener registrado: {}", listener.getClass().getSimpleName());
    }

    /**
     * Remove um listener
     */
    public void removerListener(NotificacaoListener listener) {
        listeners.remove(listener);
        log.debug("Listener removido: {}", listener.getClass().getSimpleName());
    }

    /**
     * Notifica todos os listeners sobre mudança de status do pagamento
     */
    public void notificarMudancaStatus(Long idPedido) {
        log.info("Notificando mudança de status para o pedido: {}", idPedido);

        for (NotificacaoListener listener : listeners) {
            try {
                listener.onStatusPagamentoAlterado(idPedido);
            } catch (Exception e) {
                log.error("Erro ao notificar listener {}: {}",
                        listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    /**
     * Interface para listeners de notificação
     */
    public interface NotificacaoListener {
        void onStatusPagamentoAlterado(Long idPedido);
    }
}
