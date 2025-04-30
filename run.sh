echo "==========================="
echo "🚀 Iniciando ambiente DEV..."
echo "==========================="

# Ir para a pasta docker
cd "$(dirname "$0")/docker"

# Parar e remover containers e volumes anteriores
echo
echo "🔁 Parando containers antigos e removendo volumes..."
docker-compose down -v

# Subir ambiente com build
echo
echo "🔨 Subindo containers com --build..."
docker-compose up --build &

# Fim
echo
echo "✅ Ambiente DEV iniciado com sucesso!"

# Aguardar alguns segundos para subir a aplicação
sleep 5

# Abrir navegador
open http://localhost:8080/swagger-ui/index.html