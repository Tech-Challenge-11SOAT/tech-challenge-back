# Tech Challenge Fast Food

Uma aplicação para gerenciamento de pedidos de fast food desenvolvida com Java Spring Boot, seguindo os princípios de Domain-Driven Design (DDD) e Arquitetura Hexagonal.

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Security + JWT
- MySQL 8
- Docker & Docker Compose
- Swagger/OpenAPI
- FlyWay (Migrations)
- JUnit (Testes)
- Feign Client (Integração com APIs)
- Cache (Spring Cache)
- **Mercado Pago API** (Integração de Pagamentos PIX)
- RestTemplate (Cliente HTTP)

## 📐 Arquitetura

O projeto utiliza Arquitetura Hexagonal (Ports and Adapters) com DDD, organizando o código nas seguintes camadas:

```
src/main/java/br/com/postech/techchallange/
├── adapter/
│   ├── in/         # Adaptadores de entrada (Controllers REST)
│   └── out/        # Adaptadores de saída (Repositories, External Services)
├── application/    # Casos de uso e serviços de aplicação
├── domain/        # Modelos de domínio e regras de negócio
│   ├── model/     # Entidades e objetos de valor
│   ├── port/      # Interfaces (ports) de entrada e saída
│   └── exception/ # Exceções de domínio
└── infra/        # Configurações e componentes de infraestrutura
    └── mercadopago/ # Integração com API do Mercado Pago
```

## 💳 Integração Mercado Pago

O sistema integra com a API do Mercado Pago para processamento de pagamentos via PIX, oferecendo:

### Funcionalidades

- **Criação automática de ordens de pagamento** durante o checkout
- **Geração de QR Code PIX** para pagamento instantâneo
- **Chave de idempotência única** para cada transação
- **Tratamento resiliente de erros** - não interrompe o fluxo principal
- **Logs detalhados** para rastreabilidade

### Fluxo de Integração

1. Cliente finaliza pedido via endpoint `/open/pedidos/checkout`
2. Sistema valida pedido e produtos
3. **Após validações bem-sucedidas**, integra automaticamente com Mercado Pago
4. Ordem de pagamento é criada na API do Mercado Pago
5. QR Code PIX é gerado e retornado na resposta
6. Cliente recebe dados completos do pedido + informações de pagamento

### Estrutura da Resposta

```json
{
  "idPedido": 1,
  "idPagamento": 1,
  "metodoPagamento": "PIX",
  "status": "RECEBIDO_NAO_PAGO",
  "numeroPedido": 1,
  "orderResponse": {
    "orderId": "order_123456",
    "externalReference": "pedido_1_1234567890",
    "totalAmount": 50.00,
    "status": "pending",
    "qrCode": "00020126580014br.gov.bcb.pix...",
    "qrCodeBase64": "iVBORw0KGgoAAAANSUhEUgAA...",
    "ticketUrl": "https://www.mercadopago.com.br/checkout/...",
    "dateCreated": "2024-01-15T10:30:00",
    "dateLastUpdated": "2024-01-15T10:30:00"
  }
}
```

### Configuração

As configurações do Mercado Pago estão no `application.properties`:

```properties
# Mercado Pago Configuration
mercadopago.api.base-url=https://api.mercadopago.com
mercadopago.api.access-token=TEST-3705789938524084-050520-e7dc1883bd54202c6eaf0b4e8561a0ff-2422158577
mercadopago.api.public-key=TEST-7c47deeb-29c4-4eee-b120-f808731c9f23
mercadopago.api.timeout=30000
```

### Arquitetura da Integração

```
PedidoController (Inbound Adapter)
    ↓
FakeCheckoutService (Application Service)
    ↓
CriarOrdemMercadoPagoService (Use Case)
    ↓
MercadoPagoOrderClient (Outbound Adapter)
    ↓
Mercado Pago API
```

## 🔐 Segurança

- Autenticação via JWT (JSON Web Tokens)
- Controle de acesso baseado em roles
- Endpoints protegidos
- Blacklist de tokens
- **Chaves de API isoladas** via variáveis de ambiente
- **X-Idempotency-Key único** para cada chamada ao Mercado Pago

## 🛠 Funcionalidades

### Administração
- Gerenciamento de usuários admin
- Cadastro e atribuição de roles
- Autenticação e refresh token

### Produtos
- CRUD completo de produtos
- Busca por categoria
- Listagem e filtros

