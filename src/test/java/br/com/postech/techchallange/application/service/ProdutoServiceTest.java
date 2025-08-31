package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepositoryPort repository;

    private ProdutoService service;

    @BeforeEach
    void setUp() {
        service = new ProdutoService(repository);
    }

    @Nested
    @DisplayName("Testes de Criação de Produto")
    class CriacaoProduto {
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

        @ParameterizedTest
        @MethodSource("produtosInvalidos")
        @DisplayName("Deve lançar exceção ao criar produto inválido")
        void deveLancarExcecaoAoCriarProdutoInvalido(Produto produtoInvalido, String mensagemEsperada) {
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> service.criarProduto(produtoInvalido));

            assertEquals(mensagemEsperada, exception.getMessage());
            verify(repository, never()).salvar(any());
        }

        private static Stream<Arguments> produtosInvalidos() {
            return Stream.of(
                    Arguments.of(criarProduto(1L, "", 1L), "Nome do produto não pode ser vazio"),
                    Arguments.of(criarProduto(1L, "Produto", null), "Categoria do produto é obrigatória"),
                    Arguments.of(criarProduto(1L, "Produto", 0L), "Categoria do produto inválida")
            );
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Produto")
    class AtualizacaoProduto {
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

        @ParameterizedTest
        @MethodSource("cenariosDadosInvalidos")
        @DisplayName("Deve validar regras de negócio na atualização")
        void deveValidarRegraDeNegocioNaAtualizacao(Long id, Produto produtoAtualizado, String mensagemEsperada) {
            Produto produtoExistente = criarProduto(1L, "Produto Original", 1L);
            when(repository.buscarPorId(id)).thenReturn(Optional.of(produtoExistente));

            BusinessException exception = assertThrows(BusinessException.class,
                    () -> service.atualizarProduto(id, produtoAtualizado));

            assertEquals(mensagemEsperada, exception.getMessage());
            verify(repository).buscarPorId(id);
            verify(repository, never()).salvar(any());
        }

        private static Stream<Arguments> cenariosDadosInvalidos() {
            return Stream.of(
                    Arguments.of(1L, criarProduto(1L, "", 1L), "Nome do produto não pode ser vazio"),
                    Arguments.of(1L, criarProduto(1L, "Produto", null), "Categoria do produto é obrigatória"),
                    Arguments.of(1L, criarProduto(2L, "Produto", 1L), "ID do produto não pode ser alterado")
            );
        }
    }

    private static Produto criarProduto(Long id, String nome, Long idCategoria) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        produto.setIdCategoria(idCategoria);
        produto.setPreco(new BigDecimal("10.00"));
        produto.setDescricao("Descrição teste");
        return produto;
    }
}
