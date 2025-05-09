package br.com.postech.techchallange.domain.constants;

public class SecurityConstants {

	private SecurityConstants() {
		throw new IllegalStateException("Utility class");
	}

	public static final String[] AUTHORIZED_URLS = new String[] { "/admin/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/open/**" };
	public static final String ADMIN_ROLE = "ROLE_ADMIN";

}
