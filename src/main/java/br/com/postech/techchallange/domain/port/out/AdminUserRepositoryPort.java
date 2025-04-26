package br.com.postech.techchallange.domain.port.out;

import java.util.List;
import java.util.Optional;

import br.com.postech.techchallange.domain.model.AdminUser;

public interface AdminUserRepositoryPort {

	Optional<AdminUser> buscarPorId(Long id);

	Optional<AdminUser> buscarPorEmail(String email);

	AdminUser salvar(AdminUser admin);

	List<AdminUser> listar();
}
