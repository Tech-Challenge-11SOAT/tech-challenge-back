package br.com.postech.techchallange.adapter.out.persistence.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(PedidoJdbcRepositoryAdapter.class);

	private static final String SQL_BASE;
	private static final String LISTAR_PEDIDOS_COMPLETOS;
	private static final String LISTAR_PEDIDOS_POR_STATUS;

	static {
		StringBuilder base = new StringBuilder();
		base.append("SELECT ")
			.append(	"p.id_pedido, ")
			.append(	"p.id_cliente, ")
			.append(	"p.data_pedido, ")
			.append(	"p.id_status_pedido, ")
			.append(	"p.data_status, ")
			.append(	"c.nome_cliente AS cliente_nome, ")
			.append(	"c.email_cliente AS cliente_email, ")
			.append(	"s.nome_status AS status_nome ")
			.append( "FROM pedido p ")
			.append("JOIN cliente c ON c.id_cliente = p.id_cliente ")
			.append("JOIN status_pedido s ON s.id_status_pedido = p.id_status_pedido ");
		SQL_BASE = base.toString();

		LISTAR_PEDIDOS_COMPLETOS = SQL_BASE;

		StringBuilder porStatus = new StringBuilder(SQL_BASE);
		porStatus.append("WHERE p.id_status_pedido = ? ");
		LISTAR_PEDIDOS_POR_STATUS = porStatus.toString();
	}

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
		logger.info("Iniciando método para salvar pedido: {} || Insert: {}", pedido.toString(), INSERT_PEDIDO);
		
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
		logger.info("Iniciando método para buscar pedido por id: {} || Select: {}", id, SELECT_BY_ID);
		
		try {
			return Optional.ofNullable(PedidoMapper.toDomain(super.queryOne(SELECT_BY_ID, id)));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public List<PedidoCompletoResponse> listarTodos() {
		logger.info("Iniciando método para buscar todos os pedido - Select: {}", LISTAR_PEDIDOS_COMPLETOS);
		
		RowMapper<PedidoCompletoResponse> rowMapper = new PedidoCompletoResponseRowMapper();
		return super.jdbcTemplate.query(LISTAR_PEDIDOS_COMPLETOS, rowMapper);
	}
	
	@Override
	public List<PedidoCompletoResponse> listarPorStatus(Long statusId) {
		logger.info("Iniciando método para buscar os pedido por status: {} - Select: {}", statusId, LISTAR_PEDIDOS_POR_STATUS);
		
		RowMapper<PedidoCompletoResponse> rowMapper = new PedidoCompletoResponseRowMapper();
		return super.jdbcTemplate.query(LISTAR_PEDIDOS_POR_STATUS, rowMapper, statusId);
	}

	@Override
	public Pedido atualizar(Pedido pedido, Long statusId) {
		logger.info("Iniciando método para atualizar pedido: {} || Update: {}", pedido.toString(), UPDATE_STATUS);
		
		LocalDateTime agora = LocalDateTime.now();
		super.executeUpdate(UPDATE_STATUS, statusId, agora, pedido.getId());
		return this.buscarPorId(pedido.getId()).orElseThrow();
	}

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
