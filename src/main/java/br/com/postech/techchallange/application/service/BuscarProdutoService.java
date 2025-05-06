package br.com.postech.techchallange.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.in.BuscarProdutoUseCase;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;

@Service
public class BuscarProdutoService implements BuscarProdutoUseCase {

    private final ProdutoRepositoryPort produtoRepository;

    public BuscarProdutoService(ProdutoRepositoryPort produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.buscarPorId(id);
    }
}
