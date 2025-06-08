package br.com.postech.techchallange.adapter.in.rest.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class WebhookPagamentoRequestTest {

    @Test
    void testGettersAndSetters() {
        WebhookPagamentoRequest req = new WebhookPagamentoRequest();
        req.setIdPedido(123L);
        req.setStatusPagamento("APROVADO");
        assertEquals(123L, req.getIdPedido());
        assertEquals("APROVADO", req.getStatusPagamento());
    }

    @Test
    void testEqualsAndHashCode() {
        WebhookPagamentoRequest req1 = new WebhookPagamentoRequest();
        req1.setIdPedido(1L);
        req1.setStatusPagamento("APROVADO");
        WebhookPagamentoRequest req2 = new WebhookPagamentoRequest();
        req2.setIdPedido(1L);
        req2.setStatusPagamento("APROVADO");
        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToString() {
        WebhookPagamentoRequest req = new WebhookPagamentoRequest();
        req.setIdPedido(1L);
        req.setStatusPagamento("APROVADO");
        String str = req.toString();
        assertTrue(str.contains("idPedido=1"));
        assertTrue(str.contains("statusPagamento=APROVADO"));
    }

    @Test
    void testNullFields() {
        WebhookPagamentoRequest req = new WebhookPagamentoRequest();
        assertNull(req.getIdPedido());
        assertNull(req.getStatusPagamento());
    }
}
