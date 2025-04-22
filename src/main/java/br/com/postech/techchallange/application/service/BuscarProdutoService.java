package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.domain.model.Produto;
import br.com.postech.techchallange.domain.port.in.BuscarProdutoUseCase;
import br.com.postech.techchallange.domain.port.out.ProdutoRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class BuscarProdutoService implements BuscarProdutoUseCase {

    private final ProdutoRepositoryPort produtoRepository;

    public BuscarProdutoService(ProdutoRepositoryPort produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Produto buscarPorId(Long id) {
        return produtoRepository.buscarPorId(id);
    }
}
