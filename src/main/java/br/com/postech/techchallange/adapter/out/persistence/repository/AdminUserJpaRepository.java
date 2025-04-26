package br.com.postech.techchallange.adapter.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminUserEntity;

public interface AdminUserJpaRepository extends JpaRepository<AdminUserEntity, Long> {
	Optional<AdminUserEntity> findByEmail(String email);
}
