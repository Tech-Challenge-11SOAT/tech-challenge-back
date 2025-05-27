package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoProdutoEntity;
import br.com.postech.techchallange.adapter.out.persistence.mapper.PedidoProdutoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.PedidoProdutoJpaRepository;
import br.com.postech.techchallange.domain.model.PedidoProduto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoProdutoRepositoryAdapterTest {

    private PedidoProdutoJpaRepository jpaRepository;
    private PedidoProdutoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(PedidoProdutoJpaRepository.class);
        adapter = new PedidoProdutoRepositoryAdapter(jpaRepository);
    }

    // Testes para listar PedidoProduto através do ID do pedido ---------------------------------------------------------------

    @Test
    @DisplayName("Deve retornar lista de PedidoProduto ao buscar por idPedido")
    void deveBuscarPedidoProdutoPorIdPedido() {
        PedidoProduto pedidoProduto = criarPedidoProduto();
        PedidoProdutoEntity entity = PedidoProdutoMapper.toEntity(pedidoProduto);

        when(jpaRepository.findByIdPedido(100L)).thenReturn(List.of(entity));

        List<PedidoProduto> resultado = adapter.buscarPorIdPedido(100L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(pedidoProduto.getIdPedido(), resultado.get(0).getIdPedido());
        verify(jpaRepository).findByIdPedido(100L);
    }

    @Test
    @DisplayName("Deve retornar lista vazia se nao houver itens com idPedido")
    void deveRetornarListaVaziaSeNaoHouverItens() {
        when(jpaRepository.findByIdPedido(999L)).thenReturn(List.of());

        List<PedidoProduto> resultado = adapter.buscarPorIdPedido(999L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findByIdPedido(999L);
    }


    // Testes para salvar um pedido ---------------------------------------------------------------


    @Test
    @DisplayName("Deve salvar um item de pedido com sucesso")
    void deveSalvarItemPedido() {
        PedidoProduto pedidoProduto = criarPedidoProduto();
        PedidoProdutoEntity entity = PedidoProdutoMapper.toEntity(pedidoProduto);

        when(jpaRepository.save(any(PedidoProdutoEntity.class))).thenReturn(entity);

        PedidoProdutoEntity resultado = adapter.salvarItemPedido(pedidoProduto);

        assertNotNull(resultado);
        assertEquals(entity.getIdPedido(), resultado.getIdPedido());
        verify(jpaRepository).save(any(PedidoProdutoEntity.class));
    }

    @Test
    @DisplayName("Deve lançar excecao se ocorrer erro ao salvar item de pedido")
    void deveLancarErroAoSalvarItemPedido() {
        PedidoProduto pedidoProduto = criarPedidoProduto();

        when(jpaRepository.save(any(PedidoProdutoEntity.class)))
                .thenThrow(new RuntimeException("Falha ao salvar"));

        assertThrows(RuntimeException.class, () -> adapter.salvarItemPedido(pedidoProduto));
        verify(jpaRepository).save(any(PedidoProdutoEntity.class));
    }

    private PedidoProduto criarPedidoProduto() {
        return new PedidoProduto(
                1L,
                100L, // idPedido
                200L, // idProduto
                2,    // quantidade
                new BigDecimal("49.90")
        );
    }
}
