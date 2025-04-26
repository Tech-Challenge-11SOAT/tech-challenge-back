package br.com.postech.techchallange.application.service;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.ToggleAdminUseCase;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InativarAdminService implements ToggleAdminUseCase {

	private final AdminUserRepositoryPort adminUserRepository;

	@Override
	public void toggle(Long idAdmin) {
		AdminUser admin = adminUserRepository.buscarPorId(idAdmin)
				.orElseThrow(() -> new BusinessException("Administrador não encontrado"));

		admin.setAtivo(!admin.getAtivo());
		this.adminUserRepository.salvar(admin);
	}
}
