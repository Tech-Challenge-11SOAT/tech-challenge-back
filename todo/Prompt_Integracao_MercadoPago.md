
# Prompt Aprimorado: Integração com Mercado Pago em Sistema Hexagonal

## 🧠 Etapa 1: Análise do Sistema

Você é um **Analista e Desenvolvedor de Software Sênior** trabalhando em um sistema de **gerenciamento de pedidos** que adota **arquitetura hexagonal**.

Sua tarefa consiste em:

1. Analise **todo o repositório** para entender a organização atual do projeto.
2. Verifique os **módulos de domínio**, **casos de uso (application/service)**, **interfaces (adapters/inbound/outbound)** e as **estruturas de entrada/saída (DTOs)**.
3. Localize o endpoint de **checkout** que está atualmente na classe `PedidoController.java`.

## 🔗 Etapa 2: Integração com Mercado Pago

Você deverá integrar a seguinte API da **Order** do Mercado Pago ao endpoint de **checkout**:

```
curl -X POST https://api.mercadopago.com/v1/orders \
  -H 'accept: application/json' \
  -H 'content-type: application/json' \
  -H 'Authorization: Bearer TEST-3705789938524084-050520-e7dc1883bd54202c6eaf0b4e8561a0ff-2422158577' \
  -H 'X-Idempotency-Key: SOME_UNIQUE_VALUE' \
  -d '{
        "type": "online",
        "total_amount": "1000.00",
        "external_reference": "ext_ref_1234",
        "processing_mode": "automatic",
        "transactions": {
          "payments": [
            {
              "amount": "1000.00",
              "payment_method": {
                "id": "pix",
                "type": "bank_transfer"
              },
              "expiration_time": "P3Y6M4DT12H30M5S"
            }
          ]
        },
        "payer": {
          "email": "test@testuser.com"
        }
      }'
```

📄 Documentação:  
https://www.mercadopago.com.br/developers/pt/docs/checkout-api-v2/payment-integration/pix

## 🛠️ Requisitos Técnicos

- Realize a **integração via HTTP client** compatível com o projeto (RestTemplate, WebClient, Feign ou SDK oficial).
- A chamada à API deve ser feita **somente após todas as validações de checkout serem concluídas com sucesso**.
- O retorno da API do Mercado Pago deve ser **encapsulado em um DTO de resposta** e **adicionado à resposta final do endpoint** de checkout.
- Utilize um **X-Idempotency-Key único por chamada**, utilizando uma estratégia segura (ex: UUID).

## 📦 Organização esperada (Arquitetura Hexagonal)

1. **Domain layer**
   - Se necessário, adicione interfaces ou modelos no domínio para representar abstrações da integração.

2. **Application/Use Case**
   - Crie um caso de uso específico, como `CriarOrdemMercadoPagoUseCase`.
   - Garanta que ele seja orquestrado com os casos de uso de pedido já existentes.

3. **Adapter Outbound (Infra)**
   - Implemente um client HTTP em um pacote de `infra`, como `MercadoPagoOrderClient`.
   - Separe DTOs externos (request/response da API) dos internos.

4. **Adapter Inbound (Controller)**
   - Atualize o endpoint `checkout` em `PedidoController` para acionar o caso de uso.
   - Mantenha responsabilidade apenas de orquestração na controller.

## 🧼 Padrões de Código e Qualidade

- Siga **Clean Code** e **DDD tactical patterns**.
- Nomeie métodos, variáveis e classes com clareza e sem ambiguidade.
- Escreva **testes unitários e de integração** para validar a integração e possíveis falhas.
- Use **loggers para rastreabilidade**, especialmente na integração externa.
- Trate **erros e timeouts da API externa** de forma controlada e resiliente (por exemplo, com fallback, retry ou erro claro para o cliente).

## 🔐 Chaves para ambiente de testes

- **Public Key**: `TEST-7c47deeb-29c4-4eee-b120-f808731c9f23`
- **Access Token**: `TEST-3705789938524084-050520-e7dc1883bd54202c6eaf0b4e8561a0ff-2422158577`

> Assegure-se de **isolar essas chaves via `application.properties` ou variáveis de ambiente** para não expor valores sensíveis.

## 🧪 Extras (opcional)

- Valide estrutura da resposta com um contrato (`Contract Test`) ou JSON Schema.
- Implemente um DTO `OrderResponseDTO` e adicione ao `CheckoutResponseDTO`.
