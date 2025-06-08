package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class AdminUserRoleIdTest {

    @Test
    void testDefaultConstructor() {
        AdminUserRoleId id = new AdminUserRoleId();
        assertNotNull(id);
        assertNull(id.getIdAdmin());
        assertNull(id.getIdRole());
    }

    @Test
    void testParameterizedConstructor() {
        Long idAdmin = 1L;
        Long idRole = 2L;

        AdminUserRoleId id = new AdminUserRoleId(idAdmin, idRole);

        assertEquals(idAdmin, id.getIdAdmin());
        assertEquals(idRole, id.getIdRole());
    }

    @Test
    void testEqualsAndHashCode() {
        AdminUserRoleId id1 = new AdminUserRoleId(1L, 2L);
        AdminUserRoleId id2 = new AdminUserRoleId(1L, 2L);
        AdminUserRoleId id3 = new AdminUserRoleId(2L, 1L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testEmbeddableAnnotations() {
        // Test if the class has the required JPA annotations
        assertTrue(AdminUserRoleId.class.isAnnotationPresent(jakarta.persistence.Embeddable.class));

        // Test field annotations
        try {
            java.lang.reflect.Field idAdminField = AdminUserRoleId.class.getDeclaredField("idAdmin");
            assertTrue(idAdminField.isAnnotationPresent(jakarta.persistence.Column.class));
            jakarta.persistence.Column column = idAdminField.getAnnotation(jakarta.persistence.Column.class);
            assertEquals("id_admin", column.name());

            java.lang.reflect.Field idRoleField = AdminUserRoleId.class.getDeclaredField("idRole");
            assertTrue(idRoleField.isAnnotationPresent(jakarta.persistence.Column.class));
            column = idRoleField.getAnnotation(jakarta.persistence.Column.class);
            assertEquals("id_role", column.name());
        } catch (NoSuchFieldException e) {
            fail("One of the fields not found");
        }
    }
}
