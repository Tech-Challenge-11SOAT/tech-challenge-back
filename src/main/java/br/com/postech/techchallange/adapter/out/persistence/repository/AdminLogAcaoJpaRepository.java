package br.com.postech.techchallange.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminLogAcaoEntity;

public interface AdminLogAcaoJpaRepository extends JpaRepository<AdminLogAcaoEntity, Long> {
}
