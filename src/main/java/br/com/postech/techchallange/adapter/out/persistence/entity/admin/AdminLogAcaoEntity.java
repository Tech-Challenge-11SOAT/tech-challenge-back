package br.com.postech.techchallange.adapter.out.persistence.entity.admin;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_log_acao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLogAcaoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idLog;

	@ManyToOne
	@JoinColumn(name = "id_admin")
	private AdminUserEntity adminUser;

	private String acao;

	private String recursoAfetado;

	private Long idRecurso;

	private LocalDateTime dataAcao;
}
