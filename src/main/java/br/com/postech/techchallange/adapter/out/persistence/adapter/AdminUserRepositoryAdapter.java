package br.com.postech.techchallange.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminUserEntity;
import br.com.postech.techchallange.adapter.out.persistence.mapper.AdminUserMapper;
import br.com.postech.techchallange.adapter.out.persistence.repository.AdminUserJpaRepository;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminUserRepositoryAdapter implements AdminUserRepositoryPort {

	private final AdminUserJpaRepository jpaRepository;

	@Override
	@Transactional
	public AdminUser salvar(AdminUser admin) {
		AdminUserEntity entity = AdminUserMapper.toEntity(admin);

		AdminUserEntity saved = jpaRepository.save(entity);

		return AdminUserMapper.toDomain(saved);
	}

	@Override
	public Optional<AdminUser> buscarPorId(Long id) {
		return jpaRepository.findById(id)
				.map(AdminUserMapper::toDomain);
	}

	@Override
	public Optional<AdminUser> buscarPorEmail(String email) {
		return jpaRepository.findByEmail(email)
				.map(AdminUserMapper::toDomain);
	}

	@Override
	public List<AdminUser> listar() {
		return jpaRepository.findAll()
				.stream()
				.map(AdminUserMapper::toDomain)
				.toList();
	}
}
