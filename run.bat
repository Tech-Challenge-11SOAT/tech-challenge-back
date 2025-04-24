@echo off
setlocal enabledelayedexpansion

echo ===========================
echo 🚀 Iniciando ambiente DEV...
echo ===========================

:: Ir para a pasta docker
cd /d "%~dp0docker"

:: Parar e remover containers e volumes anteriores
echo.
echo 🔁 Parando containers antigos e removendo volumes...
docker-compose down -v

:: Subir ambiente com build
echo.
echo 🔨 Subindo containers com --build...
docker-compose up --build

:: Fim
echo.
echo ✅ Ambiente DEV iniciado com sucesso!

:: Abrir navegador após alguns segundos (tempo de subir a app)
timeout /t 5 >nul
start http://localhost:8080/swagger-ui.html

pause
