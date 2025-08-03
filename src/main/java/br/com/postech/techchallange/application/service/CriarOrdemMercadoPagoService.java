package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.OrdemPagamento;
import br.com.postech.techchallange.domain.port.in.CriarOrdemMercadoPagoUseCase;
import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CriarOrdemMercadoPagoService implements CriarOrdemMercadoPagoUseCase {

    private final MercadoPagoPort mercadoPagoPort;

    @Override
    public OrdemPagamento criarOrdemPagamento(Long pedidoId, BigDecimal totalAmount, String payerEmail) {
        log.info("Iniciando criação de ordem de pagamento no Mercado Pago para pedido: {}", pedidoId);

        try {
            String externalReference = "pedido_" + pedidoId + "_" + System.currentTimeMillis();

            OrdemPagamento ordemPagamento = OrdemPagamento.builder()
                    .externalReference(externalReference)
                    .totalAmount(totalAmount)
                    .paymentMethodId("pix")
                    .paymentMethodType("bank_transfer")
                    .payerEmail(payerEmail)
                    .build();

            OrdemPagamento ordemCriada = mercadoPagoPort.criarOrdemPagamento(ordemPagamento);

            log.info("Ordem de pagamento criada com sucesso no Mercado Pago. ID: {}, External Reference: {}",
                    ordemCriada.getId(), ordemCriada.getExternalReference());

            return ordemCriada;

        } catch (Exception e) {
            log.error("Erro ao criar ordem de pagamento no Mercado Pago para pedido: {}", pedidoId, e);
            throw new RuntimeException("Falha ao processar pagamento via Mercado Pago", e);
        }
    }
}
