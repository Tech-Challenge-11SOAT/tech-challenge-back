package br.com.postech.techchallange.adapter.out.persistence.entity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class ProdutoEntityTest {

    @Test
    void testDefaultConstructor() {
        ProdutoEntity produto = new ProdutoEntity();
        assertNotNull(produto);
        assertNull(produto.getIdProduto());
        assertNull(produto.getNome());
        assertNull(produto.getDescricao());
        assertNull(produto.getPreco());
        assertNull(produto.getIdImagemUrl());
        assertNull(produto.getIdCategoria());
    }

    @Test
    void testParameterizedConstructor() {
        Long idProduto = 1L;
        String nome = "Produto Teste";
        String descricao = "Descrição do produto teste";
        BigDecimal preco = new BigDecimal("99.99");
        Long idImagemUrl = 2L;
        Long idCategoria = 3L;

        ProdutoEntity produto = new ProdutoEntity(idProduto, nome, descricao, preco, idImagemUrl, idCategoria);

        assertEquals(idProduto, produto.getIdProduto());
        assertEquals(nome, produto.getNome());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(preco, produto.getPreco());
        assertEquals(idImagemUrl, produto.getIdImagemUrl());
        assertEquals(idCategoria, produto.getIdCategoria());
    }

    @Test
    void testBuilder() {
        Long idProduto = 1L;
        String nome = "Produto Teste";
        String descricao = "Descrição do produto teste";
        BigDecimal preco = new BigDecimal("99.99");
        Long idImagemUrl = 2L;
        Long idCategoria = 3L;

        ProdutoEntity produto = ProdutoEntity.builder()
                .idProduto(idProduto)
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .idImagemUrl(idImagemUrl)
                .idCategoria(idCategoria)
                .build();

        assertEquals(idProduto, produto.getIdProduto());
        assertEquals(nome, produto.getNome());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(preco, produto.getPreco());
        assertEquals(idImagemUrl, produto.getIdImagemUrl());
        assertEquals(idCategoria, produto.getIdCategoria());
    }

    @Test
    void testEntityAnnotations() {
        // Test if the class has the required JPA annotations
        assertTrue(ProdutoEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
        assertTrue(ProdutoEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

        // Test table name
        jakarta.persistence.Table tableAnnotation = ProdutoEntity.class.getAnnotation(jakarta.persistence.Table.class);
        assertEquals("produto", tableAnnotation.name());

        // Test ID field annotations
        try {
            java.lang.reflect.Field idField = ProdutoEntity.class.getDeclaredField("idProduto");
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));

            jakarta.persistence.GeneratedValue generatedValue = idField.getAnnotation(jakarta.persistence.GeneratedValue.class);
            assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());
        } catch (NoSuchFieldException e) {
            fail("Field idProduto not found");
        }
    }
}
