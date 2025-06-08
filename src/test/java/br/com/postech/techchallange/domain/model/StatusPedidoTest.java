package br.com.postech.techchallange.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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