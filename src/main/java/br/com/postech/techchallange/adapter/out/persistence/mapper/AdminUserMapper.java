package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminUserEntity;
import br.com.postech.techchallange.domain.model.AdminUser;

public class AdminUserMapper {

	public static AdminUser toDomain(AdminUserEntity entity) {
		if (entity == null) {
			return null;
		}

		return AdminUser.builder()
				.id(entity.getIdAdmin())
				.nome(entity.getNome())
				.email(entity.getEmail())
				.senhaHash(entity.getSenhaHash())
				.ativo(entity.getAtivo())
				.dataCriacao(entity.getDataCriacao())
				.build();
	}

	public static AdminUserEntity toEntity(AdminUser domain) {
		if (domain == null) {
			return null;
		}

		AdminUserEntity adminEntity = AdminUserEntity.builder()
				.idAdmin(domain.getId())
				.nome(domain.getNome())
				.email(domain.getEmail())
				.senhaHash(domain.getSenhaHash())
				.ativo(domain.getAtivo())
				.dataCriacao(domain.getDataCriacao())
				.build();

		return adminEntity;
	}
}
