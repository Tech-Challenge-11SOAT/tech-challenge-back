package br.com.postech.techchallange.adapter.out.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class StatusPagamentoEntityTest {

    @Test
    void testDefaultConstructor() {
        StatusPagamentoEntity statusPagamento = new StatusPagamentoEntity();
        assertNotNull(statusPagamento);
        assertNull(statusPagamento.getIdStatusPagamento());
        assertNull(statusPagamento.getNomeStatus());
    }

    @Test
    void testParameterizedConstructor() {
        Long idStatusPagamento = 1L;
        String nomeStatus = "PENDENTE";

        StatusPagamentoEntity statusPagamento = new StatusPagamentoEntity(idStatusPagamento, nomeStatus);

        assertEquals(idStatusPagamento, statusPagamento.getIdStatusPagamento());
        assertEquals(nomeStatus, statusPagamento.getNomeStatus());
    }

    @Test
    void testBuilder() {
        Long idStatusPagamento = 1L;
        String nomeStatus = "PENDENTE";

        StatusPagamentoEntity statusPagamento = StatusPagamentoEntity.builder()
                .idStatusPagamento(idStatusPagamento)
                .nomeStatus(nomeStatus)
                .build();

        assertEquals(idStatusPagamento, statusPagamento.getIdStatusPagamento());
        assertEquals(nomeStatus, statusPagamento.getNomeStatus());
    }

    @Test
    void testEntityAnnotations() {
        // Test if the class has the required JPA annotations
        assertTrue(StatusPagamentoEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
        assertTrue(StatusPagamentoEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

        // Test table name
        jakarta.persistence.Table tableAnnotation = StatusPagamentoEntity.class.getAnnotation(jakarta.persistence.Table.class);
        assertEquals("status_pagamento", tableAnnotation.name());

        // Test ID field annotations
        try {
            java.lang.reflect.Field idField = StatusPagamentoEntity.class.getDeclaredField("idStatusPagamento");
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));

            jakarta.persistence.GeneratedValue generatedValue = idField.getAnnotation(jakarta.persistence.GeneratedValue.class);
            assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());
        } catch (NoSuchFieldException e) {
            fail("Field idStatusPagamento not found");
        }

        // Test nomeStatus field annotations
        try {
            java.lang.reflect.Field nomeStatusField = StatusPagamentoEntity.class.getDeclaredField("nomeStatus");
            assertTrue(nomeStatusField.isAnnotationPresent(jakarta.persistence.Column.class));

            jakarta.persistence.Column column = nomeStatusField.getAnnotation(jakarta.persistence.Column.class);
            assertEquals("nome_status", column.name());
            assertFalse(column.nullable());
        } catch (NoSuchFieldException e) {
            fail("Field nomeStatus not found");
        }
    }
}
