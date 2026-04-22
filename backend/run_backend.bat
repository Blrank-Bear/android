@echo off
title Tic-Tac-Toe Backend
color 0A

echo.
echo  ============================================
echo    TIC TAC TOE  ^|  Backend Server
echo  ============================================
echo.

:: Move to the folder where this .bat lives
cd /d "%~dp0"

:: ── Check virtual environment ────────────────────────────────────────────────
if not exist "venv\Scripts\python.exe" (
    echo  [ERROR] Virtual environment not found.
    echo.
    echo  Please run the following commands first:
    echo    python -m venv venv
    echo    venv\Scripts\pip install -r requirements.txt
    echo.
    pause
    exit /b 1
)

:: ── Check daphne is installed ────────────────────────────────────────────────
venv\Scripts\python.exe -c "import daphne" 2>nul
if errorlevel 1 (
    echo  [ERROR] Daphne is not installed in the virtual environment.
    echo.
    echo  Please run:
    echo    venv\Scripts\pip install -r requirements.txt
    echo.
    pause
    exit /b 1
)

:: ── Apply migrations ─────────────────────────────────────────────────────────
echo  [1/2] Applying database migrations...
echo.
venv\Scripts\python.exe manage.py migrate --no-input
if errorlevel 1 (
    echo.
    echo  [ERROR] Migration failed. Check your database settings in .env
    echo.
    pause
    exit /b 1
)

:: ── Start server ─────────────────────────────────────────────────────────────
echo.
echo  [2/2] Starting Daphne ASGI server...
echo.
echo  ============================================
echo    HTTP   : http://localhost:8000
echo    WS     : ws://localhost:8000/ws/game/{id}/
echo    Admin  : http://localhost:8000/admin/
echo  ============================================
echo.
echo  Press Ctrl+C to stop the server.
echo.

venv\Scripts\python.exe -m daphne -b 0.0.0.0 -p 8000 tictactoe.asgi:application

echo.
echo  Server stopped.
pause
