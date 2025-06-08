package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class AdminRoleEntityTest {

	@Test
	void testDefaultConstructor() {
		AdminRoleEntity adminRole = new AdminRoleEntity();
		assertNotNull(adminRole);
		assertNull(adminRole.getId());
		assertNull(adminRole.getNome());
		assertNull(adminRole.getDescricao());
	}

	@Test
	void testParameterizedConstructor() {
		Long id = 1L;
		String nome = "ADMIN";
		String descricao = "Administrator role";

		AdminRoleEntity adminRole = new AdminRoleEntity(id, nome, descricao);

		assertEquals(id, adminRole.getId());
		assertEquals(nome, adminRole.getNome());
		assertEquals(descricao, adminRole.getDescricao());
	}

	@Test
	void testBuilder() {
		Long id = 1L;
		String nome = "ADMIN";
		String descricao = "Administrator role";

		AdminRoleEntity adminRole = AdminRoleEntity.builder().id(id).nome(nome).descricao(descricao).build();

		assertEquals(id, adminRole.getId());
		assertEquals(nome, adminRole.getNome());
		assertEquals(descricao, adminRole.getDescricao());
	}

	@Test
	void testEntityAnnotations() {
		// Test if the class has the required JPA annotations
		assertTrue(AdminRoleEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
		assertTrue(AdminRoleEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

		// Test table name
		jakarta.persistence.Table tableAnnotation = AdminRoleEntity.class
				.getAnnotation(jakarta.persistence.Table.class);
		assertEquals("admin_role", tableAnnotation.name());

		// Test ID field annotations
		try {
			java.lang.reflect.Field idField = AdminRoleEntity.class.getDeclaredField("id");
			assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
			assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));
			assertTrue(idField.isAnnotationPresent(jakarta.persistence.Column.class));

			jakarta.persistence.GeneratedValue generatedValue = idField
					.getAnnotation(jakarta.persistence.GeneratedValue.class);
			assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());

			jakarta.persistence.Column column = idField.getAnnotation(jakarta.persistence.Column.class);
			assertEquals("id", column.name());
		} catch (NoSuchFieldException e) {
			fail("Field id not found");
		}

		// Test other field annotations
		try {
			java.lang.reflect.Field nomeField = AdminRoleEntity.class.getDeclaredField("nome");
			assertFalse(nomeField.isAnnotationPresent(jakarta.persistence.Column.class));

			java.lang.reflect.Field descricaoField = AdminRoleEntity.class.getDeclaredField("descricao");
			assertFalse(descricaoField.isAnnotationPresent(jakarta.persistence.Column.class));
		} catch (NoSuchFieldException e) {
			fail("One of the fields not found");
		}
	}
}
