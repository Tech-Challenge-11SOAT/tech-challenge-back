package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminLogAcaoEntity;
import br.com.postech.techchallange.adapter.out.persistence.entity.admin.AdminUserEntity;
import br.com.postech.techchallange.domain.model.AdminLogAcao;

public class AdminLogAcaoMapper {

	public static AdminLogAcaoEntity toEntity(AdminLogAcao domain, AdminUserEntity adminUser) {
		return AdminLogAcaoEntity.builder()
				.acao(domain.getAcao())
				.recursoAfetado(domain.getRecursoAfetado())
				.idRecurso(domain.getIdRecurso())
				.dataAcao(domain.getDataAcao())
				.adminUser(adminUser)
				.build();
	}
}
