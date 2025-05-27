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

    private ProdutoRepositoryPort repository;
    private BuscarProdutoService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ProdutoRepositoryPort.class);
        service = new BuscarProdutoService(repository);
    }

    @Test
    void deveRetornarProdutoQuandoIdExistir() {
        // Arrange
        Produto produto = criarProduto(1L);

        when(repository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        // Act
        Optional<Produto> resultado = service.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Produto Teste", resultado.get().getNome());
        verify(repository, times(1)).buscarPorId(1L);
    }

    @Test
    void deveRetornarEmptyQuandoIdNaoExistir() {
        // Arrange
        when(repository.buscarPorId(2L)).thenReturn(Optional.empty());

        // Act
        Optional<Produto> resultado = service.buscarPorId(2L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(repository, times(1)).buscarPorId(2L);
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