package br.com.postech.techchallange.adapter.in.rest;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.adapter.in.rest.request.AdminRegisterRequest;
import br.com.postech.techchallange.adapter.in.rest.request.ChangePasswordRequest;
import br.com.postech.techchallange.adapter.in.rest.request.LoginRequest;
import br.com.postech.techchallange.adapter.in.rest.request.RefreshTokenRequest;
import br.com.postech.techchallange.adapter.in.rest.response.TokenResponse;
import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.AdminRole;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.AlterarSenhaAdminUseCase;
import br.com.postech.techchallange.domain.port.in.AutenticarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.CadastrarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.ConsultarAdminUseCase;
import br.com.postech.techchallange.domain.port.out.AdminRoleRepositoryPort;
import br.com.postech.techchallange.infra.security.JwtProvider;
import br.com.postech.techchallange.infra.security.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
	private final TokenBlacklistService tokenBlacklistService;
	private final AdminRoleRepositoryPort adminRoleRepository;
	private final ConsultarAdminUseCase consultarUseCase;
	private final AlterarSenhaAdminUseCase alterarSenhaUseCase;
	private final JwtProvider jwtProvider;

	@PostMapping("/login")
	@Operation(
			summary = "Realizar login",
			description = "Autentica um administrador e retorna tokens de acesso e refresh."
		)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login realizado com sucesso."),
			@ApiResponse(responseCode = "400", description = "Credenciais inválidas.")
		})
	public TokenResponse login(@RequestBody LoginRequest request) {
		AdminUser admin = autenticarUseCase.autenticar(request.email(), request.senha());

		String accessToken = jwtProvider.generateAccessToken(admin);
		String refreshToken = jwtProvider.generateRefreshToken(admin);

		return new TokenResponse(accessToken, refreshToken);
	}

	@PostMapping("/refresh")
	@Operation(
			summary = "Renovar token de acesso",
			description = "Gera um novo access token usando um refresh token válido."
		)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Novo token de acesso gerado."),
			@ApiResponse(responseCode = "400", description = "Refresh token inválido ou expirado.")
		})
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
	@Operation(
			summary = "Registrar novo administrador",
			description = "Cadastra um novo administrador e associa roles a ele."
		)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Administrador registrado com sucesso."),
			@ApiResponse(responseCode = "400", description = "Erro de validação no cadastro.")
		})
	public AdminUser register(@RequestBody @Valid AdminRegisterRequest request) {
		AdminUser admin = new AdminUser();
		admin.setNome(request.getNome());
		admin.setEmail(request.getEmail());
		admin.setSenhaHash(request.getSenha());

		List<AdminRole> roles = adminRoleRepository.listarTodos().stream()
				.filter(role -> request.getRolesIds().contains(role.getId())).toList();

		admin.setRoles(roles);

		return cadastrarUseCase.cadastrar(admin);
	}

	@PostMapping("/change-password")
	@Operation(summary = "Alterar senha do administrador autenticado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Senha alterada com sucesso."),
			@ApiResponse(responseCode = "400", description = "Senha atual incorreta ou requisição inválida."),
			@ApiResponse(responseCode = "403", description = "Token inválido ou expirado.")
	})
	public void changePassword(@RequestBody @Valid ChangePasswordRequest request, HttpServletRequest servletRequest) {
		String token = this.extractToken(servletRequest);
		String email = jwtProvider.getEmailFromToken(token);

		alterarSenhaUseCase.alterarSenha(email, request.getCurrentPassword(), request.getNewPassword());
	}

	@PostMapping("/logout")
	@Operation(
			summary = "Realizar logout",
			description = "Realiza logout do usuário, invalidando o token atual."
		)
	@SecurityRequirement(name = "bearerAuth")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Logout realizado com sucesso."),
			@ApiResponse(responseCode = "403", description = "Token inválido ou expirado.")
		})
	public void logout(@RequestBody RefreshTokenRequest request) {
		tokenBlacklistService.blacklistToken(request.refreshToken());
	}
	
	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			throw new BusinessException("Token inválido ou não informado.");
		}
		return header.substring(7);
	}
}
