package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.adapter.out.persistence.entity.StatusPagamentoEntity;
import br.com.postech.techchallange.adapter.out.persistence.repository.StatusPagamentoJpaRepository;
import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.StatusPagamento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatusPagamentoRepositoryAdapterTest {

    private StatusPagamentoJpaRepository jpaRepository;
    private StatusPagamentoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(StatusPagamentoJpaRepository.class);
        adapter = new StatusPagamentoRepositoryAdapter(jpaRepository);
    }


    // Testes para retornar o status do pagamento através do Id ---------------------------------------------------------------


    @Test
    @DisplayName("Deve retornar StatusPagamento por ID existente")
    void deveBuscarPorId() {
        StatusPagamentoEntity entity = criarStatusPagamentoEntity(StatusPagamentoEnum.FINALIZADO, 1L);
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<StatusPagamento> resultado = adapter.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(StatusPagamentoEnum.FINALIZADO, resultado.get().getNomeStatus());
        verify(jpaRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por ID inexistente")
    void deveRetornarVazioPorIdInexistente() {
        when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<StatusPagamento> resultado = adapter.buscarPorId(999L);

        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findById(999L);
    }


    // Testes para retornar o status do pagamento através do status ---------------------------------------------------------------


    @Test
    @DisplayName("Deve retornar StatusPagamento por status (ignorando case)")
    void deveBuscarPorStatus() {
        StatusPagamentoEntity entity = criarStatusPagamentoEntity(StatusPagamentoEnum.FINALIZADO, 1L);
        when(jpaRepository.findByNomeStatusIgnoreCase("FINALIZADO")).thenReturn(Optional.of(entity));

        Optional<StatusPagamento> resultado = adapter.buscarStatusPagamentoPorStatus("FINALIZADO");

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdStatusPagamento());
        verify(jpaRepository).findByNomeStatusIgnoreCase("FINALIZADO");
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por status inexistente")
    void deveRetornarVazioPorStatusInexistente() {
        when(jpaRepository.findByNomeStatusIgnoreCase("invalido")).thenReturn(Optional.empty());

        Optional<StatusPagamento> resultado = adapter.buscarStatusPagamentoPorStatus("invalido");

        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findByNomeStatusIgnoreCase("invalido");
    }


    // Testes para listar todos os status ---------------------------------------------------------------


    @Test
    @DisplayName("Deve listar todos os StatusPagamento")
    void deveListarTodos() {
        StatusPagamentoEntity entity1 = criarStatusPagamentoEntity(StatusPagamentoEnum.FINALIZADO, 1L);
        StatusPagamentoEntity entity2 = criarStatusPagamentoEntity(StatusPagamentoEnum.PENDENTE, 2L);

        when(jpaRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<StatusPagamento> resultado = adapter.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals(StatusPagamentoEnum.FINALIZADO, resultado.get(0).getNomeStatus());
        assertEquals(StatusPagamentoEnum.PENDENTE, resultado.get(1).getNomeStatus());
        verify(jpaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao houver status cadastrados")
    void deveRetornarListaVaziaQuandoNaoHouverStatus() {
        when(jpaRepository.findAll()).thenReturn(List.of());

        List<StatusPagamento> resultado = adapter.listarTodos();

        assertNotNull(resultado); // a lista deve existir
        assertTrue(resultado.isEmpty()); // mas estar vazia
        verify(jpaRepository).findAll();
    }


    private StatusPagamentoEntity criarStatusPagamentoEntity(StatusPagamentoEnum status, Long id) {
        StatusPagamentoEntity entity = new StatusPagamentoEntity();
        entity.setIdStatusPagamento(id);
        entity.setNomeStatus(status.name());
        return entity;
    }
}
