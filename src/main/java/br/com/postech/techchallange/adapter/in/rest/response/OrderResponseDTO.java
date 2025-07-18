package br.com.postech.techchallange.adapter.in.rest.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponseDTO {

    private String orderId;
    private String externalReference;
    private BigDecimal totalAmount;
    private String status;
    private String qrCode;
    private String qrCodeBase64;
    private String ticketUrl;
    private LocalDateTime dateCreated;
    private LocalDateTime dateLastUpdated;
}
