package br.com.postech.techchallange.adapter.in.rest.request;

import br.com.postech.techchallange.domain.enums.StatusPedidoEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtualizarPedidoRequest {
	
	@NotNull
	private Long id;
	private StatusPedidoEnum status;
}
