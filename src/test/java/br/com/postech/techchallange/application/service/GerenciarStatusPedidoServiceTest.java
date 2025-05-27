package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.StatusPedido;
import br.com.postech.techchallange.domain.port.out.StatusPedidoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GerenciarStatusPedidoServiceTest {

    private StatusPedidoRepositoryPort repository;
    private GerenciarStatusPedidoService service;

    @BeforeEach
    void setUp() {
        repository = mock(StatusPedidoRepositoryPort.class);
        service = new GerenciarStatusPedidoService(repository);
    }


    // Testes para buscar StatusPedido por ID ---------------------------------------


    @Test
    @DisplayName("Deve retornar StatusPedido ao buscar por ID existente")
    void deveRetornarStatusPedidoPorId() {
        Long id = 1L;
        StatusPedido status = new StatusPedido(id, "CONFIRMADO");
        when(repository.buscarPorId(id)).thenReturn(Optional.of(status));

        StatusPedido resultado = service.buscarStatusPedidoPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdStatusPedido());
        assertEquals("CONFIRMADO", resultado.getNomeStatus());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por ID inexistente")
    void deveLancarExcecaoSeIdNaoExistir() {
        Long id = 2L;
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            service.buscarStatusPedidoPorId(id);
        });

        assertEquals("Status do pedido não encontrado para o ID: " + id, excecao.getMessage());
        verify(repository).buscarPorId(id);
    }


    // Testes para buscar StatusPedido por nome -------------------------------------


    @Test
    @DisplayName("Deve retornar StatusPedido ao buscar por nome existente")
    void deveRetornarStatusPedidoPorNome() {
        String nome = "PREPARANDO";
        StatusPedido status = new StatusPedido(3L, nome);
        when(repository.buscarPorNome(nome)).thenReturn(Optional.of(status));

        StatusPedido resultado = service.buscarStatusPedidoPorNome(nome);

        assertNotNull(resultado);
        assertEquals(nome, resultado.getNomeStatus());
        verify(repository).buscarPorNome(nome);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por nome inexistente")
    void deveLancarExcecaoSeNomeNaoExistir() {
        String nome = "INVALIDO";
        when(repository.buscarPorNome(nome)).thenReturn(Optional.empty());

        EntityNotFoundException excecao = assertThrows(EntityNotFoundException.class, () -> {
            service.buscarStatusPedidoPorNome(nome);
        });

        assertEquals("Status do pedido não encontrado para o nome: " + nome, excecao.getMessage());
        verify(repository).buscarPorNome(nome);
    }


    // Testes para listar todos os StatusPedido -------------------------------------


    @Test
    @DisplayName("Deve retornar lista com todos os StatusPedido")
    void deveListarTodosOsStatusPedidos() {
        List<StatusPedido> lista = List.of(
                new StatusPedido(1L, "CONFIRMADO"),
                new StatusPedido(2L, "ENTREGUE")
        );
        when(repository.listaTodos()).thenReturn(lista);

        List<StatusPedido> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals("CONFIRMADO", resultado.get(0).getNomeStatus());
        assertEquals("ENTREGUE", resultado.get(1).getNomeStatus());
        verify(repository).listaTodos();
    }
}
