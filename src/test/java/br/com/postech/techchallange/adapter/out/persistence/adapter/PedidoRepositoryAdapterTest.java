package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoEntity;
import br.com.postech.techchallange.adapter.out.persistence.mapper.PedidoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.PedidoJpaRepository;
import br.com.postech.techchallange.domain.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoRepositoryAdapterTest {

    private PedidoJpaRepository jpaRepository;
    private PedidoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(PedidoJpaRepository.class);
        adapter = new PedidoRepositoryAdapter(jpaRepository);
    }


    // Testes para salvar informações ---------------------------------------------------------------


    @Test
    @DisplayName("Deve salvar as informacoes do pedido no banco")
    void deveSalvarPedidoComSucesso() {
        // Arrange
        Pedido pedido = criarPedido(1L);
        PedidoEntity entity = PedidoMapper.toEntity(pedido);

        when(jpaRepository.save(any(PedidoEntity.class))).thenReturn(entity);

        // Act
        Pedido resultado = adapter.salvar(pedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getId());
        verify(jpaRepository).save(any(PedidoEntity.class));
    }

    @Test
    @DisplayName("Deve falhar ao salvar pedido e lancar excecao")
    void deveFalharAoSalvarPedido() {
        // Arrange
        Pedido pedido = criarPedido(null);
        when(jpaRepository.save(any(PedidoEntity.class)))
                .thenThrow(new RuntimeException("Erro ao salvar"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> adapter.salvar(pedido));
        verify(jpaRepository).save(any(PedidoEntity.class));
    }


    // Testes para buscar pedido por ID ---------------------------------------------------------------


    @Test
    @DisplayName("Deve buscar pedido por ID com sucesso")
    void deveBuscarPedidoPorIdComSucesso() {
        // Arrange
        Pedido pedido = criarPedido(2L);
        PedidoEntity entity = PedidoMapper.toEntity(pedido);

        when(jpaRepository.findById(2L)).thenReturn(Optional.of(entity));

        // Act
        Optional<Pedido> resultado = adapter.buscarPorId(2L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(2L, resultado.get().getId());
        verify(jpaRepository).findById(2L);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar pedido por ID inexistente")
    void deveRetornarVazioAoBuscarPedidoInexistente() {
        // Arrange
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Pedido> resultado = adapter.buscarPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(jpaRepository).findById(99L);
    }


    // Testes para listar pedidos do banco ---------------------------------------------------------------


    @Test
    @DisplayName("Deve listar todos os pedidos do banco")
    void deveListarTodosPedidos() {
        // Arrange
        PedidoEntity pedido1 = PedidoMapper.toEntity(criarPedido(1L));
        PedidoEntity pedido2 = PedidoMapper.toEntity(criarPedido(2L));

        when(jpaRepository.findAll()).thenReturn(Arrays.asList(pedido1, pedido2));

        // Act
        List<Pedido> resultado = adapter.listarTodos();

        // Assert
        assertEquals(2, resultado.size());
        verify(jpaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao houver pedidos")
    void deveRetornarListaVaziaSeNaoHouverPedidos() {
        // Arrange
        when(jpaRepository.findAll()).thenReturn(List.of());

        // Act
        List<Pedido> resultado = adapter.listarTodos();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findAll();
    }


    private Pedido criarPedido(Long id) {
        return new Pedido(
                id,
                100L,
                LocalDateTime.of(2024, 5, 1, 14, 0),
                1L,
                LocalDateTime.of(2024, 5, 1, 15, 0)
        );
    }

}