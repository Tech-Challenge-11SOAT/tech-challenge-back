package br.com.postech.techchallange.adapter.in.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.domain.model.AdminRole;
import br.com.postech.techchallange.domain.port.in.AtribuirRoleUseCase;
import br.com.postech.techchallange.domain.port.in.CadastrarRoleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin/roles")
@AllArgsConstructor
@Tag(name = "Admin - Roles", description = "Gerenciamento de roles dos administradores")
public class AdminRoleController {

	private final CadastrarRoleUseCase cadastrarRoleUseCase;
	private final AtribuirRoleUseCase atribuirRoleUseCase;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	@Operation(summary = "Cadastrar nova role")
	public AdminRole cadastrar(@RequestBody AdminRole role) {
		return cadastrarRoleUseCase.cadastrar(role);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{idAdmin}/atribuir/{idRole}")
	@Operation(summary = "Atribuir uma role a um admin")
	public void atribuirRole(@PathVariable Long idAdmin, @PathVariable Long idRole) {
		atribuirRoleUseCase.atribuirRole(idAdmin, idRole);
	}
}
