-- Adicionar constraints de integridade referencial
-- Motivo: Faltam foreign keys para garantir integridade dos dados

ALTER TABLE pedido 
ADD CONSTRAINT fk_pedido_cliente 
FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente);

ALTER TABLE pedido 
ADD CONSTRAINT fk_pedido_status 
FOREIGN KEY (id_status_pedido) REFERENCES status_pedido(id_status_pedido);

ALTER TABLE pagamento 
ADD CONSTRAINT fk_pagamento_status 
FOREIGN KEY (id_status_pagamento) REFERENCES status_pagamento(id_status_pagamento);
