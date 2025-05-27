package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.StatusPagamento;
import br.com.postech.techchallange.domain.port.out.StatusPagamentoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GerenciarStatusPagamentoServiceTest {

    private StatusPagamentoRepositoryPort repository;
    private GerenciarStatusPagamentoService service;

    @BeforeEach
    void setUp() {
        repository = mock(StatusPagamentoRepositoryPort.class);
        service = new GerenciarStatusPagamentoService(repository);
    }

    // Testes para buscar StatusPagamento por ID ---------------------------------------------------------------

    @Test
    @DisplayName("Deve retornar StatusPagamento ao buscar por ID existente")
    void deveRetornarStatusPagamentoPorId() {
        Long id = 1L;
        StatusPagamento status = StatusPagamento.builder()
                .idStatusPagamento(id)
                .nomeStatus(StatusPagamentoEnum.FINALIZADO)
                .build();
        when(repository.buscarPorId(id)).thenReturn(Optional.of(status));

        StatusPagamento resultado = service.buscarStatusPagamento(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdStatusPagamento());
        assertEquals("FINALIZADO", resultado.getNomeStatus().getStatus());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve retornar null ao buscar por ID inexistente")
    void deveRetornarNullSeNaoEncontrarPorId() {
        Long id = 2L;
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        StatusPagamento resultado = service.buscarStatusPagamento(id);

        assertNull(resultado);
        verify(repository).buscarPorId(id);
    }


    // Testes para buscar StatusPagamento por nome ---------------------------------------------------------------


    @Test
    @DisplayName("Deve retornar StatusPagamento ao buscar por nome válido")
    void deveRetornarStatusPagamentoPorStatus() {
        String statusNome = "PENDENTE";
        StatusPagamento status = StatusPagamento.builder()
                .idStatusPagamento(1L)
                .nomeStatus(StatusPagamentoEnum.PENDENTE)
                .build();
        when(repository.buscarStatusPagamentoPorStatus(statusNome)).thenReturn(Optional.of(status));

        StatusPagamento resultado = service.buscarStatusPagamentoPorStatus(statusNome);

        assertNotNull(resultado);
        assertEquals("PENDENTE", resultado.getNomeStatus().getStatus());
        verify(repository).buscarStatusPagamentoPorStatus(statusNome);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao buscar por status inexistente")
    void deveLancarExcecaoSeStatusNaoExistir() {
        String statusNome = "INEXISTENTE";
        when(repository.buscarStatusPagamentoPorStatus(statusNome)).thenReturn(Optional.empty());

        EntityNotFoundException excecao = assertThrows(EntityNotFoundException.class, () -> {
            service.buscarStatusPagamentoPorStatus(statusNome);
        });

        assertEquals("Status do pagamento não encontrado para o status: " + statusNome, excecao.getMessage());
        verify(repository).buscarStatusPagamentoPorStatus(statusNome);
    }


    // Testes para listar StatusPagamento ---------------------------------------------------------------


    @Test
    @DisplayName("Deve retornar lista de todos os StatusPagamento")
    void deveListarTodosStatusPagamento() {
        List<StatusPagamento> lista = List.of(
                StatusPagamento.builder()
                        .idStatusPagamento(1L)
                        .nomeStatus(StatusPagamentoEnum.ERRO)
                        .build(),
                StatusPagamento.builder()
                        .idStatusPagamento(2L)
                        .nomeStatus(StatusPagamentoEnum.PENDENTE)
                        .build()
        );
        when(repository.listarTodos()).thenReturn(lista);

        List<StatusPagamento> resultado = service.listarStatusPagamentos();

        assertEquals(2, resultado.size());
        assertEquals("ERRO", resultado.get(0).getNomeStatus().getStatus());
        assertEquals("PENDENTE", resultado.get(1).getNomeStatus().getStatus());
        verify(repository).listarTodos();
    }
}
