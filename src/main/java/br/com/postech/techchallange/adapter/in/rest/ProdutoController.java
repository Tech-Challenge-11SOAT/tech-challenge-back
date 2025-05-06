package br.com.postech.techchallange.adapter.in.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallange.adapter.in.rest.request.ProdutoRequest;
import br.com.postech.techchallange.adapter.in.rest.response.ProdutoResponse;
import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.in.BuscarProdutoUseCase;
import br.com.postech.techchallange.domain.port.in.ProdutoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/open/produtos")
@Tag(name = "Produtos", description = "API para gerenciamento de produtos")
public class ProdutoController {

	private final ProdutoUseCase produtoUseCase;
	private final BuscarProdutoUseCase buscarProdutoUseCase;

	public ProdutoController(ProdutoUseCase produtoUseCase, BuscarProdutoUseCase buscarProdutoUseCase) {
		this.produtoUseCase = produtoUseCase;
		this.buscarProdutoUseCase = buscarProdutoUseCase;
	}

	@PostMapping
	@Operation(summary = "Criar novo produto", description = "Registra um novo produto no sistema")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
				content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
		@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<ProdutoResponse> criarProduto(
			@Valid @RequestBody ProdutoRequest request) {
		Produto produto = produtoUseCase.criarProduto(request.toDomain());
		return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponse.fromDomain(produto));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto específico")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Produto encontrado",
				content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
		@ApiResponse(responseCode = "404", description = "Produto não encontrado"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<ProdutoResponse> buscarPorId(
			@Parameter(description = "ID do produto a ser buscado", required = true, example = "1")
			@PathVariable Long id) {
		Produto produto = buscarProdutoUseCase.buscarPorId(id).get();
		return ResponseEntity.ok(ProdutoResponse.fromDomain(produto));
	}

	@GetMapping
	@Operation(summary = "Listar todos os produtos", description = "Retorna uma lista com todos os produtos cadastrados")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
				content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<List<ProdutoResponse>> listarTodos() {
		List<Produto> produtos = produtoUseCase.listarTodosProdutos();
		return ResponseEntity.ok(produtos.stream().map(ProdutoResponse::fromDomain).toList());
	}

	@GetMapping("/categoria/{idCategoria}")
	@Operation(summary = "Buscar produtos por categoria", description = "Retorna produtos filtrados por categoria")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Produtos encontrados",
				content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<List<ProdutoResponse>> buscarPorCategoria(
			@Parameter(description = "ID da categoria para filtro", required = true, example = "1")
			@PathVariable Long idCategoria) {
		List<Produto> produtos = produtoUseCase.buscarPorCategoria(idCategoria);
		return ResponseEntity.ok(produtos.stream().map(ProdutoResponse::fromDomain).toList());
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
				content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
		@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
		@ApiResponse(responseCode = "404", description = "Produto não encontrado"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<ProdutoResponse> atualizarProduto(
			@Parameter(description = "ID do produto a ser atualizado", required = true, example = "1")
			@PathVariable Long id,
			@Valid @RequestBody ProdutoRequest request) {
		Produto produto = request.toDomain();
		produto.setId(id);
		Produto produtoAtualizado = produtoUseCase.atualizarProduto(id, produto);
		return ResponseEntity.ok(ProdutoResponse.fromDomain(produtoAtualizado));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir produto", description = "Remove um produto do sistema")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
		@ApiResponse(responseCode = "404", description = "Produto não encontrado"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<Void> deletarProduto(
			@Parameter(description = "ID do produto a ser excluído", required = true, example = "1")
			@PathVariable Long id) {
		produtoUseCase.deletarProduto(id);
		return ResponseEntity.noContent().build();
	}
}
