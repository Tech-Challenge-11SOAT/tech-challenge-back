package br.com.postech.techchallange.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrdemPagamento {

    private String id;
    private String externalReference;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethodId;
    private String paymentMethodType;
    private String payerEmail;
    private String qrCode;
    private String qrCodeBase64;
    private String ticketUrl;
    private LocalDateTime dateCreated;
    private LocalDateTime dateLastUpdated;
}
