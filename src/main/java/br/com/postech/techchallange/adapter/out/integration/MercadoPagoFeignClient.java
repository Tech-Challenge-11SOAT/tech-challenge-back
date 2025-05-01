package br.com.postech.techchallange.adapter.out.integration;

import br.com.postech.techchallange.adapter.out.integration.dto.QRCodeRequest;
import br.com.postech.techchallange.adapter.out.integration.dto.QRCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "mercado-pago", url = "https://api.mercadopago.com")
public interface MercadoPagoFeignClient {

	@PutMapping("/instore/orders/qr/seller/collectors/{user_id}/pos/{external_pos_id}/qrs")
	QRCodeResponse gerarQRCode(
			@RequestHeader("Authorization") String authorization,
			@PathVariable("user_id") String userId,
			@PathVariable("external_pos_id") String externalPosId,
			@RequestBody QRCodeRequest request
		);
}
