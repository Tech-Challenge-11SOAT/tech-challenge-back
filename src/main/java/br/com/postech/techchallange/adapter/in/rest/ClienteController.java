package br.com.postech.techchallange.adapter.in.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.domain.model.Cliente;
import br.com.postech.techchallange.domain.port.in.GerenciarClienteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/open/clientes")
@Tag(name = "Cliente", description = "Endpoints de cadastro e listagem de clientes")
public class ClienteController {

	private final GerenciarClienteUseCase clienteUseCase;

	public ClienteController(GerenciarClienteUseCase clienteUseCase) {
		this.clienteUseCase = clienteUseCase;
	}

	@Operation(summary = "Cadastrar um novo cliente", description = "Cria um novo cliente com nome, e-mail e CPF.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos ou faltantes", content = @Content)
		})
	@PostMapping
	public Cliente cadastrar(@Valid
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do cliente a ser cadastrado",
			required = true,
			content = @Content(schema = @Schema(implementation = Cliente.class),
			examples = @ExampleObject(name = "Exemplo de Cliente", value = "{ \"nomeCliente\": \"João da Silva\", \"emailCliente\": \"joao@email.com\", \"cpfCliente\": \"12345678901\" }")))
			@RequestBody Cliente cliente) {
		return clienteUseCase.cadastrarCliente(cliente);
	}

	@Operation(summary = "Buscar um cliente pelo ID", description = "Retorna os dados de um cliente pelo seu ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
		})
	@GetMapping("/{id}")
	public Cliente buscarClientePorId(@Parameter(description = "ID do cliente", example = "1") @PathVariable Long id) {
		return clienteUseCase.buscarCliente(id);
	}

	@Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
		})
	@GetMapping
	public List<Cliente> listar() {
		return clienteUseCase.listarClientes();
	}
}
