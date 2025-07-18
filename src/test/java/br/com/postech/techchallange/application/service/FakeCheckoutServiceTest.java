package br.com.postech.techchallange.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postech.techchallange.adapter.in.rest.request.FakeCheckoutRequest;
import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;
import br.com.postech.techchallange.domain.model.OrdemPagamento;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.in.CriarOrdemMercadoPagoUseCase;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;

@ExtendWith(MockitoExtension.class)
class FakeCheckoutServiceTest {

    @Mock
    private OrquestradorPedidoPagamentoService orquestradorPedidoPagamentoService;

    @Mock
    private CriarOrdemMercadoPagoUseCase criarOrdemMercadoPagoUseCase;

    @Mock
    private ProdutoRepositoryPort produtoRepositoryPort;

    @Spy
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
                .dateCreated(LocalDateTime.now())
                .dateLastUpdated(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve processar checkout com sucesso quando request válido")
    void deveProcessarCheckoutComSucesso() {
        // Arrange
        Long idCliente = 1L;
        FakeCheckoutRequest request = new FakeCheckoutRequest();
        request.setIdCliente(idCliente);

        FakeCheckoutRequest.ItemProduto produto = new FakeCheckoutRequest.ItemProduto();
        produto.setIdProduto(1L);
        produto.setQuantidade(2);
        request.setProdutos(Arrays.asList(produto));

        PedidoPagamentoResponse expectedResponse = PedidoPagamentoResponse.builder()
                .idPedido(1L)
                .idPagamento(1L)
                .metodoPagamento("PIX")
                .status("RECEBIDO_NAO_PAGO")
                .numeroPedido(1)
                .build();

        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any()))
                .thenReturn(expectedResponse);

        // Act
        PedidoPagamentoResponse response = fakeCheckoutService.processarFakeCheckout(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(fakeCheckoutService).enviarParaFila(request);
        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(pagamentoRequestCaptor.capture());

        PedidoPagamentoRequest capturedRequest = pagamentoRequestCaptor.getValue();
        assertEquals(idCliente, capturedRequest.getIdCliente());
        assertEquals(1, capturedRequest.getProdutos().size());
        assertEquals(1L, capturedRequest.getProdutos().get(0).getIdProduto());
        assertEquals(2, capturedRequest.getProdutos().get(0).getQuantidade());
    }

