package br.com.postech.techchallange.adapter.in.rest.request;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegisterRequest {

	@NotBlank
	private String nome;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String senha;

	@NotNull
	private List<Long> rolesIds;
	
}
