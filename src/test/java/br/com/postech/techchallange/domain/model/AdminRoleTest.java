package br.com.postech.techchallange.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class AdminRoleTest {

    @Test
    void testDefaultConstructor() {
        AdminRole adminRole = new AdminRole();
        assertNotNull(adminRole);
        assertNull(adminRole.getId());
        assertNull(adminRole.getNome());
        assertNull(adminRole.getDescricao());
    }

    @Test
    void testParameterizedConstructor() {
        Long id = 1L;
        String nome = "ADMIN";
        String descricao = "Administrador do sistema";

        AdminRole adminRole = new AdminRole(id, nome, descricao);

        assertEquals(id, adminRole.getId());
        assertEquals(nome, adminRole.getNome());
        assertEquals(descricao, adminRole.getDescricao());
    }

    @Test
    void testBuilder() {
        Long id = 1L;
        String nome = "ADMIN";
        String descricao = "Administrador do sistema";

        AdminRole adminRole = AdminRole.builder()
                .id(id)
                .nome(nome)
                .descricao(descricao)
                .build();

        assertEquals(id, adminRole.getId());
        assertEquals(nome, adminRole.getNome());
        assertEquals(descricao, adminRole.getDescricao());
    }
}
