package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@SuppressWarnings("serial")
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdminUserRoleId implements Serializable {

	@Column(name = "id_admin")
	private Long idAdmin;

	@Column(name = "id_role")
	private Long idRole;
}
