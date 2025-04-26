package br.com.postech.techchallange.adapter.in.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.postech.techchallange.adapter.in.rest.request.ChangePasswordRequest;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;
import br.com.postech.techchallange.infra.security.JwtProvider;

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
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void deveAlterarSenhaComSucesso() throws Exception {
		// Arrange - cria o admin de teste se não existir
		AdminUser admin = adminUserRepository.buscarPorEmail("admin@example.com").orElseGet(() -> {
			AdminUser novoAdmin = new AdminUser();
			novoAdmin.setNome("Admin Teste");
			novoAdmin.setEmail("admin@example.com");
			novoAdmin.setSenhaHash(passwordEncoder.encode("senhaAtual"));
			novoAdmin.setAtivo(true);
			novoAdmin.setDataCriacao(LocalDateTime.now());
			return adminUserRepository.salvar(novoAdmin);
		});

		String token = jwtProvider.generateAccessToken(admin);

		ChangePasswordRequest request = new ChangePasswordRequest("senhaAtual", "novaSenha123");

		// Act + Assert
		mockMvc.perform(post("/admin/auth/change-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

}
