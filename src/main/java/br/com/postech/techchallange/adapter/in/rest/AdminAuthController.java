package br.com.postech.techchallange.adapter.in.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.adapter.in.rest.request.LoginDTO;
import br.com.postech.techchallange.adapter.in.rest.response.AdminUserResponse;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.AutenticarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.CadastrarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.LogoutAdminUseCase;
import br.com.postech.techchallange.infra.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin/auth")
@AllArgsConstructor
@Tag(name = "Admin - Autenticação", description = "Endpoints de login e cadastro de administradores")
public class AdminAuthController {

	private final AutenticarAdminUseCase autenticarUseCase;
	private final CadastrarAdminUseCase cadastrarUseCase;
	private final LogoutAdminUseCase logoutAdminUseCase;
	private final JwtProvider jwtProvider;

	@PostMapping("/login")
	@Operation(summary = "Autenticar um administrador")
	public Map<String, String> login(@RequestBody @Valid LoginDTO loginDTO) {
		AdminUser admin = autenticarUseCase.autenticar(loginDTO.email(), loginDTO.senha());

		String token = jwtProvider.generateToken(admin.getId(), admin.getEmail());

		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		return response;
	}

	@PostMapping("/register")
	@Operation(summary = "Cadastrar um novo administrador")
	public AdminUserResponse register(@RequestBody @Valid AdminUser adminUser) {
		AdminUser novoAdmin = cadastrarUseCase.cadastrar(adminUser);
		return AdminUserResponse.builder()
				.id(novoAdmin.getId())
				.nome(novoAdmin.getNome())
				.email(novoAdmin.getEmail())
				.ativo(novoAdmin.getAtivo())
				.dataCriacao(novoAdmin.getDataCriacao())
				.build();
	}

	@PostMapping("/logout")
	@Operation(summary = "Realizar logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		logoutAdminUseCase.logout(request.getHeader("Authorization"));
		return ResponseEntity.ok("Logout realizado com sucesso.");
	}
}
