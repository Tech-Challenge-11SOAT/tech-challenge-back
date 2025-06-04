package br.com.postech.techchallange.domain.port.in;

import java.util.List;

import br.com.postech.techchallange.domain.model.Pagamento;

public interface GerenciarPagamentoUseCase {

    Pagamento pagar(Pagamento pagamento);

    Pagamento buscarPagamento(Long id);

    Pagamento buscarPagamentoPorPedido(Long idPedido);

    void processarWebhookPagamento(br.com.postech.techchallange.adapter.in.rest.request.WebhookPagamentoRequest request);

    List<Pagamento> listarPagamentos();
}
