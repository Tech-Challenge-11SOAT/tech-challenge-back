# Diagrama Comparativo - Evolução do Banco de Dados

## Requisitos

Para visualizar o diagrama, deve ter a extensão: bierner.markdown-mermaid

---

## Estrutura original

```mermaid
erDiagram
    CLIENTE_ORIGINAL {
        int id_cliente PK
        varchar nome_cliente
        varchar email_cliente
        varchar cpf_cliente
    }
    
    PEDIDO_ORIGINAL {
        bigint id_pedido PK
        int id_cliente "❌ Sem FK"
        datetime data_pedido
        int id_status_pedido "❌ Sem FK"
        datetime data_status
        int fila_pedido
    }
    
    PRODUTO_ORIGINAL {
        bigint id_produto PK
        varchar nome
        text descricao
        decimal preco
        bigint id_imagem_url "❌ FK desnecessária"
        bigint id_categoria "✅ FK OK"
    }
    
    CATEGORIA_ORIGINAL {
        bigint id_categoria PK
        varchar nome
    }
    
    IMAGEM_URL_ORIGINAL {
        bigint id_imagem_url PK
        varchar imagem "❌ Tabela desnecessária"
    }
    
    PEDIDO_PRODUTO_ORIGINAL {
        bigint id_pedido_produto PK
        bigint id_pedido "✅ FK OK"
        bigint id_produto "✅ FK OK"
        int quantidade
    }
    
    PAGAMENTO_ORIGINAL {
        int id_pagamento PK
        bigint id_pedido "✅ FK OK"
        decimal valor_total
        varchar metodo_pagamento
        bigint id_status_pagamento "❌ Sem FK"
        datetime data_pagamento
    }
    
    STATUS_PEDIDO_ORIGINAL {
        int id_status_pedido PK "❌ Tipo inconsistente"
        varchar nome_status
    }
    
    STATUS_PAGAMENTO_ORIGINAL {
        int id_status_pagamento PK "❌ Tipo inconsistente"
        varchar nome_status
    }
    
    ADMIN_USER_ORIGINAL {
        bigint id_admin PK
        varchar nome
        varchar email
        varchar senha_hash
        boolean ativo
        datetime data_criacao
    }

    %% Relacionamentos ORIGINAIS (com problemas)
    CLIENTE_ORIGINAL ||--o{ PEDIDO_ORIGINAL : "❌ Sem FK"
    PEDIDO_ORIGINAL ||--o{ PEDIDO_PRODUTO_ORIGINAL : "✅ FK OK"
    PRODUTO_ORIGINAL ||--o{ PEDIDO_PRODUTO_ORIGINAL : "✅ FK OK"
    CATEGORIA_ORIGINAL ||--o{ PRODUTO_ORIGINAL : "✅ FK OK"
    IMAGEM_URL_ORIGINAL ||--o{ PRODUTO_ORIGINAL : "❌ Desnecessário"
    PEDIDO_ORIGINAL ||--o{ PAGAMENTO_ORIGINAL : "✅ FK OK"
    STATUS_PEDIDO_ORIGINAL ||--o{ PEDIDO_ORIGINAL : "❌ Sem FK"
    STATUS_PAGAMENTO_ORIGINAL ||--o{ PAGAMENTO_ORIGINAL : "❌ Sem FK"
```

---

## Estrutura melhorada

