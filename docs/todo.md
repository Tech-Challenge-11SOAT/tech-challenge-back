# TODO - Tech Challenge Fast Food

# FASE 1

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
  - [x] Buscar produtos por categoria ⚠️
  - [x] Fake checkout: enviar produtos escolhidos para a fila (finalização do pedido) ⚠️
  - [x] Listar os pedidos
  - [x] Swagger disponível para consumo das APIs
- [x] Banco de dados integrado

## 3. Fila de Pedidos
- [x] Fila implementada inicialmente apenas em banco de dados

## 4. Docker & Deploy
- [x] 1 instância para banco de dados ⚠️
- [x] 1 instância para aplicação ⚠️
- [x] Dockerfile configurado corretamente ⚠️
- [x] docker-compose.yml para subir o ambiente completo ⚠️
- [x] Boas práticas de segurança e inicialização rápida

## 5. Validação da POC
- [ ] Vídeo demonstrando a arquitetura localmente ⚠️
  - [ ] Postar no Youtube, Vimeo ou Google Drive/One Drive
  - [ ] Tornar o vídeo público ou não listado
  - [ ] Demonstrar execução da aplicação e banco via Docker Compose
  - [ ] Explicar informações relevantes do projeto e passos para funcionamento

---

# FASE 2

## Visão Geral Stack
- **Infra:** Terraform + DigitalOcean Kubernetes (DOKS)
- **CI/CD:** GitHub Actions
- **Testes:** Unitários via Maven
- **Container:** Docker com push para Docker Hub
- **Deploy:** kubectl apply automatizado via pipeline


## 1. Preparação e organização
- [x] Organizar estrutura do projeto:
  - [x] infra/terraform/ (infraestrutura DO)
  - [x] infra/k8s/ (manifests Kubernetes)
  - [x] .github/workflows/ (pipelines GitHub Actions)

- [ ] Criar repositório no Docker Hub para a imagem
- [ ] Criar e salvar secrets no GitHub:
  - [ ] DO_TOKEN (token da DigitalOcean)
  - [ ] DOCKER_USERNAME
  - [ ] DOCKER_PASSWORD

## 2. Provisionar cluster Kubernetes com Terraform
- [ ] Criar main.tf com:
  - [ ] provider da DO
  - [ ] recurso digitalocean_kubernetes_cluster
[ ] Adicionar terraform.tfvars com:
  - [ ] token
  - [ ] nome do cluster
- [ ] Testar terraform init, plan, apply localmente
- [ ] Salvar kubeconfig com doctl ou output do Terraform
- [ ] Confirmar acesso com kubectl get nodes
- [x] Adicionar remote backend se quiser versionar o state

## 3. Criar pipeline CI (ci.yml)
- [ ] Criar .github/workflows/ci.yml com:
  - [ ] Checkout do código
  - [ ] Setup do Java/Maven
  - [ ] Rodar mvn test (testes unitários)
  - [ ] Build da imagem Docker
  - [ ] Push para Docker Hub (latest e SHA)

## 4. Criar manifests Kubernetes
- [ ] Criar infra/k8s/ com:
  - [ ] deployment.yaml (app)
  - [ ] service.yaml (ClusterIP ou LoadBalancer)
  - [ ] configmap.yaml
  - [ ] secret.yaml
  - [ ] hpa.yaml (Horizontal Pod Autoscaler)
  - [ ] ingress.yaml (opcional, se usar domínio)

## 5. Criar pipeline CD (deploy.yml)
- [ ] Criar .github/workflows/deploy.yml com:
  - [ ] Checkout do código
  - [ ] doctl auth init com DO_TOKEN
  - [ ] doctl kubernetes cluster kubeconfig save tech-challenge
  - [ ] kubectl apply -f infra/k8s/

## 6. Documentação e entrega
- [ ] Criar README com:
  - [ ] Diagrama da arquitetura (infra + app)
  - [ ] Explicação dos componentes (Terraform, CI/CD, K8s)
  - [ ] Como rodar o projeto localmente
  - [ ] Como fazer deploy
  - [ ] Link do vídeo no YouTube
  - [ ] Link do Swagger ou Postman Collection
- [ ] Gravar vídeo demonstrando:
  - [ ] Fluxo de deploy
  - [ ] API funcionando
  - [ ] Cluster em execução
- [ ] Adicionar soat-architecture como colaborador privado no GitHub


> **Legenda:**
> - [x] Feito
> - [ ] Pendente
> - ⚠️ Atenção: item crítico para entrega