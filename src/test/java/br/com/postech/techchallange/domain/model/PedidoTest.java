package br.com.postech.techchallange.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class PedidoTest {

    @Test
    void testDefaultConstructor() {
        Pedido pedido = new Pedido();
        assertNotNull(pedido);
        assertNull(pedido.getId());
        assertNull(pedido.getIdCliente());
        assertNull(pedido.getDataPedido());
        assertNull(pedido.getIdStatusPedido());
        assertNull(pedido.getDataStatus());
        assertNull(pedido.getFilaPedido());
    }

    @Test
    void testParameterizedConstructor() {
        Long id = 1L;
        Long idCliente = 2L;
        LocalDateTime dataPedido = LocalDateTime.now();
        Long idStatusPedido = 3L;
        LocalDateTime dataStatus = LocalDateTime.now();
        Integer filaPedido = 1;

        Pedido pedido = new Pedido(id, idCliente, dataPedido, idStatusPedido, dataStatus, filaPedido);

        assertEquals(id, pedido.getId());
        assertEquals(idCliente, pedido.getIdCliente());
        assertEquals(dataPedido, pedido.getDataPedido());
        assertEquals(idStatusPedido, pedido.getIdStatusPedido());
        assertEquals(dataStatus, pedido.getDataStatus());
        assertEquals(filaPedido, pedido.getFilaPedido());
    }

    @Test
    void testSettersAndGetters() {
        Pedido pedido = new Pedido();

        Long id = 1L;
        Long idCliente = 2L;
        LocalDateTime dataPedido = LocalDateTime.now();
        Long idStatusPedido = 3L;
        LocalDateTime dataStatus = LocalDateTime.now();
        Integer filaPedido = 1;

        pedido.setId(id);
        pedido.setIdCliente(idCliente);
        pedido.setDataPedido(dataPedido);
        pedido.setIdStatusPedido(idStatusPedido);
        pedido.setDataStatus(dataStatus);
        pedido.setFilaPedido(filaPedido);

        assertEquals(id, pedido.getId());
        assertEquals(idCliente, pedido.getIdCliente());
        assertEquals(dataPedido, pedido.getDataPedido());
        assertEquals(idStatusPedido, pedido.getIdStatusPedido());
        assertEquals(dataStatus, pedido.getDataStatus());
        assertEquals(filaPedido, pedido.getFilaPedido());
    }

    @Test
    void testToString() {
        Long id = 1L;
        Long idCliente = 2L;
        LocalDateTime dataPedido = LocalDateTime.now();
        Long idStatusPedido = 3L;
        LocalDateTime dataStatus = LocalDateTime.now();
        Integer filaPedido = 1;

        Pedido pedido = new Pedido(id, idCliente, dataPedido, idStatusPedido, dataStatus, filaPedido);

        String expectedToString = String.format("Pedido [id=%d, idCliente=%d, dataPedido=%s, idStatusPedido=%d, dataStatus=%s, filaPedido=%d]",
                id, idCliente, dataPedido, idStatusPedido, dataStatus, filaPedido);

        assertEquals(expectedToString, pedido.toString());
    }
}