    @Test
    @DisplayName("Deve processar checkout com sucesso quando cliente é null")
    void deveProcessarCheckoutQuandoClienteNull() {
        // Arrange
        FakeCheckoutRequest request = new FakeCheckoutRequest();
        request.setIdCliente(null);

        FakeCheckoutRequest.ItemProduto produto = new FakeCheckoutRequest.ItemProduto();
        produto.setIdProduto(1L);
        produto.setQuantidade(2);
        request.setProdutos(Arrays.asList(produto));

        PedidoPagamentoResponse expectedResponse = PedidoPagamentoResponse.builder()
                .idPedido(1L)
                .idPagamento(1L)
                .metodoPagamento("PIX")
                .status("RECEBIDO_NAO_PAGO")
                .numeroPedido(1)
                .build();

        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any()))
                .thenReturn(expectedResponse);

        // Act
        PedidoPagamentoResponse response = fakeCheckoutService.processarFakeCheckout(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(fakeCheckoutService).enviarParaFila(request);
        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(pagamentoRequestCaptor.capture());

        PedidoPagamentoRequest capturedRequest = pagamentoRequestCaptor.getValue();
        assertNull(capturedRequest.getIdCliente());
        assertEquals(1, capturedRequest.getProdutos().size());
    }

    @Test
    @DisplayName("Deve processar checkout com sucesso quando lista de produtos está vazia")
    void deveProcessarCheckoutQuandoListaProdutosVazia() {
        // Arrange
        FakeCheckoutRequest request = new FakeCheckoutRequest();
        request.setIdCliente(1L);
        request.setProdutos(new ArrayList<>());

        PedidoPagamentoResponse expectedResponse = PedidoPagamentoResponse.builder()
                .idPedido(1L)
                .idPagamento(1L)
                .metodoPagamento("PIX")
                .status("RECEBIDO_NAO_PAGO")
                .numeroPedido(1)
                .build();

        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any()))
                .thenReturn(expectedResponse);

        // Act
        PedidoPagamentoResponse response = fakeCheckoutService.processarFakeCheckout(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(fakeCheckoutService).enviarParaFila(request);
        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(pagamentoRequestCaptor.capture());

        PedidoPagamentoRequest capturedRequest = pagamentoRequestCaptor.getValue();
        assertEquals(1L, capturedRequest.getIdCliente());
        assertTrue(capturedRequest.getProdutos().isEmpty());
    }

    @Test
    @DisplayName("Deve processar checkout com sucesso quando há múltiplos produtos")
    void deveProcessarCheckoutComMultiplosProdutos() {
        // Arrange
        FakeCheckoutRequest request = new FakeCheckoutRequest();
        request.setIdCliente(1L);

        List<FakeCheckoutRequest.ItemProduto> produtos = Arrays.asList(
                createProdutoRequest(1L, 2),
                createProdutoRequest(2L, 3),
                createProdutoRequest(3L, 1)
        );
        request.setProdutos(produtos);

        PedidoPagamentoResponse expectedResponse = PedidoPagamentoResponse.builder()
                .idPedido(1L)
                .idPagamento(1L)
                .metodoPagamento("PIX")
                .status("RECEBIDO_NAO_PAGO")
                .numeroPedido(1)
                .build();

        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any()))
                .thenReturn(expectedResponse);

        // Act
        PedidoPagamentoResponse response = fakeCheckoutService.processarFakeCheckout(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(fakeCheckoutService).enviarParaFila(request);
        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(pagamentoRequestCaptor.capture());

        PedidoPagamentoRequest capturedRequest = pagamentoRequestCaptor.getValue();
        assertEquals(1L, capturedRequest.getIdCliente());
        assertEquals(3, capturedRequest.getProdutos().size());

        assertEquals(1L, capturedRequest.getProdutos().get(0).getIdProduto());
        assertEquals(2, capturedRequest.getProdutos().get(0).getQuantidade());

        assertEquals(2L, capturedRequest.getProdutos().get(1).getIdProduto());
        assertEquals(3, capturedRequest.getProdutos().get(1).getQuantidade());

        assertEquals(3L, capturedRequest.getProdutos().get(2).getIdProduto());
        assertEquals(1, capturedRequest.getProdutos().get(2).getQuantidade());
    }

    @Test
    @DisplayName("Deve propagar exceção do orquestrador quando ocorrer erro")
    void devePropagaExcecaoDoOrquestrador() {
        // Arrange
        FakeCheckoutRequest request = new FakeCheckoutRequest();
        request.setIdCliente(1L);
        request.setProdutos(Arrays.asList(createProdutoRequest(1L, 1)));

        RuntimeException expectedException = new RuntimeException("Erro no processamento");
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any()))
                .thenThrow(expectedException);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
                () -> fakeCheckoutService.processarFakeCheckout(request));

        assertEquals("Erro no processamento", exception.getMessage());
        verify(fakeCheckoutService).enviarParaFila(request);
        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(any());
    }

    @Test
    void deveProcessarFakeCheckoutComIntegracaoMercadoPago() {
        // Given
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any(PedidoPagamentoRequest.class)))
                .thenReturn(mockResponse);
        when(produtoRepositoryPort.buscarPorId(1L))
                .thenReturn(Optional.of(mockProduto));
        when(criarOrdemMercadoPagoUseCase.criarOrdemPagamento(eq(1L), any(BigDecimal.class), eq("test@testuser.com")))
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
        verify(criarOrdemMercadoPagoUseCase).criarOrdemPagamento(eq(1L), eq(new BigDecimal("100.00")), eq("test@testuser.com"));
    }

    @Test
    void deveProcessarFakeCheckoutMesmoComFalhaNaMercadoPago() {
        // Given
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any(PedidoPagamentoRequest.class)))
                .thenReturn(mockResponse);
        when(produtoRepositoryPort.buscarPorId(1L))
                .thenReturn(Optional.of(mockProduto));
        when(criarOrdemMercadoPagoUseCase.criarOrdemPagamento(any(Long.class), any(BigDecimal.class), any(String.class)))
                .thenThrow(new RuntimeException("Erro na integração"));

        // When
        PedidoPagamentoResponse resultado = fakeCheckoutService.processarFakeCheckout(request);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdPedido());
        assertNull(resultado.getOrderResponse()); // Não deve ter resposta do Mercado Pago devido ao erro

        verify(orquestradorPedidoPagamentoService).orquestrarPedidoPagamento(any(PedidoPagamentoRequest.class));
        verify(criarOrdemMercadoPagoUseCase).criarOrdemPagamento(any(Long.class), any(BigDecimal.class), any(String.class));
    }

    @Test
    void deveCalcularTotalPedidoCorretamente() {
        // Given
        when(produtoRepositoryPort.buscarPorId(1L))
                .thenReturn(Optional.of(mockProduto));
        when(orquestradorPedidoPagamentoService.orquestrarPedidoPagamento(any(PedidoPagamentoRequest.class)))
                .thenReturn(mockResponse);
        when(criarOrdemMercadoPagoUseCase.criarOrdemPagamento(any(Long.class), any(BigDecimal.class), any(String.class)))
                .thenReturn(mockOrdemPagamento);

        // When
        fakeCheckoutService.processarFakeCheckout(request);

        // Then
        verify(criarOrdemMercadoPagoUseCase).criarOrdemPagamento(
                eq(1L),
                eq(new BigDecimal("100.00")), // 50.00 * 2 produtos
                eq("test@testuser.com")
        );
    }

    private FakeCheckoutRequest.ItemProduto createProdutoRequest(Long id, int quantidade) {
        FakeCheckoutRequest.ItemProduto produto = new FakeCheckoutRequest.ItemProduto();
        produto.setIdProduto(id);
        produto.setQuantidade(quantidade);
        return produto;
    }
}
