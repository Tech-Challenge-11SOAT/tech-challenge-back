package br.com.postech.techchallange.adapter.out.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produto")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;
    
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Long idImagemUrl;
    private Long idCategoria;

}