package br.com.postech.techchallange.adapter.in.rest.request;

import lombok.Data;

@Data
public class WebhookPagamentoRequest {

    private Long idPedido;
    private String statusPagamento; // "APROVADO", "RECUSADO", etc.
}
