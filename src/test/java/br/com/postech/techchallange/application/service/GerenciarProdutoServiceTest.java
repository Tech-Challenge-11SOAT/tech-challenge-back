package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GerenciarProdutoServiceTest {

    private ProdutoRepositoryPort repository;
    private GerenciarProdutoService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProdutoRepositoryPort.class);
        service = new GerenciarProdutoService(repository);
    }


    // Testes para salvar informações ---------------------------------------------------------------


    @Test
    @DisplayName("Deve criar um novo produto com sucesso")
    void deveCriarProduto() {
        // Arrange
        Produto produto = criarProduto(1L);
        when(repository.salvar(produto)).thenReturn(produto);

        // Act
        Produto resultado = service.criarProduto(produto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Refrigerante", resultado.getNome());
        verify(repository).salvar(produto);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar um produto e ocorrer falha")
    void deveLancarExcecaoAoCriarProduto() {
        // Arrange
        Produto produto = criarProduto(1L);
        when(repository.salvar(produto)).thenThrow(new RuntimeException("Erro ao salvar no banco"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.criarProduto(produto);
        });
        assertEquals("Erro ao salvar no banco", exception.getMessage());
        verify(repository).salvar(produto);
    }


    // Testes para atualizar um produto ---------------------------------------------------------------


    @Test
    @DisplayName("Deve atualizar produto existente com sucesso")
    void deveAtualizarProduto() {
        // Arrange
        Produto existente = criarProduto(1L);
        Produto atualizado = new Produto(1L, "Refrigerante Cola", "Bebida doce", new BigDecimal("6.50"), 1L, 1L);

        when(repository.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(repository.salvar(any(Produto.class))).thenReturn(atualizado);

        // Act
        Produto resultado = service.atualizarProduto(1L, atualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Refrigerante Cola", resultado.getNome());
        assertEquals("Bebida doce", resultado.getDescricao());
        verify(repository).salvar(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar produto inexistente")
    void deveLancarExcecaoSeProdutoNaoEncontradoParaAtualizar() {
        Produto atualizado = criarProduto(1L);
        when(repository.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.atualizarProduto(1L, atualizado));
    }


    // Testes para buscar produto por ID ---------------------------------------------------------------


    @Test
    @DisplayName("Deve buscar um produto existente pelo ID")
    void deveBuscarProdutoPorId() {
        Produto produto = criarProduto(1L);
        when(repository.buscarPorId(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = service.buscarProdutoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Refrigerante", resultado.get().getNome());
        verify(repository).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar produto por ID inexistente")
    void deveFalharAoBuscarProdutoPorIdInexistente(){
        // Arrange
        when(repository.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Produto> resultado = service.buscarProdutoPorId(999L);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(repository).buscarPorId(999L);
    }


    // Testes para listar os produtos ---------------------------------------------------------------


    @Test
    @DisplayName("Deve listar todos os produtos")
    void deveListarTodosProdutos() {
        // Arrange
        when(repository.buscarTodos()).thenReturn(List.of(criarProduto(1L)));

        // Act
        List<Produto> produtos = service.listarTodosProdutos();

        // Assert
        assertEquals(1, produtos.size());
        verify(repository).buscarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver produtos cadastrados")
    void deveRetornarListaVaziaSeNaoHouverProdutos(){
        // Arrange
        when(repository.buscarTodos()).thenReturn(List.of());

        // Act
        List<Produto> produtos = service.listarTodosProdutos();

        // Assert
        assertNotNull(produtos);
        assertTrue(produtos.isEmpty());
        verify(repository).buscarTodos();
    }


    // Testes para deletar um produto ---------------------------------------------------------------


    @Test
    @DisplayName("Deve deletar produto por ID")
    void deveDeletarProduto() {
        doNothing().when(repository).deletar(1L);

        assertDoesNotThrow(() -> service.deletarProduto(1L));
        verify(repository).deletar(1L);
    }

    @Test
    @DisplayName("Deve falhar ao tentar deletar produto por ID inexistente")
    void deveFalharAoTentarDeletarProdutoComIDInexistente() {
        // Arrange
        Long idInexistente = 999L;
        doThrow(new RuntimeException("Produto não encontrado"))
                .when(repository).deletar(idInexistente);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.deletarProduto(idInexistente);
        });
        assertEquals("Produto não encontrado", exception.getMessage());
        verify(repository).deletar(idInexistente);
    }


    // Testes para buscar um produto por categoria ---------------------------------------------------------------


    @Test
    @DisplayName("Deve buscar produtos por categoria com sucesso")
    void deveBuscarProdutosPorCategoria() {
        when(repository.buscarPorCategoria(10L)).thenReturn(List.of(criarProduto(1L)));

        List<Produto> resultado = service.buscarPorCategoria(10L);

        assertEquals(1, resultado.size());
        verify(repository).buscarPorCategoria(10L);
    }

    @Test
    @DisplayName("Deve lançar exceção se não encontrar produtos por categoria")
    void deveLancarExcecaoAoBuscarPorCategoriaSemResultado() {
        when(repository.buscarPorCategoria(10L)).thenReturn(List.of());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.buscarPorCategoria(10L));

        assertTrue(ex.getMessage().contains("Nenhum produto para categoria 10 foi encontrado"));
    }

    private Produto criarProduto(Long id) {
        return new Produto(
                id,
                "Refrigerante",
                "Bebida",
                new BigDecimal("5.50"),
                1L,
                1L);
    }

}