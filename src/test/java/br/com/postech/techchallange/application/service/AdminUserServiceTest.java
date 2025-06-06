package br.com.postech.techchallange.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.postech.techchallange.adapter.in.rest.request.AdminRegisterRequest;
import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.AdminLogAcao;
import br.com.postech.techchallange.domain.model.AdminRole;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.out.AdminLogAcaoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.AdminRoleRepositoryPort;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;
import br.com.postech.techchallange.infra.security.TokenBlacklistService;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private AdminUserRepositoryPort userRepository;

    @Mock
    private AdminLogAcaoRepositoryPort logRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminRoleRepositoryPort adminRoleRepository;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @InjectMocks
    private AdminUserService adminUserService;

    @Captor
    private ArgumentCaptor<AdminLogAcao> logCaptor;

    @Captor
    private ArgumentCaptor<AdminUser> adminCaptor;

    @Test
    @DisplayName("Deve cadastrar admin com sucesso")
    void deveCadastrarAdminComSucesso() {
        // Arrange
        String nome = "Admin Teste";
        String email = "admin@test.com";
        String senha = "senha123";
        List<Long> rolesIds = Arrays.asList(1L);

        AdminRegisterRequest request = new AdminRegisterRequest();
        request.setNome(nome);
        request.setEmail(email);
        request.setSenha(senha);
        request.setRolesIds(rolesIds);

        AdminRole role = new AdminRole();
        role.setId(1L);
        role.setNome("ADMIN");

        String senhaHash = "hashedPassword";
        Long adminId = 1L;

        when(adminRoleRepository.listarTodos()).thenReturn(Arrays.asList(role));
        when(passwordEncoder.encode(senha)).thenReturn(senhaHash);
        when(userRepository.salvar(any())).thenAnswer(invocation -> {
            AdminUser admin = invocation.getArgument(0);
            admin.setId(adminId);
            return admin;
        });

        // Act
        AdminUser resultado = adminUserService.cadastrar(request);

        // Assert
        verify(userRepository).salvar(adminCaptor.capture());
        verify(adminRoleRepository).atribuirRole(adminId, role.getId());
        verify(logRepository).registrarLog(logCaptor.capture());

        AdminUser adminSalvo = adminCaptor.getValue();
        assertEquals(nome, adminSalvo.getNome());
        assertEquals(email, adminSalvo.getEmail());
        assertEquals(null, adminSalvo.getSenhaHash());
        assertTrue(adminSalvo.getAtivo());
        assertNotNull(adminSalvo.getDataCriacao());
        assertEquals(1, adminSalvo.getRoles().size());

        AdminLogAcao log = logCaptor.getValue();
        assertEquals(adminId, log.getIdAdmin());
        assertEquals("CADASTRO", log.getAcao());
        assertEquals("ADMIN_USER", log.getRecursoAfetado());
        assertEquals(adminId, log.getIdRecurso());
        assertNotNull(log.getDataAcao());

        assertNull(resultado.getSenhaHash(), "A senha não deve ser retornada na resposta");
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar admin sem roles")
    void deveLancarExcecaoAoCadastrarAdminSemRoles() {
        // Arrange
        AdminRegisterRequest request = new AdminRegisterRequest();
        request.setNome("Admin Teste");
        request.setEmail("admin@test.com");
        request.setSenha("senha123");
        request.setRolesIds(Arrays.asList());

        when(adminRoleRepository.listarTodos()).thenReturn(Arrays.asList());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> adminUserService.cadastrar(request));

        assertEquals("Um administrador precisa ter pelo menos uma role.", exception.getMessage());
        verify(userRepository, never()).salvar(any());
        verify(logRepository, never()).registrarLog(any());
    }

    @Test
    @DisplayName("Deve autenticar admin com sucesso")
    void deveAutenticarAdminComSucesso() {
        // Arrange
        String email = "admin@test.com";
        String senha = "senha123";
        String senhaHash = "hashedPassword";
        Long adminId = 1L;

        AdminUser admin = new AdminUser();
        admin.setId(adminId);
        admin.setEmail(email);
        admin.setSenhaHash(senhaHash);

        List<AdminRole> roles = Arrays.asList(new AdminRole());

        when(userRepository.buscarPorEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(senha, senhaHash)).thenReturn(true);
        when(adminRoleRepository.listarRolesPorAdminId(adminId)).thenReturn(roles);

        // Act
        AdminUser resultado = adminUserService.autenticar(email, senha);

        // Assert
        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        assertEquals(roles, resultado.getRoles());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email não encontrado na autenticação")
    void deveLancarExcecaoQuandoEmailNaoEncontradoNaAutenticacao() {
        // Arrange
        String email = "naoexiste@test.com";
        when(userRepository.buscarPorEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> adminUserService.autenticar(email, "qualquer"));

        assertEquals("Usuário ou senha inválidos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha incorreta na autenticação")
    void deveLancarExcecaoQuandoSenhaIncorretaNaAutenticacao() {
        // Arrange
        String email = "admin@test.com";
        String senha = "senhaErrada";
        String senhaHash = "hashedPassword";

        AdminUser admin = new AdminUser();
        admin.setEmail(email);
        admin.setSenhaHash(senhaHash);

        when(userRepository.buscarPorEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(senha, senhaHash)).thenReturn(false);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> adminUserService.autenticar(email, senha));

        assertEquals("Usuário ou senha inválidos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve processar logout com sucesso")
    void deveProcessarLogoutComSucesso() {
        // Arrange
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9...";
        String tokenSemBearer = "eyJhbGciOiJIUzI1NiJ9...";

        // Act
        adminUserService.logout(token);

        // Assert
        verify(tokenBlacklistService).blacklistToken(tokenSemBearer);
    }

    @Test
    @DisplayName("Deve ignorar logout quando token é null")
    void deveIgnorarLogoutQuandoTokenNull() {
        // Act
        adminUserService.logout(null);

        // Assert
        verify(tokenBlacklistService, never()).blacklistToken(any());
    }

    @Test
    @DisplayName("Deve ignorar logout quando token não começa com Bearer")
    void deveIgnorarLogoutQuandoTokenNaoComecaComBearer() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9...";

        // Act
        adminUserService.logout(token);

        // Assert
        verify(tokenBlacklistService, never()).blacklistToken(any());
    }

    @Test
    @DisplayName("Deve buscar admin por ID com sucesso")
    void deveBuscarAdminPorIdComSucesso() {
        // Arrange
        Long id = 1L;
        AdminUser admin = new AdminUser();
        admin.setId(id);

        when(userRepository.buscarPorId(id)).thenReturn(Optional.of(admin));

        // Act
        AdminUser resultado = adminUserService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando admin não encontrado por ID")
    void deveLancarExcecaoQuandoAdminNaoEncontradoPorId() {
        // Arrange
        Long id = 999L;
        when(userRepository.buscarPorId(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> adminUserService.buscarPorId(id));

        assertEquals("Admin não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar admin por email com sucesso")
    void deveBuscarAdminPorEmailComSucesso() {
        // Arrange
        String email = "admin@test.com";
        AdminUser admin = new AdminUser();
        admin.setEmail(email);

        when(userRepository.buscarPorEmail(email)).thenReturn(Optional.of(admin));

        // Act
        AdminUser resultado = adminUserService.buscarPorEmail(email);

        // Assert
        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção quando admin não encontrado por email")
    void deveLancarExcecaoQuandoAdminNaoEncontradoPorEmail() {
        // Arrange
        String email = "naoexiste@test.com";
        when(userRepository.buscarPorEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> adminUserService.buscarPorEmail(email));

        assertEquals("Admin não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve listar todos os admins com suas roles")
    void deveListarTodosAdminsComSuasRoles() {
        // Arrange
        Long adminId = 1L;
        AdminUser admin = new AdminUser();
        admin.setId(adminId);
        admin.setNome("Admin");
        admin.setSenhaHash("hash");

        List<AdminRole> roles = Arrays.asList(new AdminRole());

        when(userRepository.listar()).thenReturn(Arrays.asList(admin));
        when(adminRoleRepository.listarRolesPorAdminId(adminId)).thenReturn(roles);

        // Act
        List<AdminUser> resultado = adminUserService.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        AdminUser adminListado = resultado.get(0);
        assertEquals(adminId, adminListado.getId());
        assertNull(adminListado.getSenhaHash(), "A senha não deve ser incluída na listagem");
        assertEquals(roles, adminListado.getRoles());
    }
}
