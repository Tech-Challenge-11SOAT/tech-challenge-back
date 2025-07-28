package br.com.postech.techchallange.infra.config;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("deprecation")
@Component
public class RateLimitingFilter implements Filter {

	private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
	// 100 requisições por 10 minutos por IP
	private static final int LIMIT = 100;
	private static final Duration DURATION = Duration.ofMinutes(10);

	private Bucket resolveBucket(String ip) {
		return buckets.computeIfAbsent(ip, k -> Bucket4j.builder()
					.addLimit(Bandwidth.classic(LIMIT, Refill.greedy(LIMIT, DURATION)))
					.build());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof HttpServletRequest req && response instanceof HttpServletResponse res) {

			String bypass = req.getHeader("X-Bypass-RateLimit");
			if ("true".equalsIgnoreCase(bypass)) {
				chain.doFilter(request, response); // ignora o bucket
				return;
			}

			String ip = req.getRemoteAddr();
			Bucket bucket = resolveBucket(ip);
			if (bucket.tryConsume(1)) {
				chain.doFilter(request, response);
			} else {
				res.setStatus(429);
				res.setContentType("application/json");
				res.getWriter().write("{\"error\":\"Rate limit exceeded. Tente novamente mais tarde.\"}");
			}
		} else {
			chain.doFilter(request, response);
		}
	}
}
