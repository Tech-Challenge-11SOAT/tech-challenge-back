package br.com.postech.techchallange.infra.config;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class RateLimitingFilterTest {

	private RateLimitingFilter filter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain chain;
	private StringWriter responseWriter;

	@BeforeEach
	void setUp() throws Exception {
		filter = new RateLimitingFilter();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		chain = mock(FilterChain.class);
		responseWriter = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
	}

	@Test
    void allowsRequestsWithinLimit() throws Exception {
        when(request.getRemoteAddr()).thenReturn("1.2.3.4");
        // Consome 5 requisições, todas devem passar
        for (int i = 0; i < 5; i++) {
            filter.doFilter(request, response, chain);
        }
        verify(chain, times(5)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        verify(response, never()).setStatus(429);
    }

	@Test
    void blocksRequestsOverLimit() throws Exception {
        when(request.getRemoteAddr()).thenReturn("5.6.7.8");
        // Consome 100 requisições (limite)
        for (int i = 0; i < 100; i++) {
            filter.doFilter(request, response, chain);
        }
        // 101ª deve ser bloqueada
        filter.doFilter(request, response, chain);
        verify(response).setStatus(429);
        assertTrue(responseWriter.toString().contains("Rate limit exceeded"));
    }

	@Test
    void allowsDifferentIpsIndependently() throws Exception {
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        filter.doFilter(request, response, chain);
        when(request.getRemoteAddr()).thenReturn("10.0.0.2");
        filter.doFilter(request, response, chain);
        verify(chain, times(2)).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }
}
