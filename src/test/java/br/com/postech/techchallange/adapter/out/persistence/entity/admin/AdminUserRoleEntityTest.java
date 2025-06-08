package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class AdminUserRoleEntityTest {

    @Test
    void testDefaultConstructor() {
        AdminUserRoleEntity adminUserRole = new AdminUserRoleEntity();
        assertNotNull(adminUserRole);
        assertNull(adminUserRole.getId());
        assertNull(adminUserRole.getAdminUser());
        assertNull(adminUserRole.getRole());
    }

    @Test
    void testParameterizedConstructor() {
        AdminUserRoleId id = new AdminUserRoleId(1L, 2L);
        AdminUserEntity adminUser = new AdminUserEntity();
        AdminRoleEntity role = new AdminRoleEntity();

        AdminUserRoleEntity adminUserRole = new AdminUserRoleEntity(id, adminUser, role);

        assertEquals(id, adminUserRole.getId());
        assertEquals(adminUser, adminUserRole.getAdminUser());
        assertEquals(role, adminUserRole.getRole());
    }

    @Test
    void testBuilder() {
        AdminUserRoleId id = new AdminUserRoleId(1L, 2L);
        AdminUserEntity adminUser = new AdminUserEntity();
        AdminRoleEntity role = new AdminRoleEntity();

        AdminUserRoleEntity adminUserRole = AdminUserRoleEntity.builder()
                .id(id)
                .adminUser(adminUser)
                .role(role)
                .build();

        assertEquals(id, adminUserRole.getId());
        assertEquals(adminUser, adminUserRole.getAdminUser());
        assertEquals(role, adminUserRole.getRole());
    }

    @Test
    void testEntityAnnotations() {
        // Test if the class has the required JPA annotations
        assertTrue(AdminUserRoleEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
        assertTrue(AdminUserRoleEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

        // Test table name
        jakarta.persistence.Table tableAnnotation = AdminUserRoleEntity.class
                .getAnnotation(jakarta.persistence.Table.class);
        assertEquals("admin_user_role", tableAnnotation.name());

        // Test ID field annotations
        try {
            java.lang.reflect.Field idField = AdminUserRoleEntity.class.getDeclaredField("id");
            assertTrue(idField.isAnnotationPresent(jakarta.persistence.EmbeddedId.class));
        } catch (NoSuchFieldException e) {
            fail("Field id not found");
        }

        // Test other field annotations
        try {
            java.lang.reflect.Field adminUserField = AdminUserRoleEntity.class.getDeclaredField("adminUser");
            assertTrue(adminUserField.isAnnotationPresent(jakarta.persistence.ManyToOne.class));
            assertTrue(adminUserField.isAnnotationPresent(jakarta.persistence.JoinColumn.class));
            assertTrue(adminUserField.isAnnotationPresent(jakarta.persistence.MapsId.class));
            jakarta.persistence.JoinColumn joinColumn = adminUserField
                    .getAnnotation(jakarta.persistence.JoinColumn.class);
            assertEquals("id_admin", joinColumn.name());
            jakarta.persistence.MapsId mapsId = adminUserField.getAnnotation(jakarta.persistence.MapsId.class);
            assertEquals("idAdmin", mapsId.value());

            java.lang.reflect.Field roleField = AdminUserRoleEntity.class.getDeclaredField("role");
            assertTrue(roleField.isAnnotationPresent(jakarta.persistence.ManyToOne.class));
            assertTrue(roleField.isAnnotationPresent(jakarta.persistence.JoinColumn.class));
            assertTrue(roleField.isAnnotationPresent(jakarta.persistence.MapsId.class));
            joinColumn = roleField.getAnnotation(jakarta.persistence.JoinColumn.class);
            assertEquals("id_role", joinColumn.name());
            mapsId = roleField.getAnnotation(jakarta.persistence.MapsId.class);
            assertEquals("idRole", mapsId.value());
        } catch (NoSuchFieldException e) {
            fail("One of the fields not found");
        }
    }
}
