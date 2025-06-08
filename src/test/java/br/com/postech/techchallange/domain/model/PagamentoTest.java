package br.com.postech.techchallange.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class PagamentoTest {

    @Test
    void testDefaultConstructor() {
        Pagamento pagamento = new Pagamento();
        assertNotNull(pagamento);
        assertNull(pagamento.getId());
        assertNull(pagamento.getIdPedido());
        assertNull(pagamento.getValorTotal());
        assertNull(pagamento.getMetodoPagamento());
        assertNull(pagamento.getIdStatusPagamento());
        assertNull(pagamento.getDataPagamento());
    }

    @Test
    void testParameterizedConstructor() {
        Long id = 1L;
        Long idPedido = 2L;
        BigDecimal valorTotal = new BigDecimal("99.99");
        String metodoPagamento = "CREDITO";
        Long idStatusPagamento = 3L;
        LocalDateTime dataPagamento = LocalDateTime.now();

        Pagamento pagamento = new Pagamento(id, idPedido, valorTotal, metodoPagamento, idStatusPagamento, dataPagamento);

        assertEquals(id, pagamento.getId());
        assertEquals(idPedido, pagamento.getIdPedido());
        assertEquals(valorTotal, pagamento.getValorTotal());
        assertEquals(metodoPagamento, pagamento.getMetodoPagamento());
        assertEquals(idStatusPagamento, pagamento.getIdStatusPagamento());
        assertEquals(dataPagamento, pagamento.getDataPagamento());
    }

    @Test
    void testBuilder() {
        Long id = 1L;
        Long idPedido = 2L;
        BigDecimal valorTotal = new BigDecimal("99.99");
        String metodoPagamento = "CREDITO";
        Long idStatusPagamento = 3L;
        LocalDateTime dataPagamento = LocalDateTime.now();

        Pagamento pagamento = Pagamento.builder()
                .id(id)
                .idPedido(idPedido)
                .valorTotal(valorTotal)
                .metodoPagamento(metodoPagamento)
                .idStatusPagamento(idStatusPagamento)
                .dataPagamento(dataPagamento)
                .build();

        assertEquals(id, pagamento.getId());
        assertEquals(idPedido, pagamento.getIdPedido());
        assertEquals(valorTotal, pagamento.getValorTotal());
        assertEquals(metodoPagamento, pagamento.getMetodoPagamento());
        assertEquals(idStatusPagamento, pagamento.getIdStatusPagamento());
        assertEquals(dataPagamento, pagamento.getDataPagamento());
    }
}
