package br.com.postech.techchallange.adapter.in.rest;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.adapter.in.rest.request.AdminRegisterRequest;
import br.com.postech.techchallange.adapter.in.rest.request.LoginRequest;
import br.com.postech.techchallange.adapter.in.rest.request.RefreshTokenRequest;
import br.com.postech.techchallange.adapter.in.rest.response.AdminUserResponse;
import br.com.postech.techchallange.adapter.in.rest.response.TokenResponse;
import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.AdminRole;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.AutenticarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.CadastrarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.ConsultarAdminUseCase;
import br.com.postech.techchallange.domain.port.out.AdminRoleRepositoryPort;
import br.com.postech.techchallange.infra.security.JwtProvider;
import br.com.postech.techchallange.infra.security.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin/auth")
@AllArgsConstructor
@Tag(name = "Admin - Autenticação", description = "Endpoints de login e cadastro de administradores")
public class AdminAuthController {

	private final AutenticarAdminUseCase autenticarUseCase;
	private final CadastrarAdminUseCase cadastrarUseCase;
	private final TokenBlacklistService tokenBlacklistService;
	private final AdminRoleRepositoryPort adminRoleRepository;
	private final ConsultarAdminUseCase consultarUseCase;
	private final JwtProvider jwtProvider;

	@PostMapping("/login")
	@Operation(summary = "Autenticar um administrador.")
	public TokenResponse login(@RequestBody LoginRequest request) {
		AdminUser admin = autenticarUseCase.autenticar(request.email(), request.senha());

		String accessToken = jwtProvider.generateAccessToken(admin);
		String refreshToken = jwtProvider.generateRefreshToken(admin);

		return new TokenResponse(accessToken, refreshToken);
	}
	
	@PostMapping("/refresh")
	@Operation(summary = "Renovar token de acesso (access token) de um administrador.")
	public TokenResponse refresh(@RequestBody RefreshTokenRequest request) {
		if (!jwtProvider.validateToken(request.refreshToken())) {
			throw new BusinessException("Refresh token inválido ou expirado.");
		}

		String email = jwtProvider.getEmailFromToken(request.refreshToken());
		AdminUser admin = consultarUseCase.buscarPorEmail(email);

		String newAccessToken = jwtProvider.generateAccessToken(admin);
		String newRefreshToken = jwtProvider.generateRefreshToken(admin);

		return new TokenResponse(newAccessToken, newRefreshToken);
	}

	@PostMapping("/register")
	@Operation(summary = "Registrar um novo administrador")
	public AdminUser register(@RequestBody @Valid AdminRegisterRequest request) {
		AdminUser admin = new AdminUser();
		admin.setNome(request.getNome());
		admin.setEmail(request.getEmail());
		admin.setSenhaHash(request.getSenha());

		List<AdminRole> roles = adminRoleRepository.listarTodos().stream()
				.filter(role -> request.getRolesIds().contains(role.getId()))
				.toList();

		admin.setRoles(roles);

		return cadastrarUseCase.cadastrar(admin);
	}

	@PostMapping("/inactivate")
	@Operation(summary = "Inativar uma conta de administrador")
	public AdminUserResponse inactivate(@RequestBody @Valid AdminUser adminUser) {
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
	@Operation(summary = "Logout de administrador")
	public void logout(@RequestBody RefreshTokenRequest request) {
		tokenBlacklistService.blacklistToken(request.refreshToken());
	}
}
