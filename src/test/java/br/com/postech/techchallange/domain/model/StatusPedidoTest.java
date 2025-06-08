package br.com.postech.techchallange.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class StatusPedidoTest {

    @Test
    void testDefaultConstructor() {
        StatusPedido statusPedido = new StatusPedido();
        assertNotNull(statusPedido);
        assertNull(statusPedido.getIdStatusPedido());
        assertNull(statusPedido.getNomeStatus());
    }

    @Test
    void testParameterizedConstructor() {
        Long idStatusPedido = 1L;
        String nomeStatus = "PENDENTE";

        StatusPedido statusPedido = new StatusPedido(idStatusPedido, nomeStatus);

        assertEquals(idStatusPedido, statusPedido.getIdStatusPedido());
        assertEquals(nomeStatus, statusPedido.getNomeStatus());
    }

    @Test
    void testBuilder() {
        Long idStatusPedido = 1L;
        String nomeStatus = "PENDENTE";

        StatusPedido statusPedido = StatusPedido.builder()
                .idStatusPedido(idStatusPedido)
                .nomeStatus(nomeStatus)
                .build();

        assertEquals(idStatusPedido, statusPedido.getIdStatusPedido());
        assertEquals(nomeStatus, statusPedido.getNomeStatus());
    }
}
