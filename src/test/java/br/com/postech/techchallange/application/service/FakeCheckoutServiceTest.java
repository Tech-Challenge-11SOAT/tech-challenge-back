package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.adapter.in.rest.request.FakeCheckoutRequest;
import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.model.Cliente;
import br.com.postech.techchallange.domain.model.OrdemPagamento;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.in.CriarOrdemMercadoPagoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarClienteUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import br.com.postech.techchallange.infra.config.MercadoPagoOptionsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FakeCheckoutServiceTest {

    @Mock
    private OrquestradorPedidoPagamentoService orquestradorPedidoPagamentoService;

    @Mock
    private CriarOrdemMercadoPagoUseCase criarOrdemMercadoPagoUseCase;

    @Mock
    private ProdutoRepositoryPort produtoRepositoryPort;

    @Mock
    private MercadoPagoOptionsConfig mercadoPagoOptionsConfig;

    @Mock
    private MercadoPagoOptionsConfig.Options mercadoPagoOptions;

    @Mock
    private GerenciarPagamentoUseCase pagamentoUseCase;

    @Mock
    private GerenciarClienteUseCase gerenciarClienteUseCase;

    @InjectMocks
    private FakeCheckoutService fakeCheckoutService;

    @Captor
    private ArgumentCaptor<PedidoPagamentoRequest> pagamentoRequestCaptor;

    private FakeCheckoutRequest request;
    private PedidoPagamentoResponse mockResponse;
    private OrdemPagamento mockOrdemPagamento;
    private Produto mockProduto;

    @BeforeEach
    void setUp() {
        // Setup request
        request = new FakeCheckoutRequest();
        request.setIdCliente(1L);
        request.setMetodoPagamento("PIX");

        FakeCheckoutRequest.ItemProduto item = new FakeCheckoutRequest.ItemProduto();
        item.setIdProduto(1L);
        item.setQuantidade(2);
        request.setProdutos(Arrays.asList(item));

        // Setup mock responses
        mockResponse = PedidoPagamentoResponse.builder()
                .idPedido(1L)
                .idPagamento(1L)
                .metodoPagamento("PIX")
                .status("RECEBIDO_NAO_PAGO")
                .numeroPedido(1)
                .build();

        mockProduto = new Produto();
        mockProduto.setId(1L);
        mockProduto.setPreco(new BigDecimal("50.00"));

        mockOrdemPagamento = OrdemPagamento.builder()
                .id("order_123")
                .externalReference("pedido_1_123456789")
                .totalAmount(new BigDecimal("100.00"))
                .status("pending")
                .qrCode("qr_code_data")
                .qrCodeBase64("base64_data")
                .ticketUrl("https://ticket.url")
                .dateCreated(OffsetDateTime.now())
                .dateLastUpdated(OffsetDateTime.now())
                .build();

        // Setup common mocks com lenient() para evitar UnnecessaryStubbingException
        lenient().when(mercadoPagoOptionsConfig.getOptions()).thenReturn(mercadoPagoOptions);
        lenient().when(mercadoPagoOptions.getTestMode()).thenReturn(Boolean.TRUE);

        // Setup mock pagamento
        Pagamento mockPagamento = new Pagamento();
        mockPagamento.setValorTotal(new BigDecimal("100.00"));
        lenient().when(pagamentoUseCase.buscarPagamento(1L)).thenReturn(mockPagamento);

        // Setup mock cliente
        Cliente mockCliente = new Cliente();
        mockCliente.setEmailCliente("test@testuser.com");
        lenient().when(gerenciarClienteUseCase.buscarCliente(1L)).thenReturn(mockCliente);
    }

    @Test
    @DisplayName("Deve processar checkout com sucesso quando request válido")
    void deveProcessarCheckoutComSucesso() {
        // Arrange
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any()))
                .thenReturn(mockResponse);

        // Act
        PedidoPagamentoResponse response = fakeCheckoutService.processarFakeCheckout(request);

        // Assert
        assertEquals(mockResponse, response);
        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(any());
    }

    @Test
    @DisplayName("Deve processar checkout quando cliente é null")
    void deveProcessarCheckoutQuandoClienteNull() {
        // Arrange
        request.setIdCliente(null);
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any()))
                .thenReturn(mockResponse);

        // Act
        PedidoPagamentoResponse response = fakeCheckoutService.processarFakeCheckout(request);

        // Assert
        assertEquals(mockResponse, response);
        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(any());
    }

    @Test
    @DisplayName("Deve processar checkout quando lista de produtos está vazia")
    void deveProcessarCheckoutQuandoListaProdutosVazia() {
        // Arrange
        request.setProdutos(new ArrayList<>());
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any()))
                .thenReturn(mockResponse);

        // Act
        PedidoPagamentoResponse response = fakeCheckoutService.processarFakeCheckout(request);

        // Assert
        assertEquals(mockResponse, response);
        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(any());
    }

    @Test
    @DisplayName("Deve processar checkout com múltiplos produtos")
    void deveProcessarCheckoutComMultiplosProdutos() {
        // Arrange
        List<FakeCheckoutRequest.ItemProduto> produtos = Arrays.asList(
                createProdutoRequest(1L, 2),
                createProdutoRequest(2L, 3),
                createProdutoRequest(3L, 1)
        );
        request.setProdutos(produtos);

        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any()))
                .thenReturn(mockResponse);

        // Act
        PedidoPagamentoResponse response = fakeCheckoutService.processarFakeCheckout(request);

        // Assert
        assertEquals(mockResponse, response);
        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(any());
    }

    @Test
    @DisplayName("Deve propagar exceção do orquestrador")
    void devePropagaExcecaoDoOrquestrador() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("Erro no processamento");
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any()))
                .thenThrow(expectedException);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
                () -> fakeCheckoutService.processarFakeCheckout(request));

        assertEquals("Erro no processamento", exception.getMessage());
        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(any());
    }

    @Test
    @DisplayName("Deve processar fake checkout com integração Mercado Pago")
    void deveProcessarFakeCheckoutComIntegracaoMercadoPago() {
        // Given
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any(PedidoPagamentoRequest.class)))
                .thenReturn(mockResponse);
        when(criarOrdemMercadoPagoUseCase.criarOrdemPagamento(eq(1L), any(BigDecimal.class), any(String.class)))
                .thenReturn(mockOrdemPagamento);

        // When
        PedidoPagamentoResponse resultado = fakeCheckoutService.processarFakeCheckout(request);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdPedido());
        assertNotNull(resultado.getOrderResponse());
        assertEquals("order_123", resultado.getOrderResponse().getOrderId());
        assertEquals(new BigDecimal("100.00"), resultado.getOrderResponse().getTotalAmount());
        assertEquals("qr_code_data", resultado.getOrderResponse().getQrCode());

        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(any(PedidoPagamentoRequest.class));
        verify(criarOrdemMercadoPagoUseCase).criarOrdemPagamento(eq(1L), eq(new BigDecimal("100.00")), any(String.class));
    }

    @Test
    @DisplayName("Deve processar fake checkout mesmo com falha na Mercado Pago")
    void deveProcessarFakeCheckoutMesmoComFalhaNaMercadoPago() {
        // Given
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any(PedidoPagamentoRequest.class)))
                .thenReturn(mockResponse);
        when(criarOrdemMercadoPagoUseCase.criarOrdemPagamento(any(Long.class), any(BigDecimal.class), any(String.class)))
                .thenThrow(new RuntimeException("Erro na integração"));

        // When
        PedidoPagamentoResponse resultado = fakeCheckoutService.processarFakeCheckout(request);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdPedido());
        assertNull(resultado.getOrderResponse());

        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(any(PedidoPagamentoRequest.class));
        verify(criarOrdemMercadoPagoUseCase).criarOrdemPagamento(any(Long.class), any(BigDecimal.class), any(String.class));
    }

    @Test
    @DisplayName("Deve calcular total do pedido corretamente")
    void deveCalcularTotalPedidoCorretamente() {
        // Given
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any(PedidoPagamentoRequest.class)))
                .thenReturn(mockResponse);
        when(criarOrdemMercadoPagoUseCase.criarOrdemPagamento(any(Long.class), any(BigDecimal.class), any(String.class)))
                .thenReturn(mockOrdemPagamento);

        // When
        fakeCheckoutService.processarFakeCheckout(request);

        // Then
        verify(criarOrdemMercadoPagoUseCase).criarOrdemPagamento(
                eq(1L),
                eq(new BigDecimal("100.00")),
                any(String.class)
        );
    }

    private FakeCheckoutRequest.ItemProduto createProdutoRequest(Long id, int quantidade) {
        FakeCheckoutRequest.ItemProduto produto = new FakeCheckoutRequest.ItemProduto();
        produto.setIdProduto(id);
        produto.setQuantidade(quantidade);
        return produto;
    }
}
