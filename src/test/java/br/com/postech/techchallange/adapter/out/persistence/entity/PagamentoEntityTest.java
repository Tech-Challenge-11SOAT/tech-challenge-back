package br.com.postech.techchallange.adapter.out.persistence.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import java.time.LocalDateTime;

class PagamentoEntityTest {

    @Test
    void testDefaultConstructor() {
        PagamentoEntity pagamento = new PagamentoEntity();
        assertNotNull(pagamento);
        assertNull(pagamento.getIdPagamento());
        assertNull(pagamento.getIdPedido());
        assertNull(pagamento.getValorTotal());
        assertNull(pagamento.getMetodoPagamento());
        assertNull(pagamento.getIdStatusPagamento());
        assertNull(pagamento.getDataPagamento());
    }

    @Test
    void testParameterizedConstructor() {
        Long idPagamento = 1L;
        Long idPedido = 2L;
        BigDecimal valorTotal = new BigDecimal("99.99");
        String metodoPagamento = "CREDITO";
        Long idStatusPagamento = 3L;
        LocalDateTime dataPagamento = LocalDateTime.now();

        PagamentoEntity pagamento = new PagamentoEntity(idPagamento, idPedido, valorTotal, metodoPagamento, idStatusPagamento, dataPagamento);

        assertEquals(idPagamento, pagamento.getIdPagamento());
        assertEquals(idPedido, pagamento.getIdPedido());
        assertEquals(valorTotal, pagamento.getValorTotal());
        assertEquals(metodoPagamento, pagamento.getMetodoPagamento());
        assertEquals(idStatusPagamento, pagamento.getIdStatusPagamento());
        assertEquals(dataPagamento, pagamento.getDataPagamento());
    }

    @Test
    void testBuilder() {
        Long idPagamento = 1L;
        Long idPedido = 2L;
        BigDecimal valorTotal = new BigDecimal("99.99");
        String metodoPagamento = "CREDITO";
        Long idStatusPagamento = 3L;
        LocalDateTime dataPagamento = LocalDateTime.now();

        PagamentoEntity pagamento = PagamentoEntity.builder()
                .idPagamento(idPagamento)
                .idPedido(idPedido)
                .valorTotal(valorTotal)
                .metodoPagamento(metodoPagamento)
                .idStatusPagamento(idStatusPagamento)
                .dataPagamento(dataPagamento)
                .build();

        assertEquals(idPagamento, pagamento.getIdPagamento());
        assertEquals(idPedido, pagamento.getIdPedido());
        assertEquals(valorTotal, pagamento.getValorTotal());
        assertEquals(metodoPagamento, pagamento.getMetodoPagamento());
        assertEquals(idStatusPagamento, pagamento.getIdStatusPagamento());
        assertEquals(dataPagamento, pagamento.getDataPagamento());
    }

    @Test
    void testEntityAnnotations() {
        // Test if the class has the required JPA annotations
        assertTrue(PagamentoEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
        assertTrue(PagamentoEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

        // Test table name
        jakarta.persistence.Table tableAnnotation = PagamentoEntity.class.getAnnotation(jakarta.persistence.Table.class);
        assertEquals("pagamento", tableAnnotation.name());

        // Test ID field annotations
        try {
            java.lang.reflect.Field idField = PagamentoEntity.class.getDeclaredField("idPagamento");
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));

            jakarta.persistence.GeneratedValue generatedValue = idField.getAnnotation(jakarta.persistence.GeneratedValue.class);
            assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());
        } catch (NoSuchFieldException e) {
            fail("Field idPagamento not found");
        }
    }
}
