package br.com.postech.techchallange.adapter.in.rest.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserResponse {
	private Long id;
	private String nome;
	private String email;
	private Boolean ativo;
	private LocalDateTime dataCriacao;
}
