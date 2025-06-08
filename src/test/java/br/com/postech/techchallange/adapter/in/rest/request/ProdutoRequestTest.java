package br.com.postech.techchallange.adapter.in.rest.request;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ProdutoRequestTest {

    @Test
    void testRecordConstructorAndGetters() {
        ProdutoRequest req = new ProdutoRequest("Nome", "Desc", new BigDecimal("10.0"), 2L, 3L);
        assertEquals("Nome", req.nome());
        assertEquals("Desc", req.descricao());
        assertEquals(new BigDecimal("10.0"), req.preco());
        assertEquals(2L, req.idImagemUrl());
        assertEquals(3L, req.idCategoria());
    }

    @Test
    void testEqualsAndHashCode() {
        ProdutoRequest req1 = new ProdutoRequest("Nome", "Desc", new BigDecimal("10.0"), 2L, 3L);
        ProdutoRequest req2 = new ProdutoRequest("Nome", "Desc", new BigDecimal("10.0"), 2L, 3L);
        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToString() {
        ProdutoRequest req = new ProdutoRequest("Nome", "Desc", new BigDecimal("10.0"), 2L, 3L);
        String str = req.toString();
        assertTrue(str.contains("Nome"));
        assertTrue(str.contains("Desc"));
    }

    @Test
    void testToDomain() {
        ProdutoRequest req = new ProdutoRequest("Nome", "Desc", new BigDecimal("10.0"), 2L, 3L);
        var domain = req.toDomain();
        assertEquals("Nome", domain.getNome());
        assertEquals("Desc", domain.getDescricao());
        assertEquals(new BigDecimal("10.0"), domain.getPreco());
        assertEquals(2L, domain.getIdImagemUrl());
        assertEquals(3L, domain.getIdCategoria());
    }
}
