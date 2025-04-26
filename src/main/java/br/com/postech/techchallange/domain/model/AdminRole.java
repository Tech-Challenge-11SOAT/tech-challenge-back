package br.com.postech.techchallange.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRole {
	private Long id;
	private String nome;
	private String descricao;
}
