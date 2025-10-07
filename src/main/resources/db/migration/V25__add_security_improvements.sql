-- Melhorias de segurança
-- Motivo: Falta de medidas de segurança no banco

ALTER TABLE admin_user 
ADD COLUMN last_login DATETIME NULL,
ADD COLUMN login_attempts INT NOT NULL DEFAULT 0,
ADD COLUMN locked_until DATETIME NULL,
ADD COLUMN password_changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN must_change_password BOOLEAN NOT NULL DEFAULT FALSE;

-- Criar tabela para logs de segurança
CREATE TABLE admin_security_log (
    id_log BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_admin BIGINT NOT NULL,
    acao VARCHAR(100) NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    data_acao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sucesso BOOLEAN NOT NULL,
    detalhes TEXT,
    FOREIGN KEY (id_admin) REFERENCES admin_user(id_admin)
);
