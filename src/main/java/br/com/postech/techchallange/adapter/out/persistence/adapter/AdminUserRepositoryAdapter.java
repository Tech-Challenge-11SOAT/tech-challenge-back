package br.com.postech.techchallange.adapter.out.persistence.adapter;

import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;
import br.com.postech.techchallange.adapter.out.persistence.mapper.AdminUserMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.AdminUserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AdminUserRepositoryAdapter implements AdminUserRepositoryPort {

	private final AdminUserJpaRepository repository;

	public AdminUserRepositoryAdapter(AdminUserJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public AdminUser salvar(AdminUser admin) {
		return AdminUserMapper.toDomain(repository.save(AdminUserMapper.toEntity(admin)));
	}

	@Override
	public Optional<AdminUser> buscarPorId(Long id) {
		return repository.findById(id).map(AdminUserMapper::toDomain);
	}

	@Override
	public Optional<AdminUser> buscarPorEmail(String email) {
		return repository.findByEmail(email).map(AdminUserMapper::toDomain);
	}

	@Override
	public List<AdminUser> listar() {
		return repository.findAll().stream().map(AdminUserMapper::toDomain).collect(Collectors.toList());
	}
}
