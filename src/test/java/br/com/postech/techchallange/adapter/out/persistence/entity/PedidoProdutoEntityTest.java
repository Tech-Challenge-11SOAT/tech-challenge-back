package br.com.postech.techchallange.adapter.out.persistence.entity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class PedidoProdutoEntityTest {

    @Test
    void testDefaultConstructor() {
        PedidoProdutoEntity pedidoProduto = new PedidoProdutoEntity();
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

        PedidoProdutoEntity pedidoProduto = new PedidoProdutoEntity(id, idPedido, idProduto, quantidade, precoUnitario);

        assertEquals(id, pedidoProduto.getId());
        assertEquals(idPedido, pedidoProduto.getIdPedido());
        assertEquals(idProduto, pedidoProduto.getIdProduto());
        assertEquals(quantidade, pedidoProduto.getQuantidade());
        assertEquals(precoUnitario, pedidoProduto.getPrecoUnitario());
    }

    @Test
    void testBuilder() {
        Long id = 1L;
        Long idPedido = 2L;
        Long idProduto = 3L;
        Integer quantidade = 2;
        BigDecimal precoUnitario = new BigDecimal("99.99");

        PedidoProdutoEntity pedidoProduto = PedidoProdutoEntity.builder()
                .id(id)
                .idPedido(idPedido)
                .idProduto(idProduto)
                .quantidade(quantidade)
                .precoUnitario(precoUnitario)
                .build();

        assertEquals(id, pedidoProduto.getId());
        assertEquals(idPedido, pedidoProduto.getIdPedido());
        assertEquals(idProduto, pedidoProduto.getIdProduto());
        assertEquals(quantidade, pedidoProduto.getQuantidade());
        assertEquals(precoUnitario, pedidoProduto.getPrecoUnitario());
    }

    @Test
    void testEntityAnnotations() {
        // Test if the class has the required JPA annotations
        assertTrue(PedidoProdutoEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
        assertTrue(PedidoProdutoEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

        // Test table name
        jakarta.persistence.Table tableAnnotation = PedidoProdutoEntity.class.getAnnotation(jakarta.persistence.Table.class);
        assertEquals("pedido_produto", tableAnnotation.name());

        // Test ID field annotations
        try {
            java.lang.reflect.Field idField = PedidoProdutoEntity.class.getDeclaredField("id");
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Column.class));

            jakarta.persistence.GeneratedValue generatedValue = idField.getAnnotation(jakarta.persistence.GeneratedValue.class);
            assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());

            jakarta.persistence.Column column = idField.getAnnotation(jakarta.persistence.Column.class);
            assertEquals("id_pedido_produto", column.name());
        } catch (NoSuchFieldException e) {
            fail("Field id not found");
        }

        // Test other field annotations
        try {
            java.lang.reflect.Field idPedidoField = PedidoProdutoEntity.class.getDeclaredField("idPedido");
            assertTrue(idPedidoField.isAnnotationPresent(jakarta.persistence.Column.class));
            jakarta.persistence.Column idPedidoColumn = idPedidoField.getAnnotation(jakarta.persistence.Column.class);
            assertEquals("id_pedido", idPedidoColumn.name());

            java.lang.reflect.Field idProdutoField = PedidoProdutoEntity.class.getDeclaredField("idProduto");
            assertTrue(idProdutoField.isAnnotationPresent(jakarta.persistence.Column.class));
            jakarta.persistence.Column idProdutoColumn = idProdutoField.getAnnotation(jakarta.persistence.Column.class);
            assertEquals("id_produto", idProdutoColumn.name());

            java.lang.reflect.Field quantidadeField = PedidoProdutoEntity.class.getDeclaredField("quantidade");
            assertTrue(quantidadeField.isAnnotationPresent(jakarta.persistence.Column.class));
            jakarta.persistence.Column quantidadeColumn = quantidadeField.getAnnotation(jakarta.persistence.Column.class);
            assertEquals("quantidade", quantidadeColumn.name());

            java.lang.reflect.Field precoUnitarioField = PedidoProdutoEntity.class.getDeclaredField("precoUnitario");
            assertTrue(precoUnitarioField.isAnnotationPresent(jakarta.persistence.Column.class));
            jakarta.persistence.Column precoUnitarioColumn = precoUnitarioField.getAnnotation(jakarta.persistence.Column.class);
            assertEquals("preco_unitario", precoUnitarioColumn.name());
        } catch (NoSuchFieldException e) {
            fail("One of the fields not found");
        }
    }
}
