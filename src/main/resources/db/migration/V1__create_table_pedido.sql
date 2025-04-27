CREATE TABLE pedido (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    data_pedido DATETIME NOT NULL,
    id_status_pedido INT NOT NULL,
    data_status DATETIME NOT NULL
);
