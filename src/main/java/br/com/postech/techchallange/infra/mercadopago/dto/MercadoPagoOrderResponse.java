package br.com.postech.techchallange.infra.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class MercadoPagoOrderResponse {

    private String id;
    private String type;
    private String status;

    @JsonProperty("status_detail")
    private String statusDetail;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("external_reference")
    private String externalReference;

    @JsonProperty("processing_mode")
    private String processingMode;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("capture_mode")
    private String captureMode;

    private String currency;

    @JsonProperty("created_date")
    private OffsetDateTime createdDate;

    @JsonProperty("last_updated_date")
    private OffsetDateTime lastUpdatedDate;

    @JsonProperty("integration_data")
    private IntegrationData integrationData;

    private Transactions transactions;

    @Data
    public static class IntegrationData {
        @JsonProperty("application_id")
        private String applicationId;
    }

    @Data
    public static class Transactions {
        private List<Payment> payments;
    }

    @Data
    public static class Payment {
        private String id;
        private String status;

        @JsonProperty("status_detail")
        private String statusDetail;

        private BigDecimal amount;

        @JsonProperty("expiration_time")
        private String expirationTime;

        @JsonProperty("date_of_expiration")
        private OffsetDateTime dateOfExpiration;

        @JsonProperty("reference_id")
        private String referenceId;

        @JsonProperty("payment_method")
        private PaymentMethod paymentMethod;
    }

    @Data
    public static class PaymentMethod {
        private String id;
        private String type;

        @JsonProperty("ticket_url")
        private String ticketUrl;

        @JsonProperty("qr_code")
        private String qrCode;

        @JsonProperty("qr_code_base64")
        private String qrCodeBase64;
    }
}
