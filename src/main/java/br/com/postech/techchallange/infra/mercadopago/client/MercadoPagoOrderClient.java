package br.com.postech.techchallange.infra.mercadopago.client;

import br.com.postech.techchallange.domain.constants.MercadoPagoConstants;
import br.com.postech.techchallange.domain.model.OrdemPagamento;
import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;
import br.com.postech.techchallange.infra.config.MercadoPagoOptionsConfig;
import br.com.postech.techchallange.infra.mercadopago.dto.MercadoPagoOrderRequest;
import br.com.postech.techchallange.infra.mercadopago.dto.MercadoPagoOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoOrderClient implements MercadoPagoPort {

    @Qualifier("mercadoPagoRestTemplate")
    private final RestTemplate restTemplate;

    private final MercadoPagoOptionsConfig mercadoPagoOptionsConfig;

    @Override
    public OrdemPagamento criarOrdemPagamento(OrdemPagamento ordemPagamento) {
        log.info("Enviando requisição para Mercado Pago para external reference: {}", ordemPagamento.getExternalReference());

        try {
            String url = mercadoPagoOptionsConfig.getApi().getBaseUrl() + "/v1/orders";
            String idempotencyKey = UUID.randomUUID().toString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + mercadoPagoOptionsConfig.getApi().getAccessToken());
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
                    MercadoPagoOrderResponse.class);

            MercadoPagoOrderResponse responseBody = response.getBody();

            if (responseBody == null) {
                throw new RuntimeException("Resposta vazia do Mercado Pago");
            }

            log.info("Ordem criada no Mercado Pago com sucesso. ID: {}, Status: {}",
                    responseBody.getId(), responseBody.getStatus());

            return mapToOrdemPagamento(responseBody);

        } catch (Exception e) {
            log.error("Erro ao comunicar com Mercado Pago para external reference: {}", ordemPagamento.getExternalReference(), e);

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
                .expirationTime("PT30M")
                .build();

        MercadoPagoOrderRequest.Transactions transactions = MercadoPagoOrderRequest.Transactions.builder()
                .payments(List.of(payment))
                .build();

        MercadoPagoOrderRequest.Payer payer = MercadoPagoOrderRequest.Payer.builder()
                .email(ordemPagamento.getPayerEmail())
                .build();

        if (mercadoPagoOptionsConfig.getOptions().getTestMode().equals(Boolean.TRUE)) {
            payer.setFirstName(MercadoPagoConstants.MERCADO_PAGO_FIRST_NAME);
        }

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
        String paymentMethodId = null;
        String paymentMethodType = null;
        String paymentId = null;
        String paymentStatus = null;
        String paymentStatusDetail = null;
        BigDecimal paymentAmount = null;
        String expirationTime = null;
        OffsetDateTime dateOfExpiration = null;
        String referenceId = null;

        // Extrair dados do pagamento se disponível
        if (response.getTransactions() != null &&
                !response.getTransactions().getPayments().isEmpty()) {

            MercadoPagoOrderResponse.Payment payment = response.getTransactions().getPayments().get(0);

            // Dados do payment
            paymentId = payment.getId();
            paymentStatus = payment.getStatus();
            paymentStatusDetail = payment.getStatusDetail();
            paymentAmount = payment.getAmount();
            expirationTime = payment.getExpirationTime();
            dateOfExpiration = payment.getDateOfExpiration();
            referenceId = payment.getReferenceId();

            if (payment.getPaymentMethod() != null) {
                MercadoPagoOrderResponse.PaymentMethod paymentMethod = payment.getPaymentMethod();

                qrCode = paymentMethod.getQrCode();
                qrCodeBase64 = paymentMethod.getQrCodeBase64();
                ticketUrl = paymentMethod.getTicketUrl();
                paymentMethodId = paymentMethod.getId();
                paymentMethodType = paymentMethod.getType();
            }
        }

        return OrdemPagamento.builder()
                .id(response.getId())
                .type(response.getType())
                .status(response.getStatus())
                .statusDetail(response.getStatusDetail())
                .externalReference(response.getExternalReference())
                .totalAmount(response.getTotalAmount())
                .processingMode(response.getProcessingMode())
                .countryCode(response.getCountryCode())
                .userId(response.getUserId())
                .captureMode(response.getCaptureMode())
                .currency(response.getCurrency())
                .dateCreated(response.getCreatedDate())
                .dateLastUpdated(response.getLastUpdatedDate())
                .applicationId(response.getIntegrationData() != null ? response.getIntegrationData().getApplicationId() : null)
                .paymentId(paymentId)
                .paymentStatus(paymentStatus)
                .paymentStatusDetail(paymentStatusDetail)
                .paymentAmount(paymentAmount)
                .expirationTime(expirationTime)
                .dateOfExpiration(dateOfExpiration)
                .referenceId(referenceId)
                .paymentMethodId(paymentMethodId)
                .paymentMethodType(paymentMethodType)
                .qrCode(qrCode)
                .qrCodeBase64(qrCodeBase64)
                .ticketUrl(ticketUrl)
                .build();
    }
}
