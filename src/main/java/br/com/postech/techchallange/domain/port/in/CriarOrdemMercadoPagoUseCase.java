package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.domain.model.OrdemPagamento;

import java.math.BigDecimal;

public interface CriarOrdemMercadoPagoUseCase {

    OrdemPagamento criarOrdemPagamento(Long pedidoId, BigDecimal totalAmount, String payerEmail);
}
