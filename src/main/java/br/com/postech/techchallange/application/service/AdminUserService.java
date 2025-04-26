package br.com.postech.techchallange.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.model.AdminUser;
import br.com.postech.techchallange.domain.port.in.AutenticarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.CadastrarAdminUseCase;
import br.com.postech.techchallange.domain.port.in.ConsultarAdminUseCase;
import br.com.postech.techchallange.domain.port.out.AdminLogAcaoRepositoryPort;
import br.com.postech.techchallange.domain.port.out.AdminUserRepositoryPort;

@Service
public class AdminUserService implements CadastrarAdminUseCase, AutenticarAdminUseCase, ConsultarAdminUseCase {

	private final AdminUserRepositoryPort userRepository;
	private final AdminLogAcaoRepositoryPort logRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminUserService(AdminUserRepositoryPort userRepository, AdminLogAcaoRepositoryPort logRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.logRepository = logRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AdminUser cadastrar(AdminUser admin) {
		admin.setSenhaHash(passwordEncoder.encode(admin.getSenhaHash()));
		admin.setAtivo(true);
		admin.setDataCriacao(LocalDateTime.now());

		AdminUser adminSalvo = userRepository.salvar(admin);

		// Registrar log de cadastro
		logRepository.registrarLog(br.com.postech.techchallange.domain.model.AdminLogAcao.builder()
				.idAdmin(adminSalvo.getId())
				.acao("CADASTRO")
				.recursoAfetado("ADMIN_USER")
				.idRecurso(adminSalvo.getId())
				.dataAcao(LocalDateTime.now()).build());

		return adminSalvo;
	}

	@Override
	public AdminUser autenticar(String email, String senha) {
		AdminUser admin = userRepository.buscarPorEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

		if (!passwordEncoder.matches(senha, admin.getSenhaHash())) {
			throw new RuntimeException("Usuário ou senha inválidos");
		}

		return admin;
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
		return userRepository.listar();
	}
}
