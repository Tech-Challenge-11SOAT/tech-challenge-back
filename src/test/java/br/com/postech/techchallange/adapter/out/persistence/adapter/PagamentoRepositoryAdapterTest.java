package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.adapter.out.persistence.entity.PagamentoEntity;
import br.com.postech.techchallange.adapter.out.persistence.mapper.PagamentoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.PagamentoJpaRepository;
import br.com.postech.techchallange.domain.model.Pagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagamentoRepositoryAdapterTest {

    private PagamentoJpaRepository jpaRepository;
    private PagamentoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(PagamentoJpaRepository.class);
        adapter = new PagamentoRepositoryAdapter(jpaRepository);
    }


    // Testes para salvar informações ---------------------------------------------------------------


    @Test
    @DisplayName("Deve salvar o pagamento com sucesso")
    void deveSalvarPagamentoComSucesso() {
        Pagamento pagamento = criarPagamento(1L);
        PagamentoEntity entity = PagamentoMapper.toEntity(pagamento);

        when(jpaRepository.save(any(PagamentoEntity.class))).thenReturn(entity);

        Pagamento resultado = adapter.salvar(pagamento);

        assertNotNull(resultado);
        assertEquals(pagamento.getId(), resultado.getId());
        verify(jpaRepository).save(any(PagamentoEntity.class));
    }

    @Test
    @DisplayName("Deve lançar excecao ao salvar pagamento se ocorrer falha no repositório")
    void deveLancarExcecaoAoSalvarPagamento() {
        Pagamento pagamento = criarPagamento(1L);
        when(jpaRepository.save(any(PagamentoEntity.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        assertThrows(RuntimeException.class, () -> adapter.salvar(pagamento));
        verify(jpaRepository).save(any(PagamentoEntity.class));
    }


    // Testes para buscar pagamentos por ID ---------------------------------------------------------------


    @Test
    @DisplayName("Deve retornar o pagamento pelo ID")
    void deveBuscarPagamentoPorIdComSucesso() {
        Pagamento pagamento = criarPagamento(1L);
        PagamentoEntity entity = PagamentoMapper.toEntity(pagamento);

        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Pagamento> resultado = adapter.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(pagamento.getId(), resultado.get().getId());
        verify(jpaRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar pagamento com ID inexistente")
    void deveRetornarVazioSeIdInexistente() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Pagamento> resultado = adapter.buscarPorId(99L);

        assertFalse(resultado.isPresent());
        verify(jpaRepository).findById(99L);
    }


    // Testes para listar pagamentos do banco ---------------------------------------------------------------


    @Test
    @DisplayName("Deve listar todos os pagamentos")
    void deveListarTodosOsPagamentos() {
        PagamentoEntity pagamento1 = PagamentoMapper.toEntity(criarPagamento(1L));
        PagamentoEntity pagamento2 = PagamentoMapper.toEntity(new Pagamento(
                2L, 20L, new BigDecimal("150.00"), "Cartao", 2L, LocalDateTime.now()));

        when(jpaRepository.findAll()).thenReturn(List.of(pagamento1, pagamento2));

        List<Pagamento> resultado = adapter.listarTodos();

        assertEquals(2, resultado.size());
        verify(jpaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao houver pagamentos")
    void deveRetornarListaVaziaSeNaoHouverPagamentos() {
        when(jpaRepository.findAll()).thenReturn(List.of());

        List<Pagamento> resultado = adapter.listarTodos();

        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findAll();
    }


    private Pagamento criarPagamento(Long id) {
        return new Pagamento(
                id,
                10L,
                new BigDecimal("99.99"),
                "PIX",
                1L,
                LocalDateTime.now()
        );
    }
}
