package br.com.postech.techchallange.adapter.in.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ChangePasswordRequest {

	@NotBlank
	private String currentPassword;

	@NotBlank
	private String newPassword;
}
