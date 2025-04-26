package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.AdminUser;

import java.util.List;
import java.util.Optional;

public interface AdminUserRepositoryPort {
	AdminUser salvar(AdminUser admin);

	Optional<AdminUser> buscarPorId(Long id);

	Optional<AdminUser> buscarPorEmail(String email);

	List<AdminUser> listar();
}
