package br.com.postech.techchallange.adapter.in.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.postech.techchallange.adapter.in.rest.request.ChangePasswordRequest;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;
import br.com.postech.techchallange.infra.security.JwtProvider;
import jakarta.transaction.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AdminAuthControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private AdminUserRepositoryPort adminUserRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private String token;

	@BeforeEach
	void setup() {
		AdminUser adminSalvo = adminUserRepository.buscarPorEmail("admin@example.com")
				.orElseThrow(() -> new RuntimeException("Admin de teste não encontrado"));

		token = jwtProvider.generateAccessToken(adminSalvo);
	}

	@Test
	void deveAlterarSenhaComSucesso() throws Exception {
		ChangePasswordRequest request = new ChangePasswordRequest("senhaAtual", "novaSenha123");

		mockMvc.perform(post("/admin/auth/change-password")
		.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());
	}

	@Test
	void deveFalharComSenhaAtualIncorreta() throws Exception {
		ChangePasswordRequest request = new ChangePasswordRequest("senhaErrada", "novaSenha123");

		mockMvc.perform(post("/admin/auth/change-password")
		.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}
}
