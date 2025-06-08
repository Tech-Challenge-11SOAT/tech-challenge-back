package br.com.postech.techchallange.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class AdminUserTest {

	@Test
	void testDefaultConstructor() {
		AdminUser adminUser = new AdminUser();
		assertNotNull(adminUser);
		assertNull(adminUser.getId());
		assertNull(adminUser.getNome());
		assertNull(adminUser.getEmail());
		assertNull(adminUser.getSenhaHash());
		assertNull(adminUser.getAtivo());
		assertNull(adminUser.getDataCriacao());
		assertNull(adminUser.getRoles());
	}

	@Test
	void testParameterizedConstructor() {
		Long id = 1L;
		String nome = "Admin Test";
		String email = "admin@test.com";
		String senhaHash = "hashedPassword123";
		Boolean ativo = true;
		LocalDateTime dataCriacao = LocalDateTime.now();
		List<AdminRole> roles = Arrays.asList(new AdminRole(1L, "ADMIN", "Administrador do sistema."));

		AdminUser adminUser = new AdminUser(id, nome, email, senhaHash, ativo, dataCriacao, roles);

		assertEquals(id, adminUser.getId());
		assertEquals(nome, adminUser.getNome());
		assertEquals(email, adminUser.getEmail());
		assertEquals(senhaHash, adminUser.getSenhaHash());
		assertEquals(ativo, adminUser.getAtivo());
		assertEquals(dataCriacao, adminUser.getDataCriacao());
		assertEquals(roles, adminUser.getRoles());
	}

	@Test
	void testBuilder() {
		Long id = 1L;
		String nome = "Admin Test";
		String email = "admin@test.com";
		String senhaHash = "hashedPassword123";
		Boolean ativo = true;
		LocalDateTime dataCriacao = LocalDateTime.now();
		List<AdminRole> roles = Arrays.asList(new AdminRole(1L, "ADMIN", "Administrador do sistema."));

		AdminUser adminUser = AdminUser.builder().id(id).nome(nome).email(email).senhaHash(senhaHash).ativo(ativo)
				.dataCriacao(dataCriacao).roles(roles).build();

		assertEquals(id, adminUser.getId());
		assertEquals(nome, adminUser.getNome());
		assertEquals(email, adminUser.getEmail());
		assertEquals(senhaHash, adminUser.getSenhaHash());
		assertEquals(ativo, adminUser.getAtivo());
		assertEquals(dataCriacao, adminUser.getDataCriacao());
		assertEquals(roles, adminUser.getRoles());
	}

	@Test
	void testSemSenha() {
		Long id = 1L;
		String nome = "Admin Test";
		String email = "admin@test.com";
		String senhaHash = "hashedPassword123";
		Boolean ativo = true;
		LocalDateTime dataCriacao = LocalDateTime.now();
		List<AdminRole> roles = Arrays.asList(new AdminRole(1L, "ADMIN", "Administrador do sistema."));

		AdminUser adminUser = new AdminUser(id, nome, email, senhaHash, ativo, dataCriacao, roles);
		AdminUser adminUserSemSenha = adminUser.semSenha();

		assertEquals(id, adminUserSemSenha.getId());
		assertEquals(nome, adminUserSemSenha.getNome());
		assertEquals(email, adminUserSemSenha.getEmail());
		assertNull(adminUserSemSenha.getSenhaHash());
		assertEquals(ativo, adminUserSemSenha.getAtivo());
		assertEquals(dataCriacao, adminUserSemSenha.getDataCriacao());
		assertEquals(roles, adminUserSemSenha.getRoles());
	}
}
