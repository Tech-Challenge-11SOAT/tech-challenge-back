package br.com.postech.techchallange.adapter.in.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VendaProdutoCategoriaDTO {

    public Long idProduto;
    public String nomeProduto;
    public Long idCategoria;
    public String nomeCategoria;
    public Integer quantidade;
    public BigDecimal valorTotal;
    public LocalDateTime dataPedido;

    public VendaProdutoCategoriaDTO(Long idProduto, String nomeProduto, Long idCategoria, String nomeCategoria,
            Integer quantidade, BigDecimal valorTotal, LocalDateTime dataPedido) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
        this.dataPedido = dataPedido;
    }
}
