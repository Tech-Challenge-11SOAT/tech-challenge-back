package br.com.postech.techchallange.infra.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class MercadoPagoOrderRequest {

    private String type;

    @JsonProperty("total_amount")
    private String totalAmount;

    @JsonProperty("external_reference")
    private String externalReference;

    @JsonProperty("processing_mode")
    private String processingMode;

    private Transactions transactions;

    private Payer payer;

    @Data
    @Builder
    public static class Transactions {
        private List<Payment> payments;
    }

    @Data
    @Builder
    public static class Payment {
        private String amount;

        @JsonProperty("payment_method")
        private PaymentMethod paymentMethod;

        @JsonProperty("expiration_time")
        private String expirationTime;
    }

    @Data
    @Builder
    public static class PaymentMethod {
        private String id;
        private String type;
    }

    @Data
    @Builder
    public static class Payer {
        private String email;
        // private String firstName;
    }
}
