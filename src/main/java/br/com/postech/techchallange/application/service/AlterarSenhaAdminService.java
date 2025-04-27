package br.com.postech.techchallange.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.AlterarSenhaAdminUseCase;
import br.com.postech.techchallange.domain.port.in.LogAdminActionUseCase;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlterarSenhaAdminService implements AlterarSenhaAdminUseCase {

	private final AdminUserRepositoryPort adminUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final LogAdminActionUseCase logAdminActionUseCase;

	@Override
	@Transactional
	public void alterarSenha(String email, String currentPassword, String newPassword) {
		AdminUser admin = adminUserRepository.buscarPorEmail(email)
				.orElseThrow(() -> new BusinessException("Administrador não encontrado."));

		if (!passwordEncoder.matches(currentPassword, admin.getSenhaHash())) {
			throw new BusinessException("Senha atual incorreta.");
		}

		validarNovaSenha(currentPassword, newPassword);

		admin.setSenhaHash(passwordEncoder.encode(newPassword));
		adminUserRepository.salvar(admin);

		logAdminActionUseCase.registrar(admin.getId(), "ALTERACAO_SENHA", "ADMIN_USER", admin.getId());
	}

	private void validarNovaSenha(String senhaAtual, String novaSenha) {
		if (senhaAtual.equals(novaSenha)) {
			throw new BusinessException("A nova senha não pode ser igual à senha atual.");
		}

		if (novaSenha.length() < 8 || !novaSenha.matches(".*\\d.*") || !novaSenha.matches(".*[a-zA-Z].*")) {
			throw new BusinessException("A nova senha deve conter pelo menos 8 caracteres, letras e números.");
		}
	}
}
