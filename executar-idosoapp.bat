@echo off
setlocal

set "ROOT=%~dp0"
set "APP_DIR=%ROOT%Idosoapp"
set "MAVEN=%ROOT%maven\mvn\bin\mvn.cmd"
set "JAR=%APP_DIR%\target\Idosoapp.jar"

if not exist "%MAVEN%" (
    echo Maven incluido nao encontrado em:
    echo %MAVEN%
    exit /b 1
)

if not exist "%JAR%" (
    echo Gerando JAR executavel...
    pushd "%APP_DIR%" >nul
    call "%MAVEN%" -q package
    if errorlevel 1 (
        popd >nul
        echo Falha ao gerar o JAR.
        exit /b 1
    )
    popd >nul
)

echo Abrindo Sistema de Gestao de Casa de Repouso...
java -jar "%JAR%"
endlocal
