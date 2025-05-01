package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.port.in.ProcessarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.out.RedisPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
				.orElseThrow(() -> new RuntimeException("QRCode não encontrado"));
	}
}