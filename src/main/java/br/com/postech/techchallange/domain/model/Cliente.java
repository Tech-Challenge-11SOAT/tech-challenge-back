package br.com.postech.techchallange.domain.model;

public class Cliente {

    //Atributos -------------------------------------------------

    private Long id;
    private String nomeCliente;
    private String emailCliente;
    private String cpfCliente;

    //Contrutor -------------------------------------------------

    public Cliente(Long id, String nomeCliente, String emailCliente, String cpfCliente) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
        this.cpfCliente = cpfCliente;
    }

    //Getters ---------------------------------------------------

    public Long getId() {
        return id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    //Setters ---------------------------------------------------

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }
}
