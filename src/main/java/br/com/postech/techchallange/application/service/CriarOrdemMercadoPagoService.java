package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.constants.MercadoPagoConstants;
import br.com.postech.techchallange.domain.model.OrdemPagamento;
import br.com.postech.techchallange.domain.port.in.CriarOrdemMercadoPagoUseCase;
import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CriarOrdemMercadoPagoService implements CriarOrdemMercadoPagoUseCase {

    private final MercadoPagoPort mercadoPagoPort;

    @Override
    public OrdemPagamento criarOrdemPagamento(Long pedidoId, BigDecimal totalAmount, String payerEmail) {
        log.info("Iniciando criação de ordem de pagamento no Mercado Pago para pedido: {}", pedidoId);

        try {
            String externalReference = this.getExternalReference(pedidoId);

            // Define email padrão se não fornecido
            String emailPagador = payerEmail != null ? payerEmail : MercadoPagoConstants.MERCADO_PAGO_TEST_EMAIL;

            OrdemPagamento ordemPagamento = OrdemPagamento.builder()
                    .externalReference(externalReference)
                    .totalAmount(totalAmount)
                    .paymentMethodId(MercadoPagoConstants.MERCADO_PAGO_PAYMENT_METHOD_ID)
                    .paymentMethodType(MercadoPagoConstants.MERCADO_PAGO_PAYMENT_METHOD_TYPE)
                    .payerEmail(emailPagador)
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

    private String getExternalReference(Long pedidoId) {
        return "pedido_" + pedidoId + "_" + System.currentTimeMillis();
    }
}
