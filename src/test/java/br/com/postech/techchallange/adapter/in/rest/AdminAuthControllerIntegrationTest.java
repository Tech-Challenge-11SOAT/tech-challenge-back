package br.com.postech.techchallange.adapter.in.rest;

import br.com.postech.techchallange.adapter.in.rest.request.AdminRegisterRequest;
import br.com.postech.techchallange.adapter.in.rest.request.ChangePasswordRequest;
import br.com.postech.techchallange.adapter.in.rest.request.LoginRequest;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;
import br.com.postech.techchallange.infra.security.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes de Integração do AdminAuthController")
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
    private static final String BASE_URL = "/admin/auth";
    private static final String TEST_EMAIL = "admin@example.com";
    private static final String TEST_PASSWORD = "senha123";

    @BeforeEach
    void setup() {
        AdminUser admin = AdminUser.builder()
                .email(TEST_EMAIL)
                .nome("Admin Teste")
                .senhaHash(passwordEncoder.encode(TEST_PASSWORD))
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build();

        admin = adminUserRepository.salvar(admin);
        token = jwtProvider.generateAccessToken(admin);
    }

    @Nested
    @DisplayName("Testes de Login")
    class LoginTests {
        @Test
        @DisplayName("Deve realizar login com sucesso")
        void deveRealizarLoginComSucesso() throws Exception {
            LoginRequest request = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);

            mockMvc.perform(post(BASE_URL + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(jsonPath("$.refreshToken").exists());
        }

        @ParameterizedTest
        @ValueSource(strings = {"email_invalido@test.com", "naoexiste@example.com"})
        @DisplayName("Deve falhar login com email inválido")
        void deveFalharLoginComEmailInvalido(String email) throws Exception {
            LoginRequest request = new LoginRequest(email, TEST_PASSWORD);

            mockMvc.perform(post(BASE_URL + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Deve falhar login com senha incorreta")
        void deveFalharLoginComSenhaIncorreta() throws Exception {
            LoginRequest request = new LoginRequest(TEST_EMAIL, "senha_incorreta");

            mockMvc.perform(post(BASE_URL + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Testes de Alteração de Senha")
    class AlteracaoSenhaTests {
        @Test
        @DisplayName("Deve alterar senha com sucesso")
        void deveAlterarSenhaComSucesso() throws Exception {
            ChangePasswordRequest request = new ChangePasswordRequest();
            request.setCurrentPassword(TEST_PASSWORD);
            request.setNewPassword("nova_senha123");

            mockMvc.perform(post(BASE_URL + "/change-password")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            // Verifica se consegue fazer login com a nova senha
            LoginRequest loginRequest = new LoginRequest(TEST_EMAIL, "nova_senha123");
            mockMvc.perform(post(BASE_URL + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Deve falhar ao alterar senha sem autenticação")
        void deveFalharAlterarSenhaSemAutenticacao() throws Exception {
            ChangePasswordRequest request = new ChangePasswordRequest();
            request.setCurrentPassword(TEST_PASSWORD);
            request.setNewPassword("nova_senha123");

            mockMvc.perform(post(BASE_URL + "/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Testes de Registro de Admin")
    class RegistroAdminTests {
        @Test
        @DisplayName("Deve registrar novo admin com sucesso")
        void deveRegistrarNovoAdminComSucesso() throws Exception {
            AdminRegisterRequest request = new AdminRegisterRequest();
            request.setEmail("novo@admin.com");
            request.setNome("Novo Admin");
            request.setSenha("senha123");
            request.setRolesIds(Arrays.asList(1L));

            mockMvc.perform(post(BASE_URL + "/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("novo@admin.com"))
                    .andExpect(jsonPath("$.nome").value("Novo Admin"));
        }

        @Test
        @DisplayName("Deve falhar ao registrar admin com email duplicado")
        void deveFalharRegistrarAdminComEmailDuplicado() throws Exception {
            AdminRegisterRequest request = new AdminRegisterRequest();
            request.setEmail(TEST_EMAIL);
            request.setNome("Admin Duplicado");
            request.setSenha("senha123");
            request.setRolesIds(Arrays.asList(1L));

            mockMvc.perform(post(BASE_URL + "/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().is5xxServerError());
        }

        @Test
        @DisplayName("Deve falhar ao registrar admin sem roles")
        void deveFalharRegistrarAdminSemRoles() throws Exception {
            AdminRegisterRequest request = new AdminRegisterRequest();
            request.setEmail("novo2@admin.com");
            request.setNome("Novo Admin");
            request.setSenha("senha123");
            request.setRolesIds(null);

            mockMvc.perform(post(BASE_URL + "/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}
