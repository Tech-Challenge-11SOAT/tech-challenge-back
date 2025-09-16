-- Otimizar estrutura de imagens mantendo normalização com relação 1:1
-- Motivo: Estrutura de imagens pode ser otimizada mantendo normalização

ALTER TABLE imagem_url 
ADD COLUMN created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
ADD COLUMN created_by VARCHAR(100),
ADD COLUMN updated_by VARCHAR(100),
ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN deleted_at DATETIME NULL,
ADD COLUMN deleted_by VARCHAR(100) NULL,
ADD COLUMN alt_text VARCHAR(255) NULL,
ADD COLUMN thumbnail_url VARCHAR(500) NULL,
ADD COLUMN file_size BIGINT NULL,
ADD COLUMN mime_type VARCHAR(100) NULL,
ADD COLUMN width INT NULL,
ADD COLUMN height INT NULL;

CREATE INDEX idx_imagem_url_mime_type ON imagem_url(mime_type);
