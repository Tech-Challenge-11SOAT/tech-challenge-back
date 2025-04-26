package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.domain.model.AdminRole;
import br.com.postech.techchallange.domain.port.out.AdminRoleRepositoryPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminRoleRepositoryAdapter implements AdminRoleRepositoryPort {

	private final JdbcTemplate jdbcTemplate;

	public AdminRoleRepositoryAdapter(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public AdminRole salvar(AdminRole role) {
		String sql = "INSERT INTO admin_role (nome, descricao) VALUES (?, ?)";
		jdbcTemplate.update(sql, role.getNome(), role.getDescricao());

		AdminRole roleSalva = jdbcTemplate.queryForObject(
				"SELECT * FROM admin_role WHERE nome = ?",
				new AdminRoleRowMapper(),
				role.getNome()
			);

		return roleSalva;
	}

	@Override
	public void atribuirRole(Long idAdmin, Long idRole) {
		String sql = "INSERT INTO admin_user_role (id_admin, id_role) VALUES (?, ?)";
		jdbcTemplate.update(sql, idAdmin, idRole);
	}

	@Override
	public List<AdminRole> listarTodos() {
		String sql = "SELECT * FROM admin_role";
		return jdbcTemplate.query(sql, new AdminRoleRowMapper());
	}

	@Override
	public List<AdminRole> listarRolesPorAdminId(Long idAdmin) {
		String sql = """
				SELECT r.id, r.nome, r.descricao
				FROM admin_role r
				INNER JOIN admin_user_role ur ON r.id = ur.id_role
				WHERE ur.id_admin = ?
				""";

		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			AdminRole role = new AdminRole();
			role.setId(rs.getLong("id"));
			role.setNome(rs.getString("nome"));
			role.setDescricao(rs.getString("descricao"));
			return role;
		}, idAdmin);
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
