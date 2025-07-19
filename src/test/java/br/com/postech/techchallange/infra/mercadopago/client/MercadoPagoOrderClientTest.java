package br.com.postech.techchallange.infra.mercadopago.client;

import br.com.postech.techchallange.domain.model.OrdemPagamento;
import br.com.postech.techchallange.infra.mercadopago.dto.MercadoPagoOrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MercadoPagoOrderClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MercadoPagoOrderClient mercadoPagoOrderClient;

    private OrdemPagamento ordemPagamento;
    private MercadoPagoOrderResponse mockResponse;

    @BeforeEach
    void setUp() {
        // Configura propriedades via reflexão
        ReflectionTestUtils.setField(mercadoPagoOrderClient, "baseUrl", "https://api.mercadopago.com");
        ReflectionTestUtils.setField(mercadoPagoOrderClient, "accessToken", "TEST-TOKEN");

        ordemPagamento = OrdemPagamento.builder()
                .externalReference("pedido_1_123456789")
                .totalAmount(new BigDecimal("100.00"))
                .paymentMethodId("pix")
                .paymentMethodType("bank_transfer")
                .payerEmail("test@example.com")
                .build();

        setupMockResponse();
    }

    private void setupMockResponse() {
        mockResponse = new MercadoPagoOrderResponse();
        mockResponse.setId("order_123");
        mockResponse.setStatus("pending");
        mockResponse.setTotalAmount(new BigDecimal("100.00"));
        mockResponse.setExternalReference("pedido_1_123456789");
        mockResponse.setCreatedDate(OffsetDateTime.now());
        mockResponse.setLastUpdatedDate(OffsetDateTime.now());

        // Setup transactions with payment data
        var transactions = new MercadoPagoOrderResponse();
        var payment = new MercadoPagoOrderResponse.PaymentMethod();
        payment.setId("payment_123");

        payment.setQrCode("qr_code_data");
        payment.setQrCodeBase64("base64_data");
        payment.setTicketUrl("https://ticket.url");

    }

    @Test
    void deveCriarOrdemPagamentoComSucesso() {
        // Given
        ResponseEntity<MercadoPagoOrderResponse> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(MercadoPagoOrderResponse.class)))
                .thenReturn(responseEntity);

        // When
        OrdemPagamento resultado = mercadoPagoOrderClient.criarOrdemPagamento(ordemPagamento);

        // Then
        assertNotNull(resultado);
        assertEquals("order_123", resultado.getId());
        assertEquals("pedido_1_123456789", resultado.getExternalReference());
        assertEquals(new BigDecimal("100.00"), resultado.getTotalAmount());
        assertEquals("pending", resultado.getStatus());
        assertEquals("test@example.com", resultado.getPayerEmail());
        assertEquals("qr_code_data", resultado.getQrCode());
        assertEquals("base64_data", resultado.getQrCodeBase64());
        assertEquals("https://ticket.url", resultado.getTicketUrl());

        verify(restTemplate).exchange(
                eq("https://api.mercadopago.com/v1/orders"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(MercadoPagoOrderResponse.class));
    }

    @Test
    void deveLancarExcecaoQuandoRespostaNula() {
        // Given
        ResponseEntity<MercadoPagoOrderResponse> responseEntity =
                new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(MercadoPagoOrderResponse.class)))
                .thenReturn(responseEntity);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                mercadoPagoOrderClient.criarOrdemPagamento(ordemPagamento));

        assertTrue(exception.getMessage().contains("Resposta vazia do Mercado Pago"));
    }

    @Test
    void deveLancarExcecaoQuandoRestTemplateFalhar() {
        // Given
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(MercadoPagoOrderResponse.class)))
                .thenThrow(new RuntimeException("Erro de conexão"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                mercadoPagoOrderClient.criarOrdemPagamento(ordemPagamento));

        assertTrue(exception.getMessage().contains("Falha na comunicação com Mercado Pago"));
    }
}
