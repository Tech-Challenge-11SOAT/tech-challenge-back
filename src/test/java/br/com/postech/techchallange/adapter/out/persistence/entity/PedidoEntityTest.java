package br.com.postech.techchallange.adapter.out.persistence.entity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class PedidoEntityTest {

    @Test
    void testDefaultConstructor() {
        PedidoEntity pedido = new PedidoEntity();
        assertNotNull(pedido);
        assertNull(pedido.getIdPedido());
        assertNull(pedido.getIdCliente());
        assertNull(pedido.getDataPedido());
        assertNull(pedido.getIdStatusPedido());
        assertNull(pedido.getDataStatus());
        assertNull(pedido.getFilaPedido());
    }

    @Test
    void testParameterizedConstructor() {
        Long idPedido = 1L;
        Long idCliente = 2L;
        LocalDateTime dataPedido = LocalDateTime.now();
        Long idStatusPedido = 3L;
        LocalDateTime dataStatus = LocalDateTime.now();
        Integer filaPedido = 1;

        PedidoEntity pedido = new PedidoEntity(idPedido, idCliente, dataPedido, idStatusPedido, dataStatus, filaPedido);

        assertEquals(idPedido, pedido.getIdPedido());
        assertEquals(idCliente, pedido.getIdCliente());
        assertEquals(dataPedido, pedido.getDataPedido());
        assertEquals(idStatusPedido, pedido.getIdStatusPedido());
        assertEquals(dataStatus, pedido.getDataStatus());
        assertEquals(filaPedido, pedido.getFilaPedido());
    }

    @Test
    void testSettersAndGetters() {
        PedidoEntity pedido = new PedidoEntity();

        Long idPedido = 1L;
        Long idCliente = 2L;
        LocalDateTime dataPedido = LocalDateTime.now();
        Long idStatusPedido = 3L;
        LocalDateTime dataStatus = LocalDateTime.now();
        Integer filaPedido = 1;

        pedido.setIdPedido(idPedido);
        pedido.setIdCliente(idCliente);
        pedido.setDataPedido(dataPedido);
        pedido.setIdStatusPedido(idStatusPedido);
        pedido.setDataStatus(dataStatus);
        pedido.setFilaPedido(filaPedido);

        assertEquals(idPedido, pedido.getIdPedido());
        assertEquals(idCliente, pedido.getIdCliente());
        assertEquals(dataPedido, pedido.getDataPedido());
        assertEquals(idStatusPedido, pedido.getIdStatusPedido());
        assertEquals(dataStatus, pedido.getDataStatus());
        assertEquals(filaPedido, pedido.getFilaPedido());
    }

    @Test
    void testEntityAnnotations() {
        // Test if the class has the required JPA annotations
        assertTrue(PedidoEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
        assertTrue(PedidoEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

        // Test table name
        jakarta.persistence.Table tableAnnotation = PedidoEntity.class.getAnnotation(jakarta.persistence.Table.class);
        assertEquals("pedido", tableAnnotation.name());

        // Test ID field annotations
        try {
            java.lang.reflect.Field idField = PedidoEntity.class.getDeclaredField("idPedido");
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));

            jakarta.persistence.GeneratedValue generatedValue = idField.getAnnotation(jakarta.persistence.GeneratedValue.class);
            assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());
        } catch (NoSuchFieldException e) {
            fail("Field idPedido not found");
        }
    }
}
