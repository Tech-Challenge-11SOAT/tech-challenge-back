package br.com.postech.techchallange.adapter.out.persistence.mapper;

import br.com.postech.techchallange.adapter.out.persistence.entity.ClienteEntity;
import br.com.postech.techchallange.domain.model.Cliente;

public class ClienteMapper {

    public static Cliente toDomain(ClienteEntity entity) {
        return new Cliente(
                entity.getIdCliente(),
                entity.getNomeCliente(),
                entity.getEmailCliente(),
                entity.getCpfCliente()
        );
    }

    public static ClienteEntity toEntity(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setIdCliente(cliente.getId());
        entity.setNomeCliente(cliente.getNomeCliente());
        entity.setEmailCliente(cliente.getEmailCliente());
        entity.setCpfCliente(cliente.getCpfCliente());
        return entity;
    }

}
