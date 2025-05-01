package br.com.postech.techchallange.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cliente")
	private Long idCliente;

	@NotBlank(message = "O nome é obrigatório.")
	@Column(name = "nome_cliente", length = 100, nullable = false)
	private String nomeCliente;

	@NotBlank(message = "O e-mail é obrigatório.")
	@Email(message = "Formato de e-mail inválido.")
	@Column(name = "email_cliente", length = 100, nullable = false)
	private String emailCliente;

	@NotBlank(message = "O CPF é obrigatório.")
	@Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
	@Column(name = "cpf_cliente", length = 11, nullable = false)
	private String cpfCliente;
}