```mermaid
erDiagram
    CLIENTE_MELHORADO {
        bigint id_cliente PK "✅ Tipo padronizado"
        varchar nome_cliente
        varchar email_cliente
        varchar cpf_cliente
        datetime created_at "✅ Auditoria"
        datetime updated_at "✅ Auditoria"
        varchar created_by "✅ Auditoria"
        varchar updated_by "✅ Auditoria"
        boolean is_deleted "✅ Soft delete"
        datetime deleted_at "✅ Soft delete"
        varchar deleted_by "✅ Soft delete"
    }
    
    PEDIDO_MELHORADO {
        bigint id_pedido PK
        bigint id_cliente FK "✅ FK implementada"
        datetime data_pedido
        bigint id_status_pedido FK "✅ FK implementada"
        datetime data_status
        int fila_pedido
        datetime created_at "✅ Auditoria"
        datetime updated_at "✅ Auditoria"
        varchar created_by "✅ Auditoria"
        varchar updated_by "✅ Auditoria"
        boolean is_deleted "✅ Soft delete"
        datetime deleted_at "✅ Soft delete"
        varchar deleted_by "✅ Soft delete"
    }
    
    PRODUTO_MELHORADO {
        bigint id_produto PK
        varchar nome
        text descricao
        decimal preco
        bigint id_categoria FK "✅ FK mantida"
        bigint id_imagem_url FK "✅ FK para imagem"
        datetime created_at "✅ Auditoria"
        datetime updated_at "✅ Auditoria"
        varchar created_by "✅ Auditoria"
        varchar updated_by "✅ Auditoria"
        boolean is_deleted "✅ Soft delete"
        datetime deleted_at "✅ Soft delete"
        varchar deleted_by "✅ Soft delete"
    }
    
    CATEGORIA_MELHORADA {
        bigint id_categoria PK
        varchar nome
        datetime created_at "✅ Auditoria"
        datetime updated_at "✅ Auditoria"
        varchar created_by "✅ Auditoria"
        varchar updated_by "✅ Auditoria"
        boolean is_deleted "✅ Soft delete"
        datetime deleted_at "✅ Soft delete"
        varchar deleted_by "✅ Soft delete"
    }
    
    PEDIDO_PRODUTO_MELHORADO {
        bigint id_pedido_produto PK
        bigint id_pedido FK "✅ FK mantida"
        bigint id_produto FK "✅ FK mantida"
        int quantidade
    }
    
    PAGAMENTO_MELHORADO {
        int id_pagamento PK
        bigint id_pedido FK "✅ FK mantida"
        decimal valor_total
        varchar metodo_pagamento
        bigint id_status_pagamento FK "✅ FK implementada"
        datetime data_pagamento
        datetime created_at "✅ Auditoria"
        datetime updated_at "✅ Auditoria"
        varchar created_by "✅ Auditoria"
        varchar updated_by "✅ Auditoria"
        boolean is_deleted "✅ Soft delete"
        datetime deleted_at "✅ Soft delete"
        varchar deleted_by "✅ Soft delete"
    }
    
    STATUS_PEDIDO_MELHORADO {
        bigint id_status_pedido PK "✅ Tipo padronizado"
        varchar nome_status
    }
    
    STATUS_PAGAMENTO_MELHORADO {
        bigint id_status_pagamento PK "✅ Tipo padronizado"
        varchar nome_status
    }
    
    ADMIN_USER_MELHORADO {
        bigint id_admin PK
        varchar nome
        varchar email
        varchar senha_hash
        boolean ativo
        datetime data_criacao
        datetime last_login "✅ Segurança"
        int login_attempts "✅ Segurança"
        datetime locked_until "✅ Segurança"
        datetime password_changed_at "✅ Segurança"
        boolean must_change_password "✅ Segurança"
    }
    
    ADMIN_SECURITY_LOG {
        bigint id_log PK "✅ Nova tabela"
        bigint id_admin FK "✅ FK para admin"
        varchar acao "✅ Log de ações"
        varchar ip_address "✅ Rastreamento"
        text user_agent "✅ Rastreamento"
        datetime data_acao "✅ Timestamp"
        boolean sucesso "✅ Status da ação"
        text detalhes "✅ Detalhes extras"
    }
    
    ADMIN_SESSION {
        varchar id_session PK "✅ Nova tabela"
        bigint id_admin FK "✅ FK para admin"
        datetime created_at "✅ Timestamp"
        datetime expires_at "✅ Expiração"
        varchar ip_address "✅ Rastreamento"
        text user_agent "✅ Rastreamento"
        boolean is_active "✅ Status ativo"
    }
    
    IMAGEM_URL_MELHORADA {
        bigint id_imagem_url PK "✅ Mantida e melhorada"
        varchar imagem "✅ URL principal"
        varchar alt_text "✅ Acessibilidade"
        varchar thumbnail_url "✅ Thumbnail"
        bigint file_size "✅ Tamanho do arquivo"
        varchar mime_type "✅ Tipo MIME"
        int width "✅ Largura"
        int height "✅ Altura"
        datetime created_at "✅ Auditoria"
        datetime updated_at "✅ Auditoria"
        varchar created_by "✅ Auditoria"
        varchar updated_by "✅ Auditoria"
        boolean is_deleted "✅ Soft delete"
        datetime deleted_at "✅ Soft delete"
        varchar deleted_by "✅ Soft delete"
    }
    

    %% Relacionamentos MELHORADOS (todos com FK)
    CLIENTE_MELHORADO ||--o{ PEDIDO_MELHORADO : "✅ FK implementada"
    PEDIDO_MELHORADO ||--o{ PEDIDO_PRODUTO_MELHORADO : "✅ FK mantida"
    PRODUTO_MELHORADO ||--o{ PEDIDO_PRODUTO_MELHORADO : "✅ FK mantida"
    CATEGORIA_MELHORADA ||--o{ PRODUTO_MELHORADO : "✅ FK mantida"
    IMAGEM_URL_MELHORADA ||--o{ PRODUTO_MELHORADO : "✅ FK 1:1"
    PEDIDO_MELHORADO ||--o{ PAGAMENTO_MELHORADO : "✅ FK mantida"
    STATUS_PEDIDO_MELHORADO ||--o{ PEDIDO_MELHORADO : "✅ FK implementada"
    STATUS_PAGAMENTO_MELHORADO ||--o{ PAGAMENTO_MELHORADO : "✅ FK implementada"
    ADMIN_USER_MELHORADO ||--o{ ADMIN_SECURITY_LOG : "✅ FK implementada"
    ADMIN_USER_MELHORADO ||--o{ ADMIN_SESSION : "✅ FK implementada"
```
