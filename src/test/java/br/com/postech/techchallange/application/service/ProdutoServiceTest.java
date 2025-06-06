package br.com.postech.techchallange.application.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepositoryPort repository;

    private ProdutoService service;

    @BeforeEach
    void setUp() {
        service = new ProdutoService(repository);
    }

    // Testes para criar produto
    @Test
    @DisplayName("Deve criar produto com sucesso")
    void deveCriarProdutoComSucesso() {
        Produto produto = criarProduto(1L, "Produto Test", 1L);
        when(repository.salvar(produto)).thenReturn(produto);

        Produto resultado = service.criarProduto(produto);

        assertNotNull(resultado);
        assertEquals(produto.getId(), resultado.getId());
        assertEquals(produto.getNome(), resultado.getNome());
        verify(repository).salvar(produto);
    }

    // Testes para atualizar produto
    @Test
    @DisplayName("Deve atualizar produto existente com sucesso")
    void deveAtualizarProdutoComSucesso() {
        Long id = 1L;
        Produto produtoExistente = criarProduto(id, "Produto Antigo", 1L);
        Produto produtoAtualizado = criarProduto(id, "Produto Novo", 2L);

        when(repository.buscarPorId(id)).thenReturn(Optional.of(produtoExistente));
        when(repository.salvar(any(Produto.class))).thenReturn(produtoAtualizado);

        Produto resultado = service.atualizarProduto(id, produtoAtualizado);

        assertNotNull(resultado);
        assertEquals("Produto Novo", resultado.getNome());
        assertEquals(2L, resultado.getIdCategoria());
        verify(repository).buscarPorId(id);
        verify(repository).salvar(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar produto inexistente")
    void deveLancarExcecaoAoAtualizarProdutoInexistente() {
        Long id = 1L;
        Produto produtoAtualizado = criarProduto(id, "Produto Novo", 1L);

        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.atualizarProduto(id, produtoAtualizado));

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(repository).buscarPorId(id);
        verify(repository, never()).salvar(any());
    }

    // Testes para buscar produto por ID
    @Test
    @DisplayName("Deve retornar produto quando buscar por ID existente")
    void deveRetornarProdutoPorId() {
        Long id = 1L;
        Produto produto = criarProduto(id, "Produto Test", 1L);

        when(repository.buscarPorId(id)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = service.buscarProdutoPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        assertEquals("Produto Test", resultado.get().getNome());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando buscar por ID inexistente")
    void deveRetornarOptionalVazioQuandoProdutoNaoExiste() {
        Long id = 1L;
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        Optional<Produto> resultado = service.buscarProdutoPorId(id);

        assertFalse(resultado.isPresent());
        verify(repository).buscarPorId(id);
    }

    // Testes para listar todos os produtos
    @Test
    @DisplayName("Deve retornar lista com todos os produtos")
    void deveListarTodosProdutos() {
        List<Produto> produtos = Arrays.asList(
                criarProduto(1L, "Produto 1", 1L),
                criarProduto(2L, "Produto 2", 1L)
        );

        when(repository.buscarTodos()).thenReturn(produtos);

        List<Produto> resultado = service.listarTodosProdutos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(repository).buscarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver produtos cadastrados")
    void deveRetornarListaVaziaQuandoNaoHouverProdutos() {
        when(repository.buscarTodos()).thenReturn(Collections.emptyList());

        List<Produto> resultado = service.listarTodosProdutos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository).buscarTodos();
    }

    // Testes para deletar produto
    @Test
    @DisplayName("Deve deletar produto com sucesso")
    void deveDeletarProdutoComSucesso() {
        Long id = 1L;
        doNothing().when(repository).deletar(id);

        assertDoesNotThrow(() -> service.deletarProduto(id));
        verify(repository).deletar(id);
    }

    // Testes para buscar por categoria
    @Test
    @DisplayName("Deve retornar lista de produtos quando categoria existir")
    void deveRetornarProdutosDaCategoria() {
        Long idCategoria = 1L;
        List<Produto> produtos = Arrays.asList(
                criarProduto(1L, "Produto 1", idCategoria),
                criarProduto(2L, "Produto 2", idCategoria)
        );

        when(repository.buscarPorCategoria(idCategoria)).thenReturn(produtos);

        List<Produto> resultado = service.buscarPorCategoria(idCategoria);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(idCategoria, resultado.get(0).getIdCategoria());
        assertEquals(idCategoria, resultado.get(1).getIdCategoria());
        verify(repository).buscarPorCategoria(idCategoria);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhum produto for encontrado para a categoria")
    void deveLancarExcecaoQuandoCategoriaNaoTemProdutos() {
        Long idCategoria = 1L;
        when(repository.buscarPorCategoria(idCategoria)).thenReturn(Collections.emptyList());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.buscarPorCategoria(idCategoria));

        assertEquals(String.format("Nenhum produto para categoria %s foi encontrado", idCategoria),
                exception.getMessage());
        verify(repository).buscarPorCategoria(idCategoria);
    }

    @Test
    @DisplayName("Deve lançar exceção quando resultado da busca por categoria for null")
    void deveLancarExcecaoQuandoResultadoDaBuscaForNull() {
        Long idCategoria = 1L;
        when(repository.buscarPorCategoria(idCategoria)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.buscarPorCategoria(idCategoria));

        assertEquals(String.format("Nenhum produto para categoria %s foi encontrado", idCategoria),
                exception.getMessage());
        verify(repository).buscarPorCategoria(idCategoria);
    }

    private Produto criarProduto(Long id, String nome, Long idCategoria) {
        return Produto.builder()
                .id(id)
                .nome(nome)
                .descricao("Descrição teste")
                .preco(new BigDecimal("10.00"))
                .idCategoria(idCategoria)
                .build();
    }
}
