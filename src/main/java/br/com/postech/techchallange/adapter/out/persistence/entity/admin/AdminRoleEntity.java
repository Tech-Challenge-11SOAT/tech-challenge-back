package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "admin_role")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRole;

	private String nome;

	private String descricao;

	@OneToMany(mappedBy = "adminRole", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdminUserRoleEntity> usuarios;
}
