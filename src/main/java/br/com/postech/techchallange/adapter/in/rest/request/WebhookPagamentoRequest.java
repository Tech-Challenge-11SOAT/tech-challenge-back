package br.com.postech.techchallange.adapter.in.rest.request;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WebhookPagamentoRequest {

    private String action;

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("application_id")
    private String applicationId;

    @NotNull
    private OrderData data;

    @JsonProperty("date_created")
    private Instant dateCreated;

    @JsonProperty("live_mode")
    private Boolean liveMode;

    private String type;

    @JsonProperty("user_id")
    private String userId;

    @Data
    public static class OrderData {

        @JsonProperty("external_reference")
        @NotNull
        private String externalReference;

        private String id;
        private String status;

        @JsonProperty("status_detail")
        private String statusDetail;

        @JsonProperty("total_amount")
        private String totalAmount;

        @JsonProperty("total_paid_amount")
        private String totalPaidAmount;

        private Transactions transactions;
        private String type;
        private Integer version;
    }

    @Data
    public static class Transactions {

        private List<Payment> payments;
    }

    @Data
    public static class Payment {

        private String amount;
        private String id;

        @JsonProperty("paid_amount")
        private String paidAmount;

        @JsonProperty("payment_method")
        private PaymentMethod paymentMethod;

        private Reference reference;
        private String status;

        @JsonProperty("status_detail")
        private String statusDetail;
    }

    @Data
    public static class PaymentMethod {

        private String id;
        private Integer installments;
        private String type;
    }

    @Data
    public static class Reference {

        private String id;
    }
}
