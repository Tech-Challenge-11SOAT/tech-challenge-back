package br.com.postech.techchallange.domain.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class AdminLogAcaoTest {

    @Test
    void testDefaultConstructor() {
        AdminLogAcao adminLogAcao = new AdminLogAcao();
        assertNotNull(adminLogAcao);
        assertNull(adminLogAcao.getId());
        assertNull(adminLogAcao.getIdAdmin());
        assertNull(adminLogAcao.getAcao());
        assertNull(adminLogAcao.getRecursoAfetado());
        assertNull(adminLogAcao.getIdRecurso());
        assertNull(adminLogAcao.getDataAcao());
    }

    @Test
    void testParameterizedConstructor() {
        Long id = 1L;
        Long idAdmin = 2L;
        String acao = "CREATE";
        String recursoAfetado = "PRODUTO";
        Long idRecurso = 3L;
        LocalDateTime dataAcao = LocalDateTime.now();

        AdminLogAcao adminLogAcao = new AdminLogAcao(id, idAdmin, acao, recursoAfetado, idRecurso, dataAcao);

        assertEquals(id, adminLogAcao.getId());
        assertEquals(idAdmin, adminLogAcao.getIdAdmin());
        assertEquals(acao, adminLogAcao.getAcao());
        assertEquals(recursoAfetado, adminLogAcao.getRecursoAfetado());
        assertEquals(idRecurso, adminLogAcao.getIdRecurso());
        assertEquals(dataAcao, adminLogAcao.getDataAcao());
    }

    @Test
    void testBuilder() {
        Long id = 1L;
        Long idAdmin = 2L;
        String acao = "CREATE";
        String recursoAfetado = "PRODUTO";
        Long idRecurso = 3L;
        LocalDateTime dataAcao = LocalDateTime.now();

        AdminLogAcao adminLogAcao = AdminLogAcao.builder()
                .id(id)
                .idAdmin(idAdmin)
                .acao(acao)
                .recursoAfetado(recursoAfetado)
                .idRecurso(idRecurso)
                .dataAcao(dataAcao)
                .build();

        assertEquals(id, adminLogAcao.getId());
        assertEquals(idAdmin, adminLogAcao.getIdAdmin());
        assertEquals(acao, adminLogAcao.getAcao());
        assertEquals(recursoAfetado, adminLogAcao.getRecursoAfetado());
        assertEquals(idRecurso, adminLogAcao.getIdRecurso());
        assertEquals(dataAcao, adminLogAcao.getDataAcao());
    }
}
