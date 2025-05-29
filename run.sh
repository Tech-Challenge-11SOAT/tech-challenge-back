#!/bin/bash

# Cores e formatação
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
WHITE='\033[0;37m'
NC='\033[0m' # No Color

echo "==========================="
echo -e "${GREEN}🚀 Iniciando ambiente DEV...${NC}"
echo "==========================="

# Verificar se o Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Docker não está em execução. Por favor, inicie o Docker.${NC}"
    exit 1
fi

# Ir para a pasta docker
cd "$(dirname "$0")/docker" || {
    echo -e "${RED}❌ Erro ao acessar a pasta docker${NC}"
    exit 1
}

# Verificar conectividade com Maven Central
echo -e "\n${BLUE}🌐 Verificando conexão com Maven Central...${NC}"
if ! curl -s -m 10 https://repo.maven.apache.org/maven2 > /dev/null; then
    echo -e "${RED}❌ Não foi possível conectar ao Maven Central. Verifique sua conexão com a internet.${NC}"
    exit 1
fi

# Parar e remover containers e volumes anteriores
echo -e "\n${YELLOW}🔁 Parando containers antigos e removendo volumes...${NC}"
docker-compose down -v

# Subir ambiente com build
echo -e "\n${CYAN}🔨 Iniciando build e subida dos containers...${NC}"
echo -e "${YELLOW}⏳ Isso pode demorar alguns minutos na primeira execução...${NC}"
echo -e "${BLUE}📦 Baixando dependências e construindo a aplicação...${NC}"

# Tentar até 3 vezes em caso de falha
retries=0
while [ $retries -lt 3 ]; do
    if docker-compose up --build -d; then
        break
    else
        retries=$((retries + 1))
        if [ $retries -lt 3 ]; then
            echo -e "${YELLOW}⚠️ Falha no build. Tentativa $retries de 3...${NC}"
            sleep 5
        else
            echo -e "${RED}❌ Erro ao executar docker-compose${NC}"
            exit 1
        fi
    fi
done

# Aguardar a aplicação inicializar
echo -e "\n${BLUE}🔄 Aguardando a aplicação inicializar...${NC}"
echo -e "${YELLOW}⏳ Isso pode levar alguns instantes...${NC}"

# Aguardar até 60 segundos pela aplicação
for i in {1..12}; do
    sleep 5
    if curl -s http://localhost:8080/actuator/health > /dev/null; then
        echo -e "\n${GREEN}✅ Ambiente DEV iniciado com sucesso!${NC}"
        echo -e "\n${CYAN}📍 Endpoints disponíveis:${NC}"
        echo -e "${WHITE}   API:${NC} http://localhost:8080"
        echo -e "${WHITE}   Swagger:${NC} http://localhost:8080/swagger-ui/index.html"
        echo -e "${WHITE}   Banco de dados:${NC} localhost:3307"
        echo

        # Abrir navegador baseado no sistema operacional
        case "$(uname -s)" in
            Darwin*)    open http://localhost:8080/swagger-ui/index.html ;; # macOS
            Linux*)     xdg-open http://localhost:8080/swagger-ui/index.html ;; # Linux
            MINGW*|CYGWIN*|MSYS*) start http://localhost:8080/swagger-ui/index.html ;; # Windows
        esac
        exit 0
    fi
done

echo -e "${RED}❌ Timeout ao aguardar a aplicação inicializar${NC}"
echo -e "${RED}❌ Erro ao iniciar o ambiente${NC}"
docker-compose logs app
exit 1