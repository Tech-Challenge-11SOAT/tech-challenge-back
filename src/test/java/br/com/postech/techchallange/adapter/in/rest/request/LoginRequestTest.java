package br.com.postech.techchallange.adapter.in.rest.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class LoginRequestTest {

    @Test
    void testRecordConstructorAndGetters() {
        LoginRequest req = new LoginRequest("email@test.com", "senha123");
        assertEquals("email@test.com", req.email());
        assertEquals("senha123", req.senha());
    }

    @Test
    void testEqualsAndHashCode() {
        LoginRequest req1 = new LoginRequest("email@test.com", "senha123");
        LoginRequest req2 = new LoginRequest("email@test.com", "senha123");
        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToString() {
        LoginRequest req = new LoginRequest("email@test.com", "senha123");
        String str = req.toString();
        assertTrue(str.contains("email@test.com"));
        assertTrue(str.contains("senha123"));
    }
}
