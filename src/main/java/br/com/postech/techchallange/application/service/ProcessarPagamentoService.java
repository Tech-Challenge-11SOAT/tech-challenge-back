package br.com.postech.techchallange.application.service;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.port.in.ProcessarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.out.RedisPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessarPagamentoService implements ProcessarPagamentoUseCase {

	private final RedisPort<String, String> redisQrcodeService;

	@Override
	public void salvarQRCode(String idPedido, String qrCode) {
		redisQrcodeService.salvar(idPedido, qrCode);
	}

	@Override
	public String obterQRCode(String idPedido) {
		return redisQrcodeService.obter(idPedido)
				.orElseThrow(() -> new EntityNotFoundException("QRCode não encontrado"));
	}
}