package br.com.postech.techchallange.adapter.in.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.ConsultarAdminUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/admin/users")
@Tag(name = "Admin - Usuários", description = "Endpoints para gerenciar administradores")
public class AdminUserController {

	private final ConsultarAdminUseCase consultarUseCase;

	public AdminUserController(ConsultarAdminUseCase consultarUseCase) {
		this.consultarUseCase = consultarUseCase;
	}

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
}
