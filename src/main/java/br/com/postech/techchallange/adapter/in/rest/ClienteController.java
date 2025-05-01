package br.com.postech.techchallange.adapter.in.rest;


import br.com.postech.techchallange.domain.model.Cliente;
import br.com.postech.techchallange.domain.port.in.GerenciarClienteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final GerenciarClienteUseCase clienteUseCase;

    public ClienteController(GerenciarClienteUseCase clienteUseCase) {
        this.clienteUseCase = clienteUseCase;
    }

    @Operation(summary = "Cadastrar um novo cliente")
    @PostMapping
    public Cliente cadastrar(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do cliente a ser cadastrado",
                    required = true)
            Cliente cliente) {
        return clienteUseCase.cadastrarCliente(cliente);
    }

    @Operation(summary = "Buscar um cliente pelo ID")
    @GetMapping("/{id}")
    public Cliente buscarClientePorId(@Parameter(description = "ID do cliente", example = "1") @PathVariable Long id) {
        return clienteUseCase.buscarCliente(id);
    }


    @Operation(summary = "Listar todos os clientes")
    @GetMapping
    public List<Cliente> listar() {
        return clienteUseCase.listarClientes();
    }


}
