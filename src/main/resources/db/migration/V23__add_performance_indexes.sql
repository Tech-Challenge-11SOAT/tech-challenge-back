-- Adicionar índices para melhorar performance
-- Motivo: Falta de índices em campos frequentemente consultados

CREATE INDEX idx_cliente_email ON cliente(email_cliente);
CREATE INDEX idx_cliente_cpf ON cliente(cpf_cliente);

CREATE INDEX idx_pedido_cliente ON pedido(id_cliente);
CREATE INDEX idx_pedido_status ON pedido(id_status_pedido);
CREATE INDEX idx_pedido_data ON pedido(data_pedido);
CREATE INDEX idx_pedido_fila ON pedido(fila_pedido);
CREATE INDEX idx_pedido_status_data ON pedido(id_status_pedido, data_pedido);

CREATE INDEX idx_produto_categoria ON produto(id_categoria);
CREATE INDEX idx_produto_preco ON produto(preco);
CREATE INDEX idx_produto_categoria_preco ON produto(id_categoria, preco);

CREATE INDEX idx_pagamento_pedido ON pagamento(id_pedido);
CREATE INDEX idx_pagamento_status ON pagamento(id_status_pagamento);
CREATE INDEX idx_pagamento_data ON pagamento(data_pagamento);
CREATE INDEX idx_pagamento_metodo ON pagamento(metodo_pagamento);
CREATE INDEX idx_pagamento_status_data ON pagamento(id_status_pagamento, data_pagamento);

CREATE INDEX idx_pedido_produto_pedido ON pedido_produto(id_pedido);
CREATE INDEX idx_pedido_produto_produto ON pedido_produto(id_produto);
CREATE INDEX idx_pedido_produto_quantidade ON pedido_produto(quantidade);

CREATE INDEX idx_categoria_nome ON categoria(nome);

CREATE INDEX idx_admin_email ON admin_user(email);
CREATE INDEX idx_admin_ativo ON admin_user(ativo);

CREATE INDEX idx_log_admin ON admin_log_acao(id_admin);
CREATE INDEX idx_log_acao ON admin_log_acao(acao);
CREATE INDEX idx_log_recurso ON admin_log_acao(recurso_afetado);
CREATE INDEX idx_log_data ON admin_log_acao(data_acao);
CREATE INDEX idx_log_admin_data ON admin_log_acao(id_admin, data_acao);
