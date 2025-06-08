package br.com.postech.techchallange.adapter.in.rest.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class AdminRegisterRequestTest {

	@Test
	void testAllArgsConstructorAndGettersAndSetters() {
		AdminRegisterRequest req = new AdminRegisterRequest();
		req.setNome("Admin");
		req.setEmail("admin@test.com");
		req.setSenha("senha123");
		req.setRolesIds(List.of(1L, 2L));
		assertEquals("Admin", req.getNome());
		assertEquals("admin@test.com", req.getEmail());
		assertEquals("senha123", req.getSenha());
		assertEquals(List.of(1L, 2L), req.getRolesIds());
	}

	@Test
	void testEqualsAndHashCode() {
		AdminRegisterRequest req1 = new AdminRegisterRequest();
		req1.setNome("Admin");
		req1.setEmail("admin@test.com");
		req1.setSenha("senha123");
		req1.setRolesIds(List.of(1L, 2L));
		AdminRegisterRequest req2 = new AdminRegisterRequest();
		req2.setNome("Admin");
		req2.setEmail("admin@test.com");
		req2.setSenha("senha123");
		req2.setRolesIds(List.of(1L, 2L));
		assertEquals(req1, req2);
		assertEquals(req1.hashCode(), req2.hashCode());
	}

	@Test
	void testToString() {
		AdminRegisterRequest req = new AdminRegisterRequest();
		req.setNome("Admin");
		req.setEmail("admin@test.com");
		req.setSenha("senha123");
		req.setRolesIds(List.of(1L, 2L));
		String str = req.toString();
		assertTrue(str.contains("Admin"));
		assertFalse(str.contains("admin@test.com"));
		assertFalse(str.contains("senha123"));
		assertFalse(str.contains("rolesIds"));
	}

	@Test
	void testNullAndEmptyRolesIds() {
		AdminRegisterRequest req = new AdminRegisterRequest();
		assertNull(req.getRolesIds());
		req.setRolesIds(Collections.emptyList());
		assertTrue(req.getRolesIds().isEmpty());
	}
}
