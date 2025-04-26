package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.domain.model.AdminRole;

public interface CadastrarRoleUseCase {
	AdminRole cadastrar(AdminRole role);
}
