package br.com.postech.techchallange.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.AlterarSenhaAdminUseCase;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlterarSenhaAdminService implements AlterarSenhaAdminUseCase {

	private final AdminUserRepositoryPort adminUserRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void alterarSenha(String email, String senhaAtual, String novaSenha) {
		AdminUser admin = adminUserRepository.buscarPorEmail(email)
				.orElseThrow(() -> new BusinessException("Administrador não encontrado"));

		if (admin.getAtivo() == null || !admin.getAtivo()) {
			throw new BusinessException("Administrador inativo. Não é possível alterar a senha.");
		}

		if (!passwordEncoder.matches(senhaAtual, admin.getSenhaHash())) {
			throw new BusinessException("Senha atual incorreta");
		}

		admin.setSenhaHash(passwordEncoder.encode(novaSenha));
		adminUserRepository.salvar(admin);
	}

}
