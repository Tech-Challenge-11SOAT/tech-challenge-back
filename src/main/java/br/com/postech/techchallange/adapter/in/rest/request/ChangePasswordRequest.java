package br.com.postech.techchallange.adapter.in.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

	@NotBlank
	private String currentPassword;

	@NotBlank
	private String newPassword;
}
