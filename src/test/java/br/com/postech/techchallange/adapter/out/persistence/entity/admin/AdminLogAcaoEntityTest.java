package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class AdminLogAcaoEntityTest {

	@Test
	void testDefaultConstructor() {
		AdminLogAcaoEntity logAcao = new AdminLogAcaoEntity();
		assertNotNull(logAcao);
		assertNull(logAcao.getIdLog());
		assertNull(logAcao.getAdminUser());
		assertNull(logAcao.getAcao());
		assertNull(logAcao.getRecursoAfetado());
		assertNull(logAcao.getIdRecurso());
		assertNull(logAcao.getDataAcao());
	}

	@Test
	void testParameterizedConstructor() {
		Long id = 1L;
		AdminUserEntity adminUser = new AdminUserEntity();
		String acao = "CREATE";
		String recursoAfetado = "PRODUTO";
		Long idRecurso = 123L;
		LocalDateTime dataAcao = LocalDateTime.now();

		AdminLogAcaoEntity logAcao = new AdminLogAcaoEntity(id, adminUser, acao, recursoAfetado, idRecurso, dataAcao);

		assertEquals(id, logAcao.getIdLog());
		assertEquals(adminUser, logAcao.getAdminUser());
		assertEquals(acao, logAcao.getAcao());
		assertEquals(recursoAfetado, logAcao.getRecursoAfetado());
		assertEquals(idRecurso, logAcao.getIdRecurso());
		assertEquals(dataAcao, logAcao.getDataAcao());
	}

	@Test
	void testBuilder() {
		Long id = 1L;
		AdminUserEntity adminUser = new AdminUserEntity();
		String acao = "CREATE";
		String recursoAfetado = "PRODUTO";
		Long idRecurso = 123L;
		LocalDateTime dataAcao = LocalDateTime.now();

		AdminLogAcaoEntity logAcao = AdminLogAcaoEntity.builder().idLog(id).adminUser(adminUser).acao(acao)
				.recursoAfetado(recursoAfetado).idRecurso(idRecurso).dataAcao(dataAcao).build();

		assertEquals(id, logAcao.getIdLog());
		assertEquals(adminUser, logAcao.getAdminUser());
		assertEquals(acao, logAcao.getAcao());
		assertEquals(recursoAfetado, logAcao.getRecursoAfetado());
		assertEquals(idRecurso, logAcao.getIdRecurso());
		assertEquals(dataAcao, logAcao.getDataAcao());
	}

	@Test
	void testEntityAnnotations() {
		// Test if the class has the required JPA annotations
		assertTrue(AdminLogAcaoEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class));
		assertTrue(AdminLogAcaoEntity.class.isAnnotationPresent(jakarta.persistence.Table.class));

		// Test table name
		jakarta.persistence.Table tableAnnotation = AdminLogAcaoEntity.class
				.getAnnotation(jakarta.persistence.Table.class);
		assertEquals("admin_log_acao", tableAnnotation.name());

		// Test ID field annotations
		try {
			java.lang.reflect.Field idField = AdminLogAcaoEntity.class.getDeclaredField("idLog");
			assertTrue(idField.isAnnotationPresent(jakarta.persistence.Id.class));
			assertTrue(idField.isAnnotationPresent(jakarta.persistence.GeneratedValue.class));

			jakarta.persistence.GeneratedValue generatedValue = idField
					.getAnnotation(jakarta.persistence.GeneratedValue.class);
			assertEquals(jakarta.persistence.GenerationType.IDENTITY, generatedValue.strategy());
		} catch (NoSuchFieldException e) {
			fail("Field idLog not found");
		}

		// Test other field annotations
		try {
			java.lang.reflect.Field adminUserField = AdminLogAcaoEntity.class.getDeclaredField("adminUser");
			assertTrue(adminUserField.isAnnotationPresent(jakarta.persistence.ManyToOne.class));
			assertTrue(adminUserField.isAnnotationPresent(jakarta.persistence.JoinColumn.class));
			jakarta.persistence.JoinColumn joinColumn = adminUserField
					.getAnnotation(jakarta.persistence.JoinColumn.class);
			assertEquals("id_admin", joinColumn.name());

			java.lang.reflect.Field acaoField = AdminLogAcaoEntity.class.getDeclaredField("acao");
			assertFalse(acaoField.isAnnotationPresent(jakarta.persistence.Column.class));

			java.lang.reflect.Field recursoAfetadoField = AdminLogAcaoEntity.class.getDeclaredField("recursoAfetado");
			assertFalse(recursoAfetadoField.isAnnotationPresent(jakarta.persistence.Column.class));

			java.lang.reflect.Field idRecursoField = AdminLogAcaoEntity.class.getDeclaredField("idRecurso");
			assertFalse(idRecursoField.isAnnotationPresent(jakarta.persistence.Column.class));

			java.lang.reflect.Field dataAcaoField = AdminLogAcaoEntity.class.getDeclaredField("dataAcao");
			assertFalse(dataAcaoField.isAnnotationPresent(jakarta.persistence.Column.class));
		} catch (NoSuchFieldException e) {
			fail("One of the fields not found");
		}
	}
}
