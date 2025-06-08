package br.com.postech.techchallange.domain.model;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class ProdutoTest {

    @Test
    void testDefaultConstructor() {
        Produto produto = new Produto();
        assertNotNull(produto);
        assertNull(produto.getId());
        assertNull(produto.getNome());
        assertNull(produto.getDescricao());
        assertNull(produto.getPreco());
        assertNull(produto.getIdImagemUrl());
        assertNull(produto.getIdCategoria());
    }

    @Test
    void testParameterizedConstructor() {
        Long id = 1L;
        String nome = "Teste Produto";
        String descricao = "Descrição do produto";
        BigDecimal preco = new BigDecimal("99.99");
        Long idImagemUrl = 2L;
        Long idCategoria = 3L;

        Produto produto = new Produto(id, nome, descricao, preco, idImagemUrl, idCategoria);

        assertEquals(id, produto.getId());
        assertEquals(nome, produto.getNome());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(preco, produto.getPreco());
        assertEquals(idImagemUrl, produto.getIdImagemUrl());
        assertEquals(idCategoria, produto.getIdCategoria());
    }

    @Test
    void testBuilder() {
        Long id = 1L;
        String nome = "Teste Produto";
        String descricao = "Descrição do produto";
        BigDecimal preco = new BigDecimal("99.99");
        Long idImagemUrl = 2L;
        Long idCategoria = 3L;

        Produto produto = Produto.builder()
                .id(id)
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .idImagemUrl(idImagemUrl)
                .idCategoria(idCategoria)
                .build();

        assertEquals(id, produto.getId());
        assertEquals(nome, produto.getNome());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(preco, produto.getPreco());
        assertEquals(idImagemUrl, produto.getIdImagemUrl());
        assertEquals(idCategoria, produto.getIdCategoria());
    }

    @Test
    void testSettersAndGetters() {
        Produto produto = new Produto();

        Long id = 1L;
        String nome = "Teste Produto";
        String descricao = "Descrição do produto";
        BigDecimal preco = new BigDecimal("99.99");
        Long idImagemUrl = 2L;
        Long idCategoria = 3L;

        produto.setId(id);
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setIdImagemUrl(idImagemUrl);
        produto.setIdCategoria(idCategoria);

        assertEquals(id, produto.getId());
        assertEquals(nome, produto.getNome());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(preco, produto.getPreco());
        assertEquals(idImagemUrl, produto.getIdImagemUrl());
        assertEquals(idCategoria, produto.getIdCategoria());
    }
}
