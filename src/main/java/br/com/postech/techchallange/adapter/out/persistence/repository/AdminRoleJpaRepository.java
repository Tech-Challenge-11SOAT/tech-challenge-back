package br.com.postech.techchallange.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminRoleEntity;

public interface AdminRoleJpaRepository extends JpaRepository<AdminRoleEntity, Long> {
}
