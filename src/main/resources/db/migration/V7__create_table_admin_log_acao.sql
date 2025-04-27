CREATE TABLE admin_log_acao (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    id_admin BIGINT NOT NULL,
    acao VARCHAR(255) NOT NULL,
    recurso_afetado VARCHAR(100) NOT NULL,
    id_recurso INT NOT NULL,
    data_acao DATETIME NOT NULL,
    FOREIGN KEY (id_admin) REFERENCES admin_user(id_admin)
);