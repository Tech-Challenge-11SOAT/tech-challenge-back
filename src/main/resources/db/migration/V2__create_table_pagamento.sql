CREATE TABLE pagamento (
    id_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    metodo_pagamento VARCHAR(100) NOT NULL,
    id_status_pagamento BIGINT NOT NULL,
    data_pagamento DATETIME NOT NULL,
    CONSTRAINT PagamentoPedidoFK FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
);
