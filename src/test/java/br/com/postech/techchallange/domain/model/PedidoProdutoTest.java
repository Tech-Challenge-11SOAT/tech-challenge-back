package br.com.postech.techchallange.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class PedidoProdutoTest {

    @Test
    void testDefaultConstructor() {
        PedidoProduto pedidoProduto = new PedidoProduto();
        assertNotNull(pedidoProduto);
        assertNull(pedidoProduto.getId());
        assertNull(pedidoProduto.getIdPedido());
        assertNull(pedidoProduto.getIdProduto());
        assertNull(pedidoProduto.getQuantidade());
        assertNull(pedidoProduto.getPrecoUnitario());
    }

    @Test
    void testParameterizedConstructor() {
        Long id = 1L;
        Long idPedido = 2L;
        Long idProduto = 3L;
        Integer quantidade = 2;
        BigDecimal precoUnitario = new BigDecimal("99.99");

        PedidoProduto pedidoProduto = new PedidoProduto(id, idPedido, idProduto, quantidade, precoUnitario);

        assertEquals(id, pedidoProduto.getId());
        assertEquals(idPedido, pedidoProduto.getIdPedido());
        assertEquals(idProduto, pedidoProduto.getIdProduto());
        assertEquals(quantidade, pedidoProduto.getQuantidade());
        assertEquals(precoUnitario, pedidoProduto.getPrecoUnitario());
    }

    @Test
    void testSettersAndGetters() {
        PedidoProduto pedidoProduto = new PedidoProduto();

        Long id = 1L;
        Long idPedido = 2L;
        Long idProduto = 3L;
        Integer quantidade = 2;
        BigDecimal precoUnitario = new BigDecimal("99.99");

        pedidoProduto.setId(id);
        pedidoProduto.setIdPedido(idPedido);
        pedidoProduto.setIdProduto(idProduto);
        pedidoProduto.setQuantidade(quantidade);
        pedidoProduto.setPrecoUnitario(precoUnitario);

        assertEquals(id, pedidoProduto.getId());
        assertEquals(idPedido, pedidoProduto.getIdPedido());
        assertEquals(idProduto, pedidoProduto.getIdProduto());
        assertEquals(quantidade, pedidoProduto.getQuantidade());
        assertEquals(precoUnitario, pedidoProduto.getPrecoUnitario());
    }
}
