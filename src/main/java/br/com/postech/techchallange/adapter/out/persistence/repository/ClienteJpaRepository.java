package br.com.postech.techchallange.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.postech.techchallange.adapter.out.persistence.entity.ClienteEntity;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {

}
