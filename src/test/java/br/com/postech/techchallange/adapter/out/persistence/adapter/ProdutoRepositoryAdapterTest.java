package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.adapter.out.persistence.entity.ProdutoEntity;
import br.com.postech.techchallange.adapter.out.persistence.mapper.ProdutoMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.ProdutoJpaRepository;
import br.com.postech.techchallange.domain.model.Produto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoRepositoryAdapterTest {

    private ProdutoJpaRepository jpaRepository;
    private ProdutoRepositoryAdapter adapter;

    @BeforeEach
    void setUp(){
        jpaRepository = mock(ProdutoJpaRepository.class);
        adapter = new ProdutoRepositoryAdapter(jpaRepository);
    }


    // Testes para salvar informações ---------------------------------------------------------------


    @Test
    @DisplayName("Deve salvar as informações do produto no banco")
    void deveSalvarProdutoComSucesso(){
        // Arrange
        Produto produto = criarProduto(1L);
        ProdutoEntity entity = ProdutoMapper.toEntity(produto);

        when(jpaRepository.save(any(ProdutoEntity.class))).thenReturn(entity);

        // Act
        Produto resultado = adapter.salvar(produto);

        // Assert
        assertNotNull(resultado);
        assertEquals(produto.getId(),resultado.getId());
        verify(jpaRepository).save(any(ProdutoEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar produto quando ocorrer erro no repositório")
    void deveFalharAoSalvarProduto() {
        // Arrange
        Produto produto = criarProduto(1L);
        when(jpaRepository.save(any(ProdutoEntity.class)))
                .thenThrow(new RuntimeException("Erro ao salvar"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> adapter.salvar(produto));
        verify(jpaRepository).save(any(ProdutoEntity.class));
    }


    // Testes para buscar produto por ID ---------------------------------------------------------------


    @Test
    @DisplayName("Deve listar as informações dos produtos do banco")
    void deveBuscarProdutoPorIdComSucesso(){
        //Arrange
        ProdutoEntity entity = ProdutoMapper.toEntity(criarProduto(1L));

        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        // Act
        Optional<Produto> resultado = adapter.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Produto Teste",resultado.get().getNome());
        verify(jpaRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar produto inexistente")
    void deveFalharAoBuscarProdutoInexistente() {
        // Arrange
        when (jpaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException excecao = assertThrows(EntityNotFoundException.class, () -> {
            adapter.buscarPorId(1L);
        });

        // Assert
        assertEquals("Produto não encontrado.", excecao.getMessage());
    }


    // Testes para listar produtos do banco ---------------------------------------------------------------


    @Test
    @DisplayName("Deve listar as informações dos produtos do banco")
    void deveListarTodosOsProdutos(){
        // Arrange
        ProdutoEntity produto1 = ProdutoMapper.toEntity(criarProduto(1L));
        ProdutoEntity produto2 = ProdutoMapper.toEntity(criarProduto(2L));

        when(jpaRepository.findAll()).thenReturn(Arrays.asList(produto1,produto2));

        // Act
        List<Produto> resultado = adapter.buscarTodos();

        // Assert
        assertEquals(2,resultado.size());
        verify(jpaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver produtos cadastrados")
    void deveRetornarListaVaziaSeNaoHouverProdutos() {
        // Arrange
        when(jpaRepository.findAll()).thenReturn(List.of());

        // Act
        List<Produto> resultado = adapter.buscarTodos();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findAll();
    }


    // Testes para deletar produtos do banco ---------------------------------------------------------------


    @Test
    @DisplayName("Deve deletar o produto do banco")
    void deveDeletarProduto() {
        // Arrange
        ProdutoEntity entity = ProdutoMapper.toEntity(criarProduto(1L));

        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        // Act
        adapter.deletar(1L);

        //Assert
        verify(jpaRepository).delete(entity);
    }

    @Test
    @DisplayName("Deve falhar ao  tentar deletar um produto que não exista no banco")
    void deveFalharAoDeletarProdutoInexistente() {
        // Arrange
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> adapter.deletar(99L));
        verify(jpaRepository).findById(99L);
    }


    // Testes para buscar produtos por categoria no banco ---------------------------------------------------------------

    @Test
    @DisplayName("Deve buscar o produto pela categoria no banco")
    void deveBuscarPorCategoria() {
        // Arrange
        ProdutoEntity entity = ProdutoMapper.toEntity(criarProduto(1L));
        when(jpaRepository.findByIdCategoria(10L)).thenReturn(List.of(entity));

        // Act
        List<Produto> resultado = adapter.buscarPorCategoria(10L);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Produto Teste", resultado.get(0).getNome());
        verify(jpaRepository).findByIdCategoria(10L);
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar produtos por categoria inexistente")
    void deveRetornarListaVaziaQuandoCategoriaNaoExistir() {
        // Arrange
        when(jpaRepository.findByIdCategoria(999L)).thenReturn(List.of());

        // Act
        List<Produto> resultado = adapter.buscarPorCategoria(999L);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findByIdCategoria(999L);
    }




    private Produto criarProduto(Long id) {
        return new Produto(
                id,
                "Produto Teste",
                "Descricao teste",
                new BigDecimal("10.99"),
                100L,
                10L
        );
    }
}