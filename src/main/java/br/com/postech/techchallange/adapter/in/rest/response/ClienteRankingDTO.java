package br.com.postech.techchallange.adapter.in.rest.response;

public class ClienteRankingDTO {

    public Long idCliente;
    public String nomeCliente;
    public String emailCliente;
    public Long totalPedidos;

    public ClienteRankingDTO(Long idCliente, String nomeCliente, String emailCliente, Long totalPedidos) {
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
        this.totalPedidos = totalPedidos;
    }
}
