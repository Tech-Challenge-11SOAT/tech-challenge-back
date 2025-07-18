package br.com.postech.techchallange.infra.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MercadoPagoOrderResponse {

    private String id;
    private String type;
    private String status;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("external_reference")
    private String externalReference;

    @JsonProperty("processing_mode")
    private String processingMode;

    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    @JsonProperty("date_last_updated")
    private LocalDateTime dateLastUpdated;

    private Transactions transactions;
    private Payer payer;

    @Data
    public static class Transactions {
        private List<Payment> payments;
    }

    @Data
    public static class Payment {
        private String id;
        private String status;
        private BigDecimal amount;

        @JsonProperty("payment_method")
        private PaymentMethod paymentMethod;

        @JsonProperty("expiration_time")
        private String expirationTime;

        @JsonProperty("point_of_interaction")
        private PointOfInteraction pointOfInteraction;
    }

    @Data
    public static class PaymentMethod {
        private String id;
        private String type;
    }

    @Data
    public static class PointOfInteraction {

        @JsonProperty("transaction_data")
        private TransactionData transactionData;
    }

    @Data
    public static class TransactionData {

        @JsonProperty("qr_code")
        private String qrCode;

        @JsonProperty("qr_code_base64")
        private String qrCodeBase64;

        @JsonProperty("ticket_url")
        private String ticketUrl;
    }

    @Data
    public static class Payer {
        private String email;
    }
}
