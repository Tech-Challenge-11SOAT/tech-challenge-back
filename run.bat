@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ===========================
echo 🚀 Iniciando ambiente DEV...
echo ===========================

:: Verificar se o Docker está rodando
docker info > nul 2>&1
if errorlevel 1 (
    echo ❌ Docker não está em execução. Por favor, inicie o Docker Desktop.
    pause
    exit /b 1
)

:: Ir para a pasta docker
cd /d "%~dp0docker"
if errorlevel 1 (
    echo ❌ Erro ao acessar a pasta docker
    pause
    exit /b 1
)

:: Parar e remover containers e volumes anteriores
echo.
echo 🔁 Parando containers antigos e removendo volumes...
docker-compose down -v

:: Verificar conectividade com Maven Central
echo.
echo 🌐 Verificando conexão com Maven Central...
curl -s -m 10 https://repo.maven.apache.org/maven2 > nul
if errorlevel 1 (
    echo ❌ Não foi possível conectar ao Maven Central. Verifique sua conexão com a internet.
    pause
    exit /b 1
)

:: Subir ambiente com build
echo.
echo 🔨 Iniciando build e subida dos containers...
echo ⏳ Isso pode demorar alguns minutos na primeira execução...
echo 📦 Baixando dependências e construindo a aplicação...
echo.

:: Tentar até 3 vezes em caso de falha
set /a retries=0
:build_retry
docker-compose up --build -d
if %errorlevel% neq 0 (    set /a retries+=1
    if !retries! lss 3 (
        echo ⚠️ Falha no build. Tentativa !retries! de 3...
        timeout /t 5
        goto build_retry
    )
    echo ❌ Erro ao executar docker-compose
    pause
    exit /b 1
)

:: Aguardar a aplicação inicializar
echo.
echo 🔄 Aguardando a aplicação inicializar...
echo ⏳ Isso pode levar alguns instantes...

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
echo ✅ Ambiente DEV iniciado com sucesso!
echo.
echo 📍 Endpoints disponíveis:
echo    API: http://localhost:8080
echo    Swagger: http://localhost:8080/swagger-ui/index.html
echo    Banco de dados: localhost:3307
echo.
start http://localhost:8080/swagger-ui/index.html
goto :end

:error
echo ❌ Erro ao iniciar o ambiente
docker-compose logs app
goto :end

:end
pause
