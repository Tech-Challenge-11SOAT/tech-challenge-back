package br.com.postech.techchallange.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.postech.techchallange.adapter.in.rest.request.AdminRegisterRequest;
import br.com.postech.techchallange.domain.exception.BusinessException;
import br.com.postech.techchallange.domain.model.AdminLogAcao;
import br.com.postech.techchallange.domain.model.AdminRole;
import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.AutenticarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.CadastrarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.ConsultarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.LogoutAdminUseCase;
import br.com.postech.techchallange.domain.port.out.AdminLogAcaoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.AdminRoleRepositoryPort;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;
import br.com.postech.techchallange.infra.security.TokenBlacklistService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminUserService implements CadastrarAdminUseCase, AutenticarAdminUseCase, ConsultarAdminUseCase, LogoutAdminUseCase {

	private final AdminUserRepositoryPort userRepository;
	private final AdminLogAcaoRepositoryPort logRepository;
	private final PasswordEncoder passwordEncoder;
	private final AdminRoleRepositoryPort adminRoleRepository;
	private final TokenBlacklistService tokenBlacklistService;

	@Override
	public AdminUser cadastrar(AdminRegisterRequest request) {
		AdminUser admin = new AdminUser();
		admin.setNome(request.getNome());
		admin.setEmail(request.getEmail());
		admin.setSenhaHash(request.getSenha());

		List<AdminRole> roles = this.getAdminRolesFiltered(request);

		admin.setRoles(roles);
		
		if (admin.getRoles() == null || admin.getRoles().isEmpty()) {
			throw new BusinessException("Um administrador precisa ter pelo menos uma role.");
		}

		admin.setSenhaHash(passwordEncoder.encode(admin.getSenhaHash()));
		admin.setAtivo(true);
		admin.setDataCriacao(LocalDateTime.now());

		AdminUser adminSalvo = userRepository.salvar(admin);
		
		admin.getRoles().forEach(role -> {
			adminRoleRepository.atribuirRole(adminSalvo.getId(), role.getId());
		});

		// Registrar log de cadastro
		logRepository.registrarLog(AdminLogAcao.builder()
				.idAdmin(adminSalvo.getId())
				.acao("CADASTRO")
				.recursoAfetado("ADMIN_USER")
				.idRecurso(adminSalvo.getId())
				.dataAcao(LocalDateTime.now())
				.build());

		adminSalvo.setRoles(admin.getRoles());
		adminSalvo.setSenhaHash(null);
		return adminSalvo;
	}

	@Override
	public AdminUser autenticar(String email, String senha) {
		AdminUser admin = userRepository.buscarPorEmail(email)
				.orElseThrow(() -> new BusinessException("Usuário ou senha inválidos"));

		if (!passwordEncoder.matches(senha, admin.getSenhaHash())) {
			throw new BusinessException("Usuário ou senha inválidos");
		}

		admin.setRoles(adminRoleRepository.listarRolesPorAdminId(admin.getId()));

		return admin;
	}

	@Override
	public void logout(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
			tokenBlacklistService.blacklistToken(token);
		}
	}

	@Override
	public AdminUser buscarPorId(Long id) {
		return userRepository.buscarPorId(id).orElseThrow(() -> new RuntimeException("Admin não encontrado"));
	}

	@Override
	public AdminUser buscarPorEmail(String email) {
		return userRepository.buscarPorEmail(email).orElseThrow(() -> new RuntimeException("Admin não encontrado"));
	}

	@Override
	public List<AdminUser> listar() {
		return userRepository.listar().stream()
				.map(AdminUser::semSenha)
				.toList();
	}
	
	private List<AdminRole> getAdminRolesFiltered(AdminRegisterRequest request) {
		List<AdminRole> roles = adminRoleRepository.listarTodos().stream()
				.filter(role -> request.getRolesIds()
				.contains(role.getId()))
				.toList();
		return roles;
	}
}
