package br.com.postech.techchallange.adapter.in.rest.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class RefreshTokenRequestTest {

    @Test
    void testRecordConstructorAndGetter() {
        RefreshTokenRequest req = new RefreshTokenRequest("refresh-token");
        assertEquals("refresh-token", req.refreshToken());
    }

    @Test
    void testEqualsAndHashCode() {
        RefreshTokenRequest req1 = new RefreshTokenRequest("refresh-token");
        RefreshTokenRequest req2 = new RefreshTokenRequest("refresh-token");
        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToString() {
        RefreshTokenRequest req = new RefreshTokenRequest("refresh-token");
        String str = req.toString();
        assertTrue(str.contains("refresh-token"));
    }
}
