package br.com.postech.techchallange.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminRoleEntity;
import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminUserEntity;
import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminUserRoleEntity;
import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminUserRoleId;
import br.com.postech.techchallange.domain.model.AdminRole;
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
				.roles(
					entity.getRoles() != null
						? entity.getRoles().stream()
							.map(adminUserRole -> mapRoleEntityToDomain(adminUserRole.getRole()))
							.collect(Collectors.toList())
						: null
				)
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

		if (domain.getRoles() != null) {
			List<AdminUserRoleEntity> userRoles = domain.getRoles().stream()
				.map(role -> {
					AdminRoleEntity roleEntity = mapRoleDomainToEntity(role);
					AdminUserRoleEntity userRoleEntity = new AdminUserRoleEntity();
					userRoleEntity.setId(new AdminUserRoleId(adminEntity.getIdAdmin(), roleEntity.getId()));
					userRoleEntity.setAdminUser(adminEntity);
					userRoleEntity.setRole(roleEntity);
					return userRoleEntity;
				})
				.toList();

			adminEntity.setRoles(userRoles);
		}

		return adminEntity;
	}


	private static AdminRole mapRoleEntityToDomain(AdminRoleEntity entity) {
		return AdminRole.builder()
				.id(entity.getId())
				.nome(entity.getNome())
				.descricao(entity.getDescricao())
				.build();
	}

	private static AdminRoleEntity mapRoleDomainToEntity(AdminRole domain) {
		return AdminRoleEntity.builder()
				.id(domain.getId())
				.nome(domain.getNome())
				.descricao(domain.getDescricao())
				.build();
	}
}
