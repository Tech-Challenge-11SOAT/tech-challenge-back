package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuscarProdutoServiceTest {

    private ProdutoRepositoryPort produtoRepository;
    private BuscarProdutoService buscarProdutoService;

    @BeforeEach
    void setUp() {
        produtoRepository = Mockito.mock(ProdutoRepositoryPort.class);
        buscarProdutoService = new BuscarProdutoService(produtoRepository);
    }

    @Test
    void deveRetornarProdutoQuandoIdExistir() {
        // Arrange
        Produto produto = criarProduto(1L);

        when(produtoRepository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        // Act
        Optional<Produto> resultado = buscarProdutoService.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Produto Teste", resultado.get().getNome());
        verify(produtoRepository, times(1)).buscarPorId(1L);
    }

    @Test
    void deveRetornarEmptyQuandoIdNaoExistir() {
        // Arrange
        when(produtoRepository.buscarPorId(2L)).thenReturn(Optional.empty());

        // Act
        Optional<Produto> resultado = buscarProdutoService.buscarPorId(2L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(produtoRepository, times(1)).buscarPorId(2L);
    }

    private Produto criarProduto(Long id) {
        return new Produto(
                id,
                "Produto Teste",
                "Descricao teste",
                new BigDecimal("10.99"),
                100L,
                10L
        );
    }

}