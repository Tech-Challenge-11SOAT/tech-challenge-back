package br.com.postech.techchallange.adapter.in.rest.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ChangePasswordRequestTest {

	@Test
	void testAllArgsConstructorAndGetters() {
		ChangePasswordRequest req = new ChangePasswordRequest("old", "new");
		assertEquals("old", req.getCurrentPassword());
		assertEquals("new", req.getNewPassword());
	}

	@Test
	void testNoArgsConstructorAndSetters() {
		ChangePasswordRequest req = new ChangePasswordRequest();
		req.setCurrentPassword("old");
		req.setNewPassword("new");
		assertEquals("old", req.getCurrentPassword());
		assertEquals("new", req.getNewPassword());
	}

	@Test
	void testEqualsAndHashCode() {
		ChangePasswordRequest req1 = new ChangePasswordRequest("old", "new");
		ChangePasswordRequest req2 = new ChangePasswordRequest("old", "new");
		assertEquals(req1, req2);
		assertEquals(req1.hashCode(), req2.hashCode());
	}

	@Test
	void testToString() {
		ChangePasswordRequest req = new ChangePasswordRequest("old", "new");
		String str = req.toString();
		assertFalse(str.contains("old"));
		assertFalse(str.contains("new"));
	}

	@Test
	void testNullFields() {
		ChangePasswordRequest req = new ChangePasswordRequest();
		assertNull(req.getCurrentPassword());
		assertNull(req.getNewPassword());
	}
}
