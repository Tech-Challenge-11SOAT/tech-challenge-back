# TODO - Tech Challenge Fast Food

## 1. Documentação do Sistema (DDD)
- [x] Event Storming completo, incluindo todos os tipos de diagrama da aula 6 (DDD)
- [x] Linguagem ubíqua aplicada
- [x] Fluxos documentados:
  - [x] Realização do pedido e pagamento
  - [x] Preparação e entrega do pedido
- [x] Desenhos seguem os padrões apresentados

## 2. Backend (Monolito)
- [x] Arquitetura hexagonal implementada
- [x] APIs:
  - [x] Cadastro do Cliente
  - [x] Identificação do Cliente via CPF
  - [x] Criar, editar e remover produtos
  - [ ] Buscar produtos por categoria ⚠️
  - [x] Fake checkout: enviar produtos escolhidos para a fila (finalização do pedido) ⚠️
  - [x] Listar os pedidos
  - [x] Swagger disponível para consumo das APIs
- [x] Banco de dados integrado

## 3. Fila de Pedidos
- [x] Fila implementada inicialmente apenas em banco de dados

## 4. Docker & Deploy
- [ ] 1 instância para banco de dados ⚠️
- [ ] 1 instância para aplicação ⚠️
- [ ] Dockerfile configurado corretamente ⚠️
- [ ] docker-compose.yml para subir o ambiente completo ⚠️
- [ ] Boas práticas de segurança e inicialização rápida

## 5. Validação da POC
- [ ] Vídeo demonstrando a arquitetura localmente ⚠️
  - [ ] Postar no Youtube, Vimeo ou Google Drive/One Drive
  - [ ] Tornar o vídeo público ou não listado
  - [ ] Demonstrar execução da aplicação e banco via Docker Compose
  - [ ] Explicar informações relevantes do projeto e passos para funcionamento

---

> **Legenda:**
> - [x] Feito
> - [ ] Pendente
> - ⚠️ Atenção: item crítico para entrega