@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ===========================
echo [92m🚀 Iniciando ambiente DEV...[0m
echo ===========================

:: Verificar se o Docker está rodando
docker info > nul 2>&1
if %errorlevel% neq 0 (    echo [91m❌ Docker não está em execução. Por favor, inicie o Docker Desktop.[0m
    pause
    exit /b 1
)

:: Ir para a pasta docker
cd /d "%~dp0docker"
if %errorlevel% neq 0 (    echo [91m❌ Erro ao acessar a pasta docker[0m
    pause
    exit /b 1
)

:: Parar e remover containers e volumes anteriores
echo.
echo [93m🔁 Parando containers antigos e removendo volumes...[0m
docker-compose down -v

:: Verificar conectividade com Maven Central
echo.
echo [94m🌐 Verificando conexão com Maven Central...[0m
curl -s -m 10 https://repo.maven.apache.org/maven2 > nul
if %errorlevel% neq 0 (    echo [91m❌ Não foi possível conectar ao Maven Central. Verifique sua conexão com a internet.[0m
    pause
    exit /b 1
)

:: Subir ambiente com build
echo.
echo [96m🔨 Iniciando build e subida dos containers...[0m
echo [93m⏳ Isso pode demorar alguns minutos na primeira execução...[0m
echo [94m📦 Baixando dependências e construindo a aplicação...[0m

:: Tentar até 3 vezes em caso de falha
set /a retries=0
:build_retry
docker-compose up --build -d
if %errorlevel% neq 0 (
    set /a retries+=1
    if %retries% lss 3 (
        echo ⚠️ Falha no build. Tentativa %retries% de 3...
        timeout /t 5
        goto build_retry
    )
    echo ❌ Erro ao executar docker-compose
    pause
    exit /b 1
)

:: Aguardar a aplicação inicializar
echo.
echo [94m🔄 Aguardando a aplicação inicializar...[0m
echo [93m⏳ Isso pode levar alguns instantes...[0m

:: Aguardar até 60 segundos pela aplicação
for /l %%x in (1, 1, 12) do (
    timeout /t 5 >nul
    curl -s http://localhost:8080/actuator/health >nul 2>&1
    if !errorlevel! equ 0 (
        goto :success
    )
)
echo ❌ Timeout ao aguardar a aplicação inicializar
goto :error

:success
echo.
echo [92m✅ Ambiente DEV iniciado com sucesso![0m
echo.
echo [96m📍 Endpoints disponíveis:[0m
echo [97m   API:[0m http://localhost:8080
echo [97m   Swagger:[0m http://localhost:8080/swagger-ui/index.html
echo [97m   Banco de dados:[0m localhost:3307
echo.
start http://localhost:8080/swagger-ui/index.html
goto :end

:error
echo ❌ Erro ao iniciar o ambiente
docker-compose logs app
goto :end

:end
pause
