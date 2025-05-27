package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.out.PedidoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GerenciarPedidoServiceTest {

    private PedidoRepositoryPort repository;
    private GerenciarPedidoService service;

    @BeforeEach
    void setUp() {
        repository = mock(PedidoRepositoryPort.class);
        service = new GerenciarPedidoService(repository);
    }


    // Testes para salvar informações ---------------------------------------------------------------


    @Test
    @DisplayName("Deve criar um pedido com sucesso")
    void deveCriarPedidoComSucesso() {
        Pedido pedido = criarPedido(1L);
        when(repository.salvar(pedido)).thenReturn(pedido);

        Pedido resultado = service.criarPedido(pedido);

        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getId());
        verify(repository).salvar(pedido);
    }

    @Test
    @DisplayName("Deve lançar excecao ao tentar salvar pedido e ocorrer falha")
    void deveFalharAoCriarPedido() {
        Pedido pedido = criarPedido(1L);
        when(repository.salvar(pedido)).thenThrow(new RuntimeException("Erro ao salvar"));

        assertThrows(RuntimeException.class, () -> service.criarPedido(pedido));
        verify(repository).salvar(pedido);
    }


    // Testes para buscar pedido por ID ---------------------------------------------------------------


    @Test
    @DisplayName("Deve retornar o pedido pelo ID")
    void deveBuscarPedidoPorIdComSucesso() {
        Pedido pedido = criarPedido(1L);
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = service.buscarPedido(1L);

        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getId());
        verify(repository).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar null ao buscar pedido por ID inexistente")
    void deveRetornarNullSePedidoNaoExistir() {
        when(repository.buscarPorId(99L)).thenReturn(Optional.empty());

        Pedido resultado = service.buscarPedido(99L);

        assertNull(resultado);
        verify(repository).buscarPorId(99L);
    }


    // Testes para listar os pedidos ---------------------------------------------------------------


    @Test
    @DisplayName("Deve listar todos os pedidos")
    void deveListarTodosOsPedidos() {
        Pedido pedido1 = criarPedido(1L);
        Pedido pedido2 = new Pedido(2L, 11L, LocalDateTime.now(), 2L, LocalDateTime.now());
        when(repository.listarTodos()).thenReturn(List.of(pedido1, pedido2));

        List<Pedido> pedidos = service.listarPedidos();

        assertEquals(2, pedidos.size());
        verify(repository).listarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia se nao houver pedidos")
    void deveRetornarListaVaziaSeNaoHouverPedidos() {
        when(repository.listarTodos()).thenReturn(List.of());

        List<Pedido> pedidos = service.listarPedidos();

        assertTrue(pedidos.isEmpty());
        verify(repository).listarTodos();
    }

    private Pedido criarPedido(Long id) {
        return new Pedido(
                id,
                10L,
                LocalDateTime.now(),
                1L,
                LocalDateTime.now()
        );
    }

}