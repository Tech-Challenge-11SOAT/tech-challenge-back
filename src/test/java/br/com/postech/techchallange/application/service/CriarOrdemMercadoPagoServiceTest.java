package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.OrdemPagamento;
import br.com.postech.techchallange.domain.port.out.MercadoPagoPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarOrdemMercadoPagoServiceTest {

    @Mock
    private MercadoPagoPort mercadoPagoPort;

    @InjectMocks
    private CriarOrdemMercadoPagoService criarOrdemMercadoPagoService;

    private Long pedidoId;
    private BigDecimal totalAmount;
    private String payerEmail;
    private OrdemPagamento ordemPagamentoMock;

    @BeforeEach
    void setUp() {
        pedidoId = 1L;
        totalAmount = new BigDecimal("100.00");
        payerEmail = "test@example.com";

        ordemPagamentoMock = OrdemPagamento.builder()
                .id("order_123")
                .externalReference("pedido_1_123456789")
                .totalAmount(totalAmount)
                .status("pending")
                .paymentMethodId("pix")
                .paymentMethodType("bank_transfer")
                .payerEmail(payerEmail)
                .qrCode("qr_code_data")
                .qrCodeBase64("base64_data")
                .ticketUrl("https://ticket.url")
                .dateCreated(LocalDateTime.now())
                .dateLastUpdated(LocalDateTime.now())
                .build();
    }

    @Test
    void deveCriarOrdemPagamentoComSucesso() {
        // Given
        when(mercadoPagoPort.criarOrdemPagamento(any(OrdemPagamento.class)))
                .thenReturn(ordemPagamentoMock);

        // When
        OrdemPagamento resultado = criarOrdemMercadoPagoService.criarOrdemPagamento(
                pedidoId, totalAmount, payerEmail);

        // Then
        assertNotNull(resultado);
        assertEquals("order_123", resultado.getId());
        assertEquals(totalAmount, resultado.getTotalAmount());
        assertEquals(payerEmail, resultado.getPayerEmail());
        assertEquals("pix", resultado.getPaymentMethodId());
        assertEquals("bank_transfer", resultado.getPaymentMethodType());

        verify(mercadoPagoPort).criarOrdemPagamento(any(OrdemPagamento.class));
    }

    @Test
    void deveCriarOrdemPagamentoComEmailPadrao() {
        // Given
        when(mercadoPagoPort.criarOrdemPagamento(any(OrdemPagamento.class)))
                .thenReturn(ordemPagamentoMock);

        // When
        OrdemPagamento resultado = criarOrdemMercadoPagoService.criarOrdemPagamento(
                pedidoId, totalAmount, null);

        // Then
        assertNotNull(resultado);
        verify(mercadoPagoPort).criarOrdemPagamento(argThat(ordem ->
                "test@testuser.com".equals(ordem.getPayerEmail())));
    }

    @Test
    void deveLancarExcecaoQuandoMercadoPagoFalhar() {
        // Given
        when(mercadoPagoPort.criarOrdemPagamento(any(OrdemPagamento.class)))
                .thenThrow(new RuntimeException("Erro na comunicação"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                criarOrdemMercadoPagoService.criarOrdemPagamento(pedidoId, totalAmount, payerEmail));

        assertEquals("Falha ao processar pagamento via Mercado Pago", exception.getMessage());
        verify(mercadoPagoPort).criarOrdemPagamento(any(OrdemPagamento.class));
    }
}
