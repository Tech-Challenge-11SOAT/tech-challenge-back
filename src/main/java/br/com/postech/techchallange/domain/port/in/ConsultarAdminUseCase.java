package br.com.postech.techchallange.domain.port.in;

import br.com.postech.techchallange.domain.model.AdminUser;

import java.util.List;

public interface ConsultarAdminUseCase {
	AdminUser buscarPorId(Long id);

	AdminUser buscarPorEmail(String email);

	List<AdminUser> listar();
}
