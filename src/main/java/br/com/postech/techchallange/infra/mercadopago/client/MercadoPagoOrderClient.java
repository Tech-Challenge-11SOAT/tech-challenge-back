package br.com.postech.techchallange.infra.mercadopago.client;

import br.com.postech.techchallange.domain.model.OrdemPagamento;
import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;
import br.com.postech.techchallange.infra.mercadopago.dto.MercadoPagoOrderRequest;
import br.com.postech.techchallange.infra.mercadopago.dto.MercadoPagoOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoOrderClient implements MercadoPagoPort {

    @Qualifier("mercadoPagoRestTemplate")
    private final RestTemplate restTemplate;

    @Value("${mercadopago.api.base-url}")
    private String baseUrl;

    @Value("${mercadopago.api.access-token}")
    private String accessToken;

    @Override
    public OrdemPagamento criarOrdemPagamento(OrdemPagamento ordemPagamento) {
        log.info("Enviando requisição para Mercado Pago para external reference: {}", ordemPagamento.getExternalReference());

        try {
            String url = baseUrl + "/v1/orders";
            String idempotencyKey = UUID.randomUUID().toString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Content-Type", "application/json");
            headers.set("Accept", "application/json");
            headers.set("X-Idempotency-Key", idempotencyKey);

            MercadoPagoOrderRequest request = buildMercadoPagoRequest(ordemPagamento);
            HttpEntity<MercadoPagoOrderRequest> entity = new HttpEntity<>(request, headers);

            log.debug("Enviando requisição para URL: {} com headers: {}", url, headers);
            log.info("Requisição Mercado Pago: {}", request);

            ResponseEntity<MercadoPagoOrderResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    MercadoPagoOrderResponse.class
            );

            MercadoPagoOrderResponse responseBody = response.getBody();

            if (responseBody == null) {
                throw new RuntimeException("Resposta vazia do Mercado Pago");
            }

            log.info("Ordem criada no Mercado Pago com sucesso. ID: {}, Status: {}",
                    responseBody.getId(), responseBody.getStatus());

            return mapToOrdemPagamento(responseBody);

        } catch (Exception e) {
            log.error("Erro ao comunicar com Mercado Pago para external reference: {}",
                    ordemPagamento.getExternalReference(), e);
            throw new RuntimeException("Falha na comunicação com Mercado Pago: " + e.getMessage(), e);
        }
    }

    private MercadoPagoOrderRequest buildMercadoPagoRequest(OrdemPagamento ordemPagamento) {
        MercadoPagoOrderRequest.PaymentMethod paymentMethod = MercadoPagoOrderRequest.PaymentMethod.builder()
                .id(ordemPagamento.getPaymentMethodId())
                .type(ordemPagamento.getPaymentMethodType())
                .build();

        MercadoPagoOrderRequest.Payment payment = MercadoPagoOrderRequest.Payment.builder()
                .amount(ordemPagamento.getTotalAmount().toString())
                .paymentMethod(paymentMethod)
                .expirationTime("P3Y6M4DT12H30M5S") // 3 anos, 6 meses, 4 dias, 12 horas, 30 minutos, 5 segundos
                .build();

        MercadoPagoOrderRequest.Transactions transactions = MercadoPagoOrderRequest.Transactions.builder()
                .payments(List.of(payment))
                .build();

        MercadoPagoOrderRequest.Payer payer = MercadoPagoOrderRequest.Payer.builder()
                .email(ordemPagamento.getPayerEmail())
                //.firstName("APRO")
                .build();

        return MercadoPagoOrderRequest.builder()
                .type("online")
                .totalAmount(ordemPagamento.getTotalAmount().toString())
                .externalReference(ordemPagamento.getExternalReference())
                .processingMode("automatic")
                .transactions(transactions)
                .payer(payer)
                .build();
    }

    private OrdemPagamento mapToOrdemPagamento(MercadoPagoOrderResponse response) {
        String qrCode = null;
        String qrCodeBase64 = null;
        String ticketUrl = null;

        // Extrair dados do QR Code se disponível
        if (response.getTransactions() != null &&
            !response.getTransactions().getPayments().isEmpty()) {

            MercadoPagoOrderResponse.Payment payment = response.getTransactions().getPayments().get(0);

            if (payment.getPointOfInteraction() != null &&
                payment.getPointOfInteraction().getTransactionData() != null) {

                MercadoPagoOrderResponse.TransactionData transactionData =
                    payment.getPointOfInteraction().getTransactionData();

                qrCode = transactionData.getQrCode();
                qrCodeBase64 = transactionData.getQrCodeBase64();
                ticketUrl = transactionData.getTicketUrl();
            }
        }

        return OrdemPagamento.builder()
                .id(response.getId())
                .externalReference(response.getExternalReference())
                .totalAmount(response.getTotalAmount())
                .status(response.getStatus())
                .payerEmail(response.getPayer() != null ? response.getPayer().getEmail() : null)
                .qrCode(qrCode)
                .qrCodeBase64(qrCodeBase64)
                .ticketUrl(ticketUrl)
                .dateCreated(response.getDateCreated())
                .dateLastUpdated(response.getDateLastUpdated())
                .build();
    }
}
