package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.adapter.in.rest.response.PedidoCompletoResponse;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.out.PedidoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testes do Gerenciador de Pedidos")
class GerenciarPedidoServiceTest {

    private PedidoRepositoryPort repository;
    private GerenciarPedidoService service;

    @BeforeEach
    void setUp() {
        repository = mock(PedidoRepositoryPort.class);
        service = new GerenciarPedidoService(repository);
    }

    @Nested
    @DisplayName("Testes de Criação de Pedido")
    class CriacaoPedido {
        @Test
        @DisplayName("Deve criar um pedido com cliente com sucesso")
        void deveCriarPedidoComClienteComSucesso() {
            Pedido pedido = criarPedido(1L);
            pedido.setIdCliente(1L);
            when(repository.salvar(pedido)).thenReturn(pedido);

            Pedido resultado = service.criarPedido(pedido);

            assertNotNull(resultado);
            assertEquals(pedido.getId(), resultado.getId());
            assertEquals(1L, resultado.getIdCliente());
            verify(repository).salvar(pedido);
        }

        @Test
        @DisplayName("Deve criar pedido anônimo e definir fila quando cliente for nulo")
        void deveCriarPedidoAnonimoComFilaQuandoClienteNulo() {
            Pedido pedido = criarPedido(1L);
            pedido.setIdCliente(null);
            when(repository.buscarMaxFilaPedidoAnonimo()).thenReturn(5);
            when(repository.salvar(any(Pedido.class))).thenReturn(pedido);

            Pedido resultado = service.criarPedido(pedido);

            assertNotNull(resultado);
            verify(repository).buscarMaxFilaPedidoAnonimo();
            verify(repository).salvar(any(Pedido.class));
        }

        @Test
        @DisplayName("Deve criar pedido anônimo com fila 1 quando não houver pedidos anteriores")
        void deveCriarPedidoAnonimoComFilaUmQuandoNaoHouverPedidosAnteriores() {
            Pedido pedido = criarPedido(1L);
            pedido.setIdCliente(null);
            when(repository.buscarMaxFilaPedidoAnonimo()).thenReturn(null);
            when(repository.salvar(any(Pedido.class))).thenReturn(pedido);

            service.criarPedido(pedido);

            verify(repository).buscarMaxFilaPedidoAnonimo();
            verify(repository).salvar(any(Pedido.class));
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Status")
    class AtualizacaoStatus {
        @Test
        @DisplayName("Deve atualizar status do pedido com sucesso")
        void deveAtualizarStatusComSucesso() {
            Pedido pedido = criarPedido(1L);
            Pedido pedidoAtualizado = criarPedido(1L);
            pedidoAtualizado.setIdStatusPedido(2L);

            when(repository.atualizar(pedido, 2L)).thenReturn(pedidoAtualizado);

            Pedido resultado = service.atualizarPedido(pedido, 2L);

            assertNotNull(resultado);
            assertEquals(2L, resultado.getIdStatusPedido());
            verify(repository).atualizar(pedido, 2L);
        }
    }

    @Nested
    @DisplayName("Testes de Consulta de Pedidos")
    class ConsultaPedidos {
        @Test
        @DisplayName("Deve retornar lista de pedidos completos")
        void deveRetornarListaPedidosCompletos() {
            List<PedidoCompletoResponse> pedidos = Arrays.asList(
                    criarPedidoCompletoResponse(1L),
                    criarPedidoCompletoResponse(2L),
                    criarPedidoCompletoResponse(3L)
            );

            when(repository.listarTodos()).thenReturn(pedidos);

            List<PedidoCompletoResponse> resultados = service.listarPedidos();

            assertNotNull(resultados);
            assertEquals(3, resultados.size());
            verify(repository).listarTodos();
        }

        @Test
        @DisplayName("Deve lançar exceção quando não houver pedidos")
        void deveLancarExcecaoQuandoNaoHouverPedidos() {
            when(repository.listarTodos()).thenReturn(List.of());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> service.listarPedidos());

            assertEquals("Nenhum pedido encontrado", exception.getMessage());
            verify(repository).listarTodos();
        }

        @Test
        @DisplayName("Deve lançar exceção quando lista for nula")
        void deveLancarExcecaoQuandoListaForNula() {
            when(repository.listarTodos()).thenReturn(null);

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> service.listarPedidos());

            assertEquals("Nenhum pedido encontrado", exception.getMessage());
            verify(repository).listarTodos();
        }

        @Test
        @DisplayName("Deve buscar pedido por ID com sucesso")
        void deveBuscarPedidoPorId() {
            Long id = 1L;
            Pedido pedido = criarPedido(id);
            when(repository.buscarPorId(id)).thenReturn(Optional.of(pedido));

            Pedido resultado = service.buscarPedido(id);

            assertNotNull(resultado);
            assertEquals(id, resultado.getId());
            verify(repository).buscarPorId(id);
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar pedido inexistente")
        void deveLancarExcecaoAoBuscarPedidoInexistente() {
            Long idInexistente = 999L;
            when(repository.buscarPorId(idInexistente)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> service.buscarPedido(idInexistente));

            assertTrue(exception.getMessage().contains("não encontrato"));
            verify(repository).buscarPorId(idInexistente);
        }

        @Test
        @DisplayName("Deve listar pedidos por status")
        void deveListarPedidosPorStatus() {
            Long statusId = 1L;
            List<PedidoCompletoResponse> pedidos = Arrays.asList(
                    criarPedidoCompletoResponse(1L),
                    criarPedidoCompletoResponse(2L)
            );

            when(repository.listarPorStatus(statusId)).thenReturn(pedidos);

            List<PedidoCompletoResponse> resultados = service.listarPorStatus(statusId);

            assertNotNull(resultados);
            assertEquals(2, resultados.size());
            verify(repository).listarPorStatus(statusId);
        }

        @Test
        @DisplayName("Deve lançar exceção quando não houver pedidos para o status")
        void deveLancarExcecaoQuandoNaoHouverPedidosParaStatus() {
            Long statusId = 999L;
            when(repository.listarPorStatus(statusId)).thenReturn(List.of());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> service.listarPorStatus(statusId));

            assertTrue(exception.getMessage().contains("status 999"));
            verify(repository).listarPorStatus(statusId);
        }
    }

    private Pedido criarPedido(Long id) {
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setIdCliente(1L);
        pedido.setIdStatusPedido(1L);
        pedido.setFilaPedido(1);
        return pedido;
    }

    private PedidoCompletoResponse criarPedidoCompletoResponse(Long id) {
        return new PedidoCompletoResponse(
                id,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new PedidoCompletoResponse.Cliente(1L, "Cliente Teste", "cliente@teste.com"),
                new PedidoCompletoResponse.StatusPedido(1L, "Em Preparação"),
                1L
        );
    }
}
