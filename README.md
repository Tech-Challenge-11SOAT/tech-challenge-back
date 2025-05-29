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
- Mercado Pago (Integração de Pagamentos)
- Feign Client (Integração com APIs)
- Cache (Spring Cache)

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
```

## 🔐 Segurança

- Autenticação via JWT (JSON Web Tokens)
- Controle de acesso baseado em roles
- Endpoints protegidos
- Blacklist de tokens

## 🛠 Funcionalidades

- Cadastro e autenticação de usuários
- Gestão de produtos e categorias
- Sistema de pedidos
- Integração com gateway de pagamento (Mercado Pago)
- Fila de pedidos
- APIs documentadas com Swagger

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

2. Configure as variáveis de ambiente:
   - Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:
```env
MERCADO_PAGO_ACCESS_TOKEN=seu_token_aqui
MERCADO_PAGO_USER_ID=seu_user_id
MERCADO_PAGO_EXTERNAL_POS_ID=seu_pos_id
MERCADO_PAGO_NOTIFICATION_URL=url_de_notificacao
```

3. Execute o projeto com Docker Compose:
```bash
cd docker
docker-compose up -d
```

A aplicação estará disponível em:
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html
- Banco de dados MySQL: localhost:3307

## 📦 Estrutura do Projeto

```
/
├── docker/           # Arquivos Docker e docker-compose
├── docs/            # Documentação do projeto
├── scripts/         # Scripts SQL e utilitários
├── src/             # Código fonte
└── terraform/       # Configurações de infraestrutura IaC
```

## 🧪 Testes

Para executar os testes:

```bash
./mvnw test
```

## 📚 Documentação

A documentação da API está disponível através do:

- Swagger UI (quando a aplicação está em execução):
  http://localhost:8080/swagger-ui/index.html
  
- Documentação detalhada no Notion:
  https://www.notion.so/techchallange

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
