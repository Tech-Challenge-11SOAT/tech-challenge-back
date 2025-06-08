package br.com.postech.techchallange.adapter.in.rest.request;

import br.com.postech.techchallange.domain.enums.StatusPedidoEnum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AtualizarPedidoRequestTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        AtualizarPedidoRequest req = new AtualizarPedidoRequest(1L, StatusPedidoEnum.RECEBIDO_NAO_PAGO);
        assertEquals(1L, req.getId());
        assertEquals(StatusPedidoEnum.RECEBIDO_NAO_PAGO, req.getStatus());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        AtualizarPedidoRequest req = new AtualizarPedidoRequest();
        req.setId(2L);
        req.setStatus(StatusPedidoEnum.EM_ANDAMENTO);
        assertEquals(2L, req.getId());
        assertEquals(StatusPedidoEnum.EM_ANDAMENTO, req.getStatus());
    }

    @Test
    void testBuilder() {
        AtualizarPedidoRequest req = AtualizarPedidoRequest.builder()
                .id(3L)
                .status(StatusPedidoEnum.FINALIZADO)
                .build();
        assertEquals(3L, req.getId());
        assertEquals(StatusPedidoEnum.FINALIZADO, req.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        AtualizarPedidoRequest req1 = new AtualizarPedidoRequest(1L, StatusPedidoEnum.RECEBIDO_NAO_PAGO);
        AtualizarPedidoRequest req2 = new AtualizarPedidoRequest(1L, StatusPedidoEnum.RECEBIDO_NAO_PAGO);
        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToString() {
        AtualizarPedidoRequest req = new AtualizarPedidoRequest(1L, StatusPedidoEnum.RECEBIDO_NAO_PAGO);
        String str = req.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("RECEBIDO_NAO_PAGO"));
    }

    @Test
    void testNullFields() {
        AtualizarPedidoRequest req = new AtualizarPedidoRequest();
        assertNull(req.getId());
        assertNull(req.getStatus());
    }
}