### Clientes
- Cadastro de clientes
- Identificação por CPF
- Listagem e busca

### Pedidos e Pagamentos
- Criação de pedidos
- **Checkout integrado com Mercado Pago**
- **Geração automática de QR Code PIX**
- Acompanhamento de status
- Fila de pedidos
- Histórico de pedidos
- **Ordens de pagamento via API externa**

### Status e Monitoramento
- Status de pedidos
- Status de pagamentos
- Consulta de pagamentos
- **Logs de integração com Mercado Pago**

## 📡 APIs e Endpoints

O sistema possui uma collection do Postman completa com todos os endpoints organizados em pastas:

### Admin
- Login e registro
- Gerenciamento de usuários e roles
- Refresh token e logout

### Produtos
- CRUD de produtos
- Busca por categoria

### Clientes
- Cadastro e identificação
- Listagem e busca

### Pedidos
- Criação e checkout
- **Checkout com integração Mercado Pago** 🆕
- Listagem e status
- Busca por ID

### Pagamentos
- Criação de pagamentos
- **QR Code PIX automático** 🆕
- Consulta de status
- **Dados da ordem Mercado Pago** 🆕

### Status
- Status de pedidos
- Status de pagamentos
- Atualização de status

A collection do Postman está disponível em `/postman/11SOAT Pos-tech.postman_collection.json`.

## 🚀 Como Executar

### Pré-requisitos

- Docker
- Docker Compose
- Git

### Passos para Execução

1. Clone o repositório:
```bash
git clone <URL_DO_REPOSITORIO>
cd techchallange
```

2. Execute o projeto:

Opção 1 - Via scripts de automação (Recomendado):

**Para Windows:**
```bash
run.bat
```

**Para macOS/Linux:**
```bash
chmod +x run.sh  # Apenas na primeira vez
./run.sh
```

Os scripts irão automaticamente:
- Verificar se o Docker está em execução
- Verificar conectividade com Maven Central
- Parar e remover containers antigos
- Fazer o build e subir os containers (com retry automático)
- Aguardar a aplicação inicializar
- Abrir o Swagger automaticamente no navegador

Opção 2 - Via Docker Compose manualmente:
```bash
cd docker
docker-compose up -d
```

A aplicação estará disponível em:
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html
- Banco de dados MySQL: localhost:3307

### Testando a API

1. Importe a collection do Postman localizada em `/postman/11SOAT Pos-tech.postman_collection.json`
2. Execute os endpoints na seguinte ordem para um fluxo completo:
   - Cadastre um usuário admin (Admin > Register)
   - Faça login como admin (Admin > Login)
   - Cadastre produtos (Produto > Criar produto)
   - Cadastre um cliente (Cliente > Adicionar Cliente)
   - Crie um pedido (Pedido > Criar Pedido)
   - **Realize o checkout (Pedido > Checkout) - Agora com integração Mercado Pago** 🆕
   - **Observe o QR Code PIX gerado automaticamente** 🆕
   - Acompanhe o status (Status Pedidos > Listar todos os status)

## 📦 Estrutura do Projeto

```
/
├── docker/           # Arquivos Docker e docker-compose
├── docs/            # Documentação do projeto
├── postman/         # Collections do Postman
├── scripts/         # Scripts SQL e utilitários
├── src/             # Código fonte
│   └── main/java/.../infra/mercadopago/  # Integração Mercado Pago 🆕
└── terraform/       # Configurações de infraestrutura IaC
```

## 🧪 Testes

Para executar os testes:

```bash
./mvnw test
```

**Testes da integração Mercado Pago inclusos:**

- Testes unitários do `CriarOrdemMercadoPagoService`
- Testes de integração do `MercadoPagoOrderClient`
- Testes atualizados do `FakeCheckoutService` com Mercado Pago
- Testes de tratamento de erros e resiliência

## 📚 Documentação

A documentação da API está disponível através do:

- Swagger UI (quando a aplicação está em execução):
  http://localhost:8080/swagger-ui/index.html
  
- Collection do Postman:
  `/postman/11SOAT Pos-tech.postman_collection.json`

## ⚙️ Configuração do Ambiente de Desenvolvimento

1. Importe o projeto em sua IDE favorita
2. Configure o JDK 17
3. Execute o comando para instalar as dependências:
```bash
./mvnw clean install
```

4. Para desenvolvimento local sem Docker, configure o application-dev.properties com suas configurações locais

## 🤝 Contribuindo

1. Faça o fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
