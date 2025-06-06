package br.com.postech.techchallange.application.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postech.techchallange.adapter.in.rest.request.FakeCheckoutRequest;
import br.com.postech.techchallange.adapter.in.rest.request.PedidoPagamentoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.PedidoPagamentoResponse;

@ExtendWith(MockitoExtension.class)
class FakeCheckoutServiceTest {

    @Mock
    private OrquestradorPedidoPagamentoService orquestradorPedidoPagamentoService;

    @Spy
    @InjectMocks
    private FakeCheckoutService fakeCheckoutService;

    @Captor
    private ArgumentCaptor<PedidoPagamentoRequest> pagamentoRequestCaptor;

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

    private FakeCheckoutRequest.ItemProduto createProdutoRequest(Long id, int quantidade) {
        FakeCheckoutRequest.ItemProduto produto = new FakeCheckoutRequest.ItemProduto();
        produto.setIdProduto(id);
        produto.setQuantidade(quantidade);
        return produto;
    }
}
