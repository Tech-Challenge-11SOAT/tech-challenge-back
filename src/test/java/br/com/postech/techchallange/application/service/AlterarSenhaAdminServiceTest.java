package br.com.postech.techchallange.application.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.LogAdminActionUseCase;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class AlterarSenhaAdminServiceTest {

    @Mock
    private AdminUserRepositoryPort adminUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private LogAdminActionUseCase logAdminActionUseCase;

    @InjectMocks
    private AlterarSenhaAdminService alterarSenhaAdminService;

    @Captor
    private ArgumentCaptor<AdminUser> adminCaptor;

    @Test
    @DisplayName("Deve alterar senha com sucesso quando dados estão corretos")
    void deveAlterarSenhaComSucesso() {
        // Arrange
        String email = "admin@test.com";
        String senhaAtual = "senha123";
        String novaSenha = "novaSenha123";
        String senhaAtualHash = "hashedSenhaAtual";
        String novaSenhaHash = "hashedNovaSenha";
        Long adminId = 1L;

        AdminUser admin = new AdminUser();
        admin.setId(adminId);
        admin.setEmail(email);
        admin.setSenhaHash(senhaAtualHash);

        when(adminUserRepository.buscarPorEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(senhaAtual, senhaAtualHash)).thenReturn(true);
        when(passwordEncoder.encode(novaSenha)).thenReturn(novaSenhaHash);
        when(adminUserRepository.salvar(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        alterarSenhaAdminService.alterarSenha(email, senhaAtual, novaSenha);

        // Assert
        verify(adminUserRepository).salvar(adminCaptor.capture());
        verify(logAdminActionUseCase).registrar(adminId, "ALTERACAO_SENHA", "ADMIN_USER", adminId);
        
        AdminUser adminSalvo = adminCaptor.getValue();
        assertEquals(novaSenhaHash, adminSalvo.getSenhaHash());
        assertEquals(email, adminSalvo.getEmail());
        assertEquals(adminId, adminSalvo.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando administrador não encontrado")
    void deveLancarExcecaoQuandoAdminNaoEncontrado() {
        // Arrange
        String email = "naoexiste@test.com";
        when(adminUserRepository.buscarPorEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> alterarSenhaAdminService.alterarSenha(email, "qualquer", "qualquer"));
        
        assertEquals("Administrador não encontrado.", exception.getMessage());
        verify(adminUserRepository, never()).salvar(any());
        verify(logAdminActionUseCase, never()).registrar(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha atual incorreta")
    void deveLancarExcecaoQuandoSenhaAtualIncorreta() {
        // Arrange
        String email = "admin@test.com";
        String senhaAtual = "senhaErrada";
        String novaSenha = "novaSenha123";
        String senhaAtualHash = "hashedSenhaAtual";

        AdminUser admin = new AdminUser();
        admin.setEmail(email);
        admin.setSenhaHash(senhaAtualHash);

        when(adminUserRepository.buscarPorEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(senhaAtual, senhaAtualHash)).thenReturn(false);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> alterarSenhaAdminService.alterarSenha(email, senhaAtual, novaSenha));
        
        assertEquals("Senha atual incorreta.", exception.getMessage());
        verify(adminUserRepository, never()).salvar(any());
        verify(logAdminActionUseCase, never()).registrar(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nova senha igual à atual")
    void deveLancarExcecaoQuandoNovaSenhaIgualAtual() {
        // Arrange
        String email = "admin@test.com";
        String senha = "senha123";
        String senhaHash = "hashedSenha";

        AdminUser admin = new AdminUser();
        admin.setEmail(email);
        admin.setSenhaHash(senhaHash);

        when(adminUserRepository.buscarPorEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(senha, senhaHash)).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> alterarSenhaAdminService.alterarSenha(email, senha, senha));
        
        assertEquals("A nova senha não pode ser igual à senha atual.", exception.getMessage());
        verify(adminUserRepository, never()).salvar(any());
        verify(logAdminActionUseCase, never()).registrar(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nova senha muito curta")
    void deveLancarExcecaoQuandoNovaSenhaMuitoCurta() {
        // Arrange
        String email = "admin@test.com";
        String senhaAtual = "senha123";
        String novaSenha = "123";  // curta e sem letras
        String senhaHash = "hashedSenha";

        AdminUser admin = new AdminUser();
        admin.setEmail(email);
        admin.setSenhaHash(senhaHash);

        when(adminUserRepository.buscarPorEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(senhaAtual, senhaHash)).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> alterarSenhaAdminService.alterarSenha(email, senhaAtual, novaSenha));
        
        assertEquals("A nova senha deve conter pelo menos 8 caracteres, letras e números.", exception.getMessage());
        verify(adminUserRepository, never()).salvar(any());
        verify(logAdminActionUseCase, never()).registrar(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nova senha não contém números")
    void deveLancarExcecaoQuandoNovaSenhaSemNumeros() {
        // Arrange
        String email = "admin@test.com";
        String senhaAtual = "senha123";
        String novaSenha = "senhaNovaAbc";  // sem números
        String senhaHash = "hashedSenha";

        AdminUser admin = new AdminUser();
        admin.setEmail(email);
        admin.setSenhaHash(senhaHash);

        when(adminUserRepository.buscarPorEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(senhaAtual, senhaHash)).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> alterarSenhaAdminService.alterarSenha(email, senhaAtual, novaSenha));
        
        assertEquals("A nova senha deve conter pelo menos 8 caracteres, letras e números.", exception.getMessage());
        verify(adminUserRepository, never()).salvar(any());
        verify(logAdminActionUseCase, never()).registrar(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nova senha não contém letras")
    void deveLancarExcecaoQuandoNovaSenhaSemLetras() {
        // Arrange
        String email = "admin@test.com";
        String senhaAtual = "senha123";
        String novaSenha = "12345678";  // sem letras
        String senhaHash = "hashedSenha";

        AdminUser admin = new AdminUser();
        admin.setEmail(email);
        admin.setSenhaHash(senhaHash);

        when(adminUserRepository.buscarPorEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(senhaAtual, senhaHash)).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> alterarSenhaAdminService.alterarSenha(email, senhaAtual, novaSenha));
        
        assertEquals("A nova senha deve conter pelo menos 8 caracteres, letras e números.", exception.getMessage());
        verify(adminUserRepository, never()).salvar(any());
        verify(logAdminActionUseCase, never()).registrar(any(), any(), any(), any());
    }
}
