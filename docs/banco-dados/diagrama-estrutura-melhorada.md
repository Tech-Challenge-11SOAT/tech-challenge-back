# Diagrama da Estrutura Melhorada do Banco de Dados

## Requisitos

Para visualizar o diagrama, deve ter a extensão: bierner.markdown-mermaid

## Modelo Entidade-Relacionamento (ER) - Estrutura melhorada

```mermaid
erDiagram
    CLIENTE {
        bigint id_cliente PK
        varchar nome_cliente
        varchar email_cliente
        varchar cpf_cliente
        datetime created_at
        datetime updated_at
        varchar created_by
        varchar updated_by
        boolean is_deleted
        datetime deleted_at
        varchar deleted_by
    }
    
    PEDIDO {
        bigint id_pedido PK
        bigint id_cliente FK
        datetime data_pedido
        bigint id_status_pedido FK
        datetime data_status
        int fila_pedido
        datetime created_at
        datetime updated_at
        varchar created_by
        varchar updated_by
        boolean is_deleted
        datetime deleted_at
        varchar deleted_by
    }
    
    PRODUTO {
        bigint id_produto PK
        varchar nome
        text descricao
        decimal preco
        bigint id_categoria FK
        varchar imagem_url
        datetime created_at
        datetime updated_at
        varchar created_by
        varchar updated_by
        boolean is_deleted
        datetime deleted_at
        varchar deleted_by
    }
    
    CATEGORIA {
        bigint id_categoria PK
        varchar nome
        datetime created_at
        datetime updated_at
        varchar created_by
        varchar updated_by
        boolean is_deleted
        datetime deleted_at
        varchar deleted_by
    }
    
    PEDIDO_PRODUTO {
        bigint id_pedido_produto PK
        bigint id_pedido FK
        bigint id_produto FK
        int quantidade
    }
    
    PAGAMENTO {
        int id_pagamento PK
        bigint id_pedido FK
        decimal valor_total
        varchar metodo_pagamento
        bigint id_status_pagamento FK
        datetime data_pagamento
        datetime created_at
        datetime updated_at
        varchar created_by
        varchar updated_by
        boolean is_deleted
        datetime deleted_at
        varchar deleted_by
    }
    
    STATUS_PEDIDO {
        bigint id_status_pedido PK
        varchar nome_status
    }
    
    STATUS_PAGAMENTO {
        bigint id_status_pagamento PK
        varchar nome_status
    }
    
    ADMIN_USER {
        bigint id_admin PK
        varchar nome
        varchar email
        varchar senha_hash
        boolean ativo
        datetime data_criacao
        datetime last_login
        int login_attempts
        datetime locked_until
        datetime password_changed_at
        boolean must_change_password
    }
    
    ADMIN_ROLE {
        bigint id PK
        varchar nome
        varchar descricao
    }
    
    ADMIN_USER_ROLE {
        bigint id_admin FK
        bigint id_role FK
    }
    
    ADMIN_LOG_ACAO {
        int id_log PK
        bigint id_admin FK
        varchar acao
        varchar recurso_afetado
        int id_recurso
        datetime data_acao
    }
    
    ADMIN_SECURITY_LOG {
        bigint id_log PK
        bigint id_admin FK
        varchar acao
        varchar ip_address
        text user_agent
        datetime data_acao
        boolean sucesso
        text detalhes
    }
    
    ADMIN_SESSION {
        varchar id_session PK
        bigint id_admin FK
        datetime created_at
        datetime expires_at
        varchar ip_address
        text user_agent
        boolean is_active
    }

    %% Relacionamentos Principais
    CLIENTE ||--o{ PEDIDO : "faz"
    PEDIDO ||--o{ PEDIDO_PRODUTO : "contém"
    PRODUTO ||--o{ PEDIDO_PRODUTO : "está_em"
    CATEGORIA ||--o{ PRODUTO : "categoriza"
    PEDIDO ||--o{ PAGAMENTO : "tem"
    STATUS_PEDIDO ||--o{ PEDIDO : "define_status"
    STATUS_PAGAMENTO ||--o{ PAGAMENTO : "define_status"
    
    %% Relacionamentos Administrativos
    ADMIN_USER ||--o{ ADMIN_USER_ROLE : "tem"
    ADMIN_ROLE ||--o{ ADMIN_USER_ROLE : "atribuído_a"
    ADMIN_USER ||--o{ ADMIN_LOG_ACAO : "executa"
    ADMIN_USER ||--o{ ADMIN_SECURITY_LOG : "registra"
    ADMIN_USER ||--o{ ADMIN_SESSION : "possui"
```
