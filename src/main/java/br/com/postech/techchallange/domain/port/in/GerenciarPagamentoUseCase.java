package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.adapter.in.rest.request.WebhookPagamentoRequest;
import br.com.postech.techchallange.domain.model.Pagamento;

import java.util.List;

public interface GerenciarPagamentoUseCase {

    Pagamento pagar(Pagamento pagamento);

    Pagamento buscarPagamento(Long id);

    Pagamento buscarPagamentoPorPedido(Long idPedido);

    void processarWebhookPagamento(WebhookPagamentoRequest request);

    List<Pagamento> listarPagamentos();
}
