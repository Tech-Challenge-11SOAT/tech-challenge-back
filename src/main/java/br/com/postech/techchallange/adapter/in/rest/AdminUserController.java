package br.com.postech.techchallange.adapter.in.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.ConsultarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.ToggleAdminUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - Usuários", description = "Endpoints para gerenciar administradores")
public class AdminUserController {

	private final ConsultarAdminUseCase consultarUseCase;
	private final ToggleAdminUseCase toggleAdminUseCase;

	@GetMapping
	@Operation(summary = "Listar todos os administradores")
	public List<AdminUser> listar() {
		return consultarUseCase.listar();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar um administrador pelo ID")
	public AdminUser buscarPorId(@PathVariable Long id) {
		return consultarUseCase.buscarPorId(id);
	}

	@PatchMapping("/{id}/inactivate")
	@Operation(summary = "Inativar um administrador")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Administrador inativado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Administrador não encontrado.")
		})
	public void inativar(@PathVariable Long id) {
		this.toggleAdminUseCase.toggle(id);
	}
	
	@PatchMapping("/{id}/activate")
	@Operation(summary = "Ativar um administrador")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Administrador inativado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Administrador não encontrado.")
		})
	public void ativar(@PathVariable Long id) {
		this.toggleAdminUseCase.toggle(id);
	}
}
