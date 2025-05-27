package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.PedidoProduto;
import br.com.postech.techchallange.domain.port.out.PedidoProdutoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuscarPedidoProdutoPorPedidoServiceTest {

    private PedidoProdutoRepositoryPort repository;
    private BuscarPedidoProdutoPorPedidoService service;

    @BeforeEach
    void setUp() {
        repository = mock(PedidoProdutoRepositoryPort.class);
        service = new BuscarPedidoProdutoPorPedidoService(repository);
    }


    // Testes para buscar PedidoProduto por ID ---------------------------------------------------------------


    @Test
    @DisplayName("Deve retornar lista de PedidoProduto ao buscar por ID do pedido")
    void deveRetornarListaDeProdutos() {
        // Arrange
        Long idPedido = 1L;
        List<PedidoProduto> mockProdutos = Arrays.asList(
                new PedidoProduto(1L, idPedido, 101L, 2, new BigDecimal("10.00")),
                new PedidoProduto(2L, idPedido, 102L, 1, new BigDecimal("15.50"))
        );

        when(repository.buscarPorIdPedido(idPedido)).thenReturn(mockProdutos);

        // Act
        List<PedidoProduto> resultado = service.buscarPorIdPedido(idPedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(repository, times(1)).buscarPorIdPedido(idPedido);
    }

    @Test
    @DisplayName("Deve retornar lista vazia se nenhum produto for encontrado")
    void deveRetornarListaVazia() {
        // Arrange
        Long idPedido = 99L;
        when(repository.buscarPorIdPedido(idPedido)).thenReturn(List.of());

        // Act
        List<PedidoProduto> resultado = service.buscarPorIdPedido(idPedido);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).buscarPorIdPedido(idPedido);
    }
}
