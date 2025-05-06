CREATE TABLE produto (
    id_produto BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    id_imagem_url BIGINT,
    id_categoria BIGINT,
    CONSTRAINT fk_produto_imagem FOREIGN KEY (id_imagem_url) 
        REFERENCES imagem_url(id_imagem_url),
    CONSTRAINT fk_produto_categoria FOREIGN KEY (id_categoria) 
        REFERENCES categoria(id_categoria)
);