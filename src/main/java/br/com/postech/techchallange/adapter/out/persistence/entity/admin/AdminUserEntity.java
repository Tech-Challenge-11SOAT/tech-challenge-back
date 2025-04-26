package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "admin_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAdmin;

	private String nome;

	private String email;

	private String senhaHash;

	private Boolean ativo;

	private LocalDateTime dataCriacao;

	@OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdminUserRoleEntity> roles;
}
