package br.com.postech.techchallange.adapter.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/health")
@Tag(name = "Saúde da Aplicação", description = "Verificação do estado de saúde da aplicação")
public class HealthController {

	@GetMapping
	@Operation(summary = "Verificar estado da aplicação", description = "Verifica se a aplicação está em funcionamento")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Aplicação está funcionando normalmente"),
			@ApiResponse(responseCode = "503", description = "Aplicação está indisponível") })
	public ResponseEntity<HealthStatus> checkHealth() {
		HealthStatus status = new HealthStatus("UP");
		return ResponseEntity.ok(status);
	}

	record HealthStatus(String status) {

	}
}
