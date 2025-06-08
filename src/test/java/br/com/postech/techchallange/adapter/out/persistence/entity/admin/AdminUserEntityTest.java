package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class AdminUserEntityTest {

	@Test
	void testDefaultConstructor() {
		AdminUserEntity adminUser = new AdminUserEntity();
		assertNotNull(adminUser);
		assertNull(adminUser.getIdAdmin());
		assertNull(adminUser.getNome());
		assertNull(adminUser.getEmail());
		assertNull(adminUser.getSenhaHash());
		assertNull(adminUser.getAtivo());
		assertNull(adminUser.getDataCriacao());
	}

	@Test
	void testParameterizedConstructor() {
		Long id = 1L;
		String nome = "Admin User";
		String email = "admin@example.com";
		String senha = "password123";
		Boolean ativo = true;
		LocalDateTime dataCriacao = LocalDateTime.now();

		AdminUserEntity adminUser = new AdminUserEntity(id, nome, email, senha, ativo, dataCriacao);

		assertEquals(id, adminUser.getIdAdmin());
		assertEquals(nome, adminUser.getNome());
		assertEquals(email, adminUser.getEmail());
		assertEquals(senha, adminUser.getSenhaHash());
		assertEquals(ativo, adminUser.getAtivo());
		assertEquals(dataCriacao, adminUser.getDataCriacao());
	}

	@Test
	void testBuilder() {
		Long id = 1L;
		String nome = "Admin User";
		String email = "admin@example.com";
		String senha = "password123";
		Boolean ativo = true;
		LocalDateTime dataCriacao = LocalDateTime.now();

		AdminUserEntity adminUser = AdminUserEntity.builder().idAdmin(id).nome(nome).email(email).senhaHash(senha)
				.ativo(ativo).dataCriacao(dataCriacao).build();

		assertEquals(id, adminUser.getIdAdmin());
		assertEquals(nome, adminUser.getNome());
		assertEquals(email, adminUser.getEmail());
		assertEquals(senha, adminUser.getSenhaHash());
		assertEquals(ativo, adminUser.getAtivo());
		assertEquals(dataCriacao, adminUser.getDataCriacao());
	}

	@Test
	void testEntityAnnotations() {
		// Test if the class has the required JPA annotations
		assertTrue(AdminUserEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
		assertTrue(AdminUserEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

		// Test table name
		jakarta.persistence.Table tableAnnotation = AdminUserEntity.class
				.getAnnotation(jakarta.persistence.Table.class);
		assertEquals("admin_user", tableAnnotation.name());

		// Test ID field annotations
		try {
			java.lang.reflect.Field idField = AdminUserEntity.class.getDeclaredField("idAdmin");
			assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
			assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));
			assertTrue(idField.isAnnotationPresent(jakarta.persistence.Column.class));

			jakarta.persistence.GeneratedValue generatedValue = idField
					.getAnnotation(jakarta.persistence.GeneratedValue.class);
			assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());

			jakarta.persistence.Column column = idField.getAnnotation(jakarta.persistence.Column.class);
			assertEquals("id_admin", column.name());
		} catch (NoSuchFieldException e) {
			fail("Field idAdmin not found");
		}

		// Test other field annotations
		try {
			java.lang.reflect.Field nomeField = AdminUserEntity.class.getDeclaredField("nome");
			assertFalse(nomeField.isAnnotationPresent(jakarta.persistence.Column.class));

			java.lang.reflect.Field emailField = AdminUserEntity.class.getDeclaredField("email");
			assertFalse(emailField.isAnnotationPresent(jakarta.persistence.Column.class));

			java.lang.reflect.Field senhaHashField = AdminUserEntity.class.getDeclaredField("senhaHash");
			assertTrue(senhaHashField.isAnnotationPresent(jakarta.persistence.Column.class));
			jakarta.persistence.Column senhaHashColumn = senhaHashField.getAnnotation(jakarta.persistence.Column.class);
			assertEquals("senha_hash", senhaHashColumn.name());

			java.lang.reflect.Field ativoField = AdminUserEntity.class.getDeclaredField("ativo");
			assertFalse(ativoField.isAnnotationPresent(jakarta.persistence.Column.class));

			java.lang.reflect.Field dataCriacaoField = AdminUserEntity.class.getDeclaredField("dataCriacao");
			assertTrue(dataCriacaoField.isAnnotationPresent(jakarta.persistence.Column.class));
			jakarta.persistence.Column dataCriacaoColumn = dataCriacaoField
					.getAnnotation(jakarta.persistence.Column.class);
			assertEquals("data_criacao", dataCriacaoColumn.name());
		} catch (NoSuchFieldException e) {
			fail("One of the fields not found");
		}
	}
}
