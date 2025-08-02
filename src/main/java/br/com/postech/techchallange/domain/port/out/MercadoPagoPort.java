package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.OrdemPagamento;

public interface MercadoPagoPort {

    OrdemPagamento criarOrdemPagamento(OrdemPagamento ordemPagamento);
}
