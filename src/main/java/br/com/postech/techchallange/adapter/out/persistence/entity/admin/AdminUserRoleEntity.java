package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_user_role")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserRoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_admin")
	private AdminUserEntity adminUser;

	@ManyToOne
	@JoinColumn(name = "id_role")
	private AdminRoleEntity adminRole;
}
