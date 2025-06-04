package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.domain.model.Categoria;

public class CategoriaMapper {
	public static Categoria toDomain(br.com.postech.techchallange.adapter.out.persistence.entity.CategoriaEntity entity) {
		if (entity == null) {
			return null;
		}
		return new Categoria(entity.getId_categoria(), entity.getNome());
	}
}
