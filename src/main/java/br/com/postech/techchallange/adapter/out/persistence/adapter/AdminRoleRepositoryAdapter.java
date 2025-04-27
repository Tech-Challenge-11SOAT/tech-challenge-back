package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.adapter.out.persistence.common.GenericJdbcRepository;
import br.com.postech.techchallange.domain.model.AdminRole;
import br.com.postech.techchallange.domain.port.out.AdminRoleRepositoryPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminRoleRepositoryAdapter extends GenericJdbcRepository<AdminRole> implements AdminRoleRepositoryPort {

	private static final String INSERT_ROLE = "INSERT INTO admin_role (nome, descricao) VALUES (?, ?)";
	private static final String FIND_ROLE_BY_NAME = "SELECT * FROM admin_role WHERE nome = ?";
	private static final String LIST_ALL_ROLES = "SELECT * FROM admin_role";
	private static final String LIST_ROLES_BY_ADMIN = """
			    SELECT r.id, r.nome, r.descricao
			    FROM admin_role r
			    INNER JOIN admin_user_role ur ON r.id = ur.id_role
			    WHERE ur.id_admin = ?
			""";

	public AdminRoleRepositoryAdapter(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, new AdminRoleRowMapper());
	}

	@Override
	public AdminRole salvar(AdminRole role) {
		super.executeUpdate(INSERT_ROLE, role.getNome(), role.getDescricao());
		return super.queryOne(FIND_ROLE_BY_NAME, role.getNome());
	}

	@Override
	public void atribuirRole(Long idAdmin, Long idRole) {
		super.executeUpdate("INSERT INTO admin_user_role (id_admin, id_role) VALUES (?, ?)", idAdmin, idRole);
	}

	@Override
	public List<AdminRole> listarTodos() {
		return super.queryList(LIST_ALL_ROLES);
	}

	@Override
	public List<AdminRole> listarRolesPorAdminId(Long idAdmin) {
		return super.queryList(LIST_ROLES_BY_ADMIN, idAdmin);
	}

	private static class AdminRoleRowMapper implements RowMapper<AdminRole> {
		@Override
		public AdminRole mapRow(ResultSet rs, int rowNum) throws SQLException {
			return AdminRole.builder()
					.id(rs.getLong("id"))
					.nome(rs.getString("nome"))
					.descricao(rs.getString("descricao"))
					.build();
		}
	}
}
