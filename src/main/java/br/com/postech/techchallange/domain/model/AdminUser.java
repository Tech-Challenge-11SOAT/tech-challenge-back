package br.com.postech.techchallange.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {
	private Long id;
	private String nome;
	private String email;

	private String senhaHash;

	private Boolean ativo;
	private LocalDateTime dataCriacao;
	private List<AdminRole> roles;
	
	public AdminUser semSenha() {
		return new AdminUser(this.id, this.nome, this.email, null, this.ativo, this.dataCriacao, this.roles);
	}
}
