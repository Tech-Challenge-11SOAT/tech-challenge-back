package br.com.postech.techchallange.adapter.in.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private String token;

	@BeforeEach
	void setup() {
		adminUserRepository.listar().stream().filter(a -> a.getEmail().equals("admin@example.com")).forEach(
				a -> adminUserRepository.salvar(AdminUser.builder()
						.id(a.getId())
						.email(a.getEmail())
						.nome(a.getNome())
						.senhaHash(a.getSenhaHash())
						.ativo(false)
						.dataCriacao(a.getDataCriacao())
						.build()
					));
		
		String senhaHash = passwordEncoder.encode("senhaAtual");
		AdminUser novo = AdminUser.builder()
				.email("admin@example.com")
				.nome("Admin Teste")
				.senhaHash(senhaHash)
				.ativo(true)
				.dataCriacao(java.time.LocalDateTime.now())
				.build();

		AdminUser admin = adminUserRepository.salvar(novo);
		token = jwtProvider.generateAccessToken(admin);
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
