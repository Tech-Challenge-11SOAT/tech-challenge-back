package br.com.postech.techchallange.adapter.out.persistence.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import br.com.postech.techchallange.adapter.in.rest.response.PedidoCompletoResponse;
import br.com.postech.techchallange.adapter.out.persistence.common.GenericJdbcRepository;
import br.com.postech.techchallange.adapter.out.persistence.entity.PedidoEntity;
import br.com.postech.techchallange.adapter.out.persistence.mapper.PedidoMapper;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.out.PedidoRepositoryPort;

@Repository
public class PedidoJdbcRepositoryAdapter extends GenericJdbcRepository<PedidoEntity> implements PedidoRepositoryPort {

	private static final String SQL_BASE = """
		SELECT
		  p.id_pedido,
		  p.id_cliente,
		  p.data_pedido,
		  p.id_status_pedido,
		  p.data_status
		FROM pedido p
	""";
	
	private static final String LISTAR_PEDIDOS_COMPLETOS = """
			SELECT
			  p.id_pedido,
			  p.id_cliente,
			  p.data_pedido,
			  p.id_status_pedido,
			  p.data_status,
			  c.nome_cliente AS cliente_nome,
			  c.email_cliente AS cliente_email,
			  s.nome_status AS status_nome
			FROM pedido p
			JOIN cliente c ON c.id_cliente = p.id_cliente
			JOIN status_pedido s ON s.id_status_pedido = p.id_status_pedido
		""";


	private static final String INSERT_PEDIDO = """
		INSERT INTO pedido (id_cliente, data_pedido, id_status_pedido, data_status)
		VALUES (?, ?, ?, ?)
	""";

	private static final String SELECT_BY_ID = SQL_BASE + " WHERE p.id_pedido = ?";

	private static final String UPDATE_STATUS = """
		UPDATE pedido SET id_status_pedido = ?, data_status = ? WHERE id_pedido = ?
	""";

	public PedidoJdbcRepositoryAdapter(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, new PedidoRowMapper());
	}

	@Override
	public Pedido salvar(Pedido pedido) {
		Long idGerado = super.insertAndReturnId(
			INSERT_PEDIDO,
			pedido.getIdCliente(),
			pedido.getDataPedido(),
			pedido.getIdStatusPedido(),
			pedido.getDataStatus()
		);
		return this.buscarPorId(idGerado).orElseThrow();
	}

	@Override
	public Optional<Pedido> buscarPorId(Long id) {
		try {
			return Optional.ofNullable(PedidoMapper.toDomain(super.queryOne(SELECT_BY_ID, id)));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public List<PedidoCompletoResponse> listarTodos() {

		RowMapper<PedidoCompletoResponse> rowMapper = new PedidoCompletoResponseRowMapper();
		return super.jdbcTemplate.query(LISTAR_PEDIDOS_COMPLETOS, rowMapper);
	}

	@Override
	public Pedido atualizar(Pedido pedido, Long statusId) {
		LocalDateTime agora = LocalDateTime.now();
		super.executeUpdate(UPDATE_STATUS, statusId, agora, pedido.getId());
		return this.buscarPorId(pedido.getId()).orElseThrow();
	}

	// RowMapper para PedidoEntity
	private static class PedidoRowMapper implements RowMapper<PedidoEntity> {
		@Override
		public PedidoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			PedidoEntity entity = new PedidoEntity();
			entity.setIdPedido(rs.getLong("id_pedido"));
			entity.setIdCliente(rs.getLong("id_cliente"));
			entity.setDataPedido(rs.getTimestamp("data_pedido").toLocalDateTime());
			entity.setIdStatusPedido(rs.getLong("id_status_pedido"));
			entity.setDataStatus(rs.getTimestamp("data_status").toLocalDateTime());
			return entity;
		}
	}

	// RowMapper para o endpoint enriquecido
	public static class PedidoCompletoResponseRowMapper implements RowMapper<PedidoCompletoResponse> {
		@Override
		public PedidoCompletoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new PedidoCompletoResponse(
				rs.getLong("id_pedido"),
				rs.getTimestamp("data_pedido").toLocalDateTime(),
				rs.getTimestamp("data_status").toLocalDateTime(),
				new PedidoCompletoResponse.Cliente(
					rs.getLong("id_cliente"),
					rs.getString("cliente_nome"),
					rs.getString("cliente_email")
				),
				new PedidoCompletoResponse.StatusPedido(
					rs.getLong("id_status_pedido"),
					rs.getString("status_nome")
				)
			);
		}
	}
}





