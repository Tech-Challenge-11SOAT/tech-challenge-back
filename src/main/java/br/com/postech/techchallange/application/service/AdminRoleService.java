package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.AdminRole;
import br.com.postech.techchallange.domain.port.in.CadastrarRoleUseCase;
import br.com.postech.techchallange.domain.port.in.AtribuirRoleUseCase;
import br.com.postech.techchallange.domain.port.out.AdminRoleRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class AdminRoleService implements CadastrarRoleUseCase, AtribuirRoleUseCase {

	private final AdminRoleRepositoryPort roleRepository;

	public AdminRoleService(AdminRoleRepositoryPort roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public AdminRole cadastrar(AdminRole role) {
		return roleRepository.salvar(role);
	}

	@Override
	public void atribuirRole(Long idAdmin, Long idRole) {
		roleRepository.atribuirRole(idAdmin, idRole);
	}
}
