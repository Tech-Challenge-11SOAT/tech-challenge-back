-- Correção de inconsistências de tipos de dados
-- Motivo: id_status_pagamento em pagamento é BIGINT mas em status_pagamento é INT

ALTER TABLE status_pagamento 
MODIFY COLUMN id_status_pagamento BIGINT AUTO_INCREMENT;

ALTER TABLE status_pedido 
MODIFY COLUMN id_status_pedido BIGINT AUTO_INCREMENT;

ALTER TABLE cliente 
MODIFY COLUMN id_cliente BIGINT AUTO_INCREMENT;

ALTER TABLE pedido 
MODIFY COLUMN id_cliente BIGINT;

ALTER TABLE pedido 
MODIFY COLUMN id_status_pedido BIGINT;
