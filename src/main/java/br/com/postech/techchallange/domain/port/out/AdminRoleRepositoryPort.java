package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.AdminRole;

import java.util.List;

public interface AdminRoleRepositoryPort {
	List<AdminRole> listarTodos();
}
