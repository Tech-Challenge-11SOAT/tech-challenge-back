-- Tabela pedido_produto (relacionamento N-N entre pedido e produto)
CREATE TABLE pedido_produto (
    id_pedido_produto BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    
    CONSTRAINT fk_pedido
    	FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    CONSTRAINT fk_produto
    	FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
);