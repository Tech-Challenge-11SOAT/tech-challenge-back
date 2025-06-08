package br.com.postech.techchallange.adapter.out.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class StatusPedidoEntityTest {

    @Test
    void testDefaultConstructor() {
        StatusPedidoEntity statusPedido = new StatusPedidoEntity();
        assertNotNull(statusPedido);
        assertNull(statusPedido.getIdStatusPedido());
        assertNull(statusPedido.getNomeStatus());
    }

    @Test
    void testParameterizedConstructor() {
        Long idStatusPedido = 1L;
        String nomeStatus = "PENDENTE";

        StatusPedidoEntity statusPedido = new StatusPedidoEntity(idStatusPedido, nomeStatus);

        assertEquals(idStatusPedido, statusPedido.getIdStatusPedido());
        assertEquals(nomeStatus, statusPedido.getNomeStatus());
    }

    @Test
    void testBuilder() {
        Long idStatusPedido = 1L;
        String nomeStatus = "PENDENTE";

        StatusPedidoEntity statusPedido = StatusPedidoEntity.builder()
                .idStatusPedido(idStatusPedido)
                .nomeStatus(nomeStatus)
                .build();

        assertEquals(idStatusPedido, statusPedido.getIdStatusPedido());
        assertEquals(nomeStatus, statusPedido.getNomeStatus());
    }

    @Test
    void testEntityAnnotations() {
        // Test if the class has the required JPA annotations
        assertTrue(StatusPedidoEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
        assertTrue(StatusPedidoEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

        // Test table name
        jakarta.persistence.Table tableAnnotation = StatusPedidoEntity.class.getAnnotation(jakarta.persistence.Table.class);
        assertEquals("status_pedido", tableAnnotation.name());

        // Test ID field annotations
        try {
            java.lang.reflect.Field idField = StatusPedidoEntity.class.getDeclaredField("idStatusPedido");
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Column.class));

            jakarta.persistence.GeneratedValue generatedValue = idField.getAnnotation(jakarta.persistence.GeneratedValue.class);
            assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());

            jakarta.persistence.Column column = idField.getAnnotation(jakarta.persistence.Column.class);
            assertEquals("id_status_pedido", column.name());
        } catch (NoSuchFieldException e) {
            fail("Field idStatusPedido not found");
        }
    }
}
