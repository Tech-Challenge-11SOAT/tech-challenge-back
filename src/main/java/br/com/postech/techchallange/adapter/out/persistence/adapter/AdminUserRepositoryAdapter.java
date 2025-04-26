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
	public Optional<AdminUser> buscarPorId(Long id) {
		return jpaRepository.findById(id).map(AdminUserMapper::toDomain).map(this::removeSenhaHash);
	}

	@Override
	public Optional<AdminUser> buscarPorEmail(String email) {
		return jpaRepository.findByEmail(email)
				.map(AdminUserMapper::toDomain);
	}

	@Override
	@Transactional
	public AdminUser salvar(AdminUser admin) {
		AdminUserEntity entity = AdminUserMapper.toEntity(admin);
		AdminUserEntity entitySalvo = jpaRepository.save(entity);
		return AdminUserMapper.toDomain(entitySalvo);
	}

	@Override
	public List<AdminUser> listar() {
		return jpaRepository.findAll()
				.stream()
				.map(AdminUserMapper::toDomain)
				.map(this::removeSenhaHash)
				.toList();
	}

	private AdminUser removeSenhaHash(AdminUser admin) {
		admin.setSenhaHash(null);
		return admin;
	}
}
