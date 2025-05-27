package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.adapter.out.persistence.entity.StatusPedidoEntity;
import br.com.postech.techchallange.adapter.out.persistence.repository.StatusPedidoJpaRepository;
import br.com.postech.techchallange.domain.model.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatusPedidoRepositoryAdapterTest {

    private StatusPedidoJpaRepository jpaRepository;
    private StatusPedidoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(StatusPedidoJpaRepository.class);
        adapter = new StatusPedidoRepositoryAdapter(jpaRepository);
    }

    // Testes para retornar o status do pedido através do Id ---------------------------------------------------------------

    @Test
    @DisplayName("Deve retornar StatusPedido por ID existente")
    void deveBuscarPorId() {
        StatusPedidoEntity entity = criarStatusPedidoEntity(1L, "RECEBIDO");
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<StatusPedido> resultado = adapter.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("RECEBIDO", resultado.get().getNomeStatus());
        verify(jpaRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por ID inexistente")
    void deveRetornarVazioPorIdInexistente() {
        when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<StatusPedido> resultado = adapter.buscarPorId(999L);

        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findById(999L);
    }


    // Testes para retornar o status do pedido através do nome ---------------------------------------------------------------


    @Test
    @DisplayName("Deve retornar StatusPedido por nome (ignorando case)")
    void deveBuscarPorNome() {
        StatusPedidoEntity entity = criarStatusPedidoEntity(2L, "EM_PREPARO");
        when(jpaRepository.findByNomeStatusIgnoreCase("em_preparo")).thenReturn(Optional.of(entity));

        Optional<StatusPedido> resultado = adapter.buscarPorNome("em_preparo");

        assertTrue(resultado.isPresent());
        assertEquals("EM_PREPARO", resultado.get().getNomeStatus());
        verify(jpaRepository).findByNomeStatusIgnoreCase("em_preparo");
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por nome inexistente")
    void deveRetornarVazioPorNomeInexistente() {
        when(jpaRepository.findByNomeStatusIgnoreCase("invalido")).thenReturn(Optional.empty());

        Optional<StatusPedido> resultado = adapter.buscarPorNome("invalido");

        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findByNomeStatusIgnoreCase("invalido");
    }


    // Testes para listar todos os status de pedido ---------------------------------------------------------------


    @Test
    @DisplayName("Deve listar todos os StatusPedido")
    void deveListarTodos() {
        StatusPedidoEntity entity1 = criarStatusPedidoEntity(1L, "RECEBIDO");
        StatusPedidoEntity entity2 = criarStatusPedidoEntity(2L, "FINALIZADO");

        when(jpaRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<StatusPedido> resultado = adapter.listaTodos();

        assertEquals(2, resultado.size());
        assertEquals("RECEBIDO", resultado.get(0).getNomeStatus());
        assertEquals("FINALIZADO", resultado.get(1).getNomeStatus());
        verify(jpaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao houver status cadastrados")
    void deveRetornarListaVaziaQuandoNaoHouverStatus() {
        when(jpaRepository.findAll()).thenReturn(List.of());

        List<StatusPedido> resultado = adapter.listaTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findAll();
    }


    private StatusPedidoEntity criarStatusPedidoEntity(Long id, String nome) {
        StatusPedidoEntity entity = new StatusPedidoEntity();
        entity.setIdStatusPedido(id);
        entity.setNomeStatus(nome);
        return entity;
    }
}
