-- Adicionando índices para melhorar performance
CREATE INDEX idx_produto_categoria ON produto(id_categoria);
CREATE INDEX idx_produto_imagem ON produto(id_imagem_url);
CREATE INDEX idx_pedido_produto_pedido ON pedido_produto(id_pedido);
CREATE INDEX idx_pedido_produto_produto ON pedido_produto(id_produto);