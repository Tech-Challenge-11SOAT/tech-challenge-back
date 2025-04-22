package br.com.postech.techchallange.adapter.in.rest;

import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.in.BuscarProdutoUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	private final BuscarProdutoUseCase buscarProdutoUseCase;

	public ProdutoController(BuscarProdutoUseCase buscarProdutoUseCase) {
		this.buscarProdutoUseCase = buscarProdutoUseCase;
	}

	@GetMapping("/{id}")
	public Produto buscarPorId(@PathVariable Long id) {
		return buscarProdutoUseCase.buscarPorId(id);
	}
}
