package br.com.postech.techchallange.adapter.out.persistence.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public abstract class GenericJdbcRepository<T> {

	protected final JdbcTemplate jdbcTemplate;
	private final RowMapper<T> rowMapper;

	protected GenericJdbcRepository(JdbcTemplate jdbcTemplate, RowMapper<T> rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	protected void executeUpdate(String sql, Object... args) {
		this.jdbcTemplate.update(sql, args);
	}

	protected T queryOne(String sql, Object... args) {
		return this.jdbcTemplate.queryForObject(sql, rowMapper, args);
	}

	protected List<T> queryList(String sql, Object... args) {
		return this.jdbcTemplate.query(sql, rowMapper, args);
	}

	protected List<T> queryListWithPagination(String sql, int limit, int offset, Object... args) {
		String paginatedSql = sql + " LIMIT ? OFFSET ?";
		Object[] params = this.appendToArray(args, limit, offset);
		return this.jdbcTemplate.query(paginatedSql, rowMapper, params);
	}

	protected Long insertAndReturnId(String sql, Object... args) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			this.setStatementParameters(ps, args);
			return ps;
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	private void setStatementParameters(PreparedStatement ps, Object[] args) throws SQLException {
		for (int i = 0; i < args.length; i++) {
			ps.setObject(i + 1, args[i]);
		}
	}

	private Object[] appendToArray(Object[] array, Object... newElements) {
		Object[] combined = new Object[array.length + newElements.length];
		System.arraycopy(array, 0, combined, 0, array.length);
		System.arraycopy(newElements, 0, combined, array.length, newElements.length);
		return combined;
	}
}
