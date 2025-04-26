package br.com.postech.techchallange.domain.port.out;

import br.com.postech.techchallange.domain.model.AdminRole;
import java.util.List;

public interface AdminRoleRepositoryPort {

	AdminRole salvar(AdminRole role);

	void atribuirRole(Long idAdmin, Long idRole);

	List<AdminRole> listarTodos();

	List<AdminRole> listarRolesPorAdminId(Long idAdmin);
}
