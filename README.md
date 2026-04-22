# Tic Tac Toe — Full Stack

A real-time multiplayer Tic Tac Toe game with a Django backend and a native Android client.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Backend](#backend)
  - [Tech Stack](#backend-tech-stack)
  - [Prerequisites](#backend-prerequisites)
  - [Setup](#backend-setup)
  - [Running the Server](#running-the-server)
  - [API Reference](#api-reference)
  - [WebSocket Protocol](#websocket-protocol)
- [Android](#android)
  - [Tech Stack](#android-tech-stack)
  - [Prerequisites](#android-prerequisites)
  - [Configuration](#android-configuration)
  - [Building & Running](#building--running)
  - [App Structure](#app-structure)
- [Game Rules](#game-rules)

---

## Overview

Two players connect to a shared room and play a standard 3×3 Tic Tac Toe game in real time. The host creates a room and waits; a second player joins. Moves are broadcast instantly via WebSocket. After a game ends, players can request a rematch — sides swap automatically.

---

## Architecture

```
┌─────────────────────────────────────────────────────┐
│                   Android Client                    │
│  Jetpack Compose UI  ←→  ViewModel  ←→  Repository │
│       REST (Retrofit)          WebSocket (OkHttp)   │
└──────────────────────┬──────────────────────────────┘
                       │ HTTP / WS
┌──────────────────────▼──────────────────────────────┐
│               Django + Channels (Daphne)            │
│   REST API (DRF + JWT)    WebSocket Consumer        │
│              PostgreSQL Database                    │
└─────────────────────────────────────────────────────┘
```

---

## Project Structure

```
.
├── backend/                  Django ASGI backend
│   ├── accounts/             User registration & authentication
│   ├── games/                Game logic, WebSocket consumer, move tracking
│   ├── rooms/                Room creation & joining
│   ├── tictactoe/            Django project settings & ASGI config
│   ├── requirements.txt
│   ├── .env                  Environment variables (DB credentials, secret key)
│   └── run_backend.bat       One-click server launcher (Windows)
│
└── android/                  Native Android client
    └── app/src/main/java/com/tictactoe/app/
        ├── data/
        │   ├── local/        TokenDataStore (JWT persistence)
        │   ├── model/        Moshi data models
        │   ├── remote/       Retrofit ApiService, WebSocketManager, AuthInterceptor
        │   └── repository/   AuthRepository, RoomRepository, GameRepository
        ├── di/               Hilt dependency injection modules
        ├── ui/
        │   ├── auth/         Login & Register screens
        │   ├── game/         Game board screen
        │   ├── history/      Match history screen
        │   ├── navigation/   NavGraph
        │   ├── rooms/        Room list & create room screens
        │   ├── components/   Shared luxury UI components
        │   └── theme/        Color, typography, shape tokens
        └── util/             Result wrapper, safeApiCall
```

---

## Backend

### Backend Tech Stack

| Library | Version | Purpose |
|---|---|---|
| Django | 4.2.7 | Web framework |
| Django REST Framework | 3.14.0 | REST API |
| djangorestframework-simplejwt | 5.3.1 | JWT authentication |
| Django Channels | 4.0.0 | WebSocket support |
| Daphne | 4.0.0 | ASGI server |
| psycopg2-binary | 2.9.9 | PostgreSQL driver |
| django-cors-headers | 4.3.1 | CORS handling |

### Backend Prerequisites

- Python 3.10+
- PostgreSQL 14+
- Virtual environment (recommended)

### Backend Setup

**1. Create and activate a virtual environment**

```bash
cd backend
python -m venv venv

# Windows
venv\Scripts\activate

# macOS / Linux
source venv/bin/activate
```

**2. Install dependencies**

```bash
pip install -r requirements.txt
```

**3. Configure environment variables**

Edit `backend/.env`:

```env
SECRET_KEY=your-secret-key-here
DEBUG=True
DB_NAME=tictactoe_db
DB_USER=postgres
DB_PASSWORD=your_password
DB_HOST=localhost
DB_PORT=5432
```

**4. Create the PostgreSQL database**

```sql
CREATE DATABASE tictactoe_db;
```

**5. Apply migrations**

```bash
python manage.py migrate
```

**6. (Optional) Create a superuser for the admin panel**

```bash
python manage.py createsuperuser
```

### Running the Server

**Option A — Double-click (Windows)**

```
backend/run_backend.bat
```

**Option B — Command line**

```bash
cd backend
venv\Scripts\python.exe -m daphne -b 0.0.0.0 -p 8000 tictactoe.asgi:application
```

The server starts at:

| Endpoint | URL |
|---|---|
| HTTP API | `http://localhost:8000` |
| WebSocket | `ws://localhost:8000/ws/game/{room_id}/?token=<jwt>` |
| Admin panel | `http://localhost:8000/admin/` |

---

### API Reference

All endpoints except `register` and `login` require:
```
Authorization: Bearer <access_token>
```

#### Auth

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register/` | Register a new user |
| `POST` | `/api/auth/login/` | Login, receive JWT tokens |
| `POST` | `/api/auth/token/refresh/` | Refresh access token |
| `GET` | `/api/auth/profile/` | Get current user profile |

**Register request**
```json
{
  "username": "player1",
  "email": "player1@example.com",
  "password": "secret123",
  "password_confirm": "secret123"
}
```

**Login request**
```json
{ "username": "player1", "password": "secret123" }
```

**Login response**
```json
{ "access": "<jwt_access_token>", "refresh": "<jwt_refresh_token>" }
```

#### Rooms

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/rooms/` | List rooms (filter: `?status=waiting\|playing\|finished`) |
| `POST` | `/api/rooms/` | Create a room |
| `GET` | `/api/rooms/{id}/` | Get room details |
| `POST` | `/api/rooms/{id}/join/` | Join a waiting room |

**Create room request**
```json
{ "name": "My Arena" }
```

**Join room response**
```json
{
  "room": { "id": 1, "name": "My Arena", "status": "playing", ... },
  "game_id": 42
}
```

#### Games

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/games/history/` | Get current user's game history |
| `GET` | `/api/games/{id}/` | Get full game details with moves |

---

### WebSocket Protocol

Connect to:
```
ws://localhost:8000/ws/game/{room_id}/?token=<access_token>
```

#### On connect — server sends initial state

```json
{
  "type": "game_state",
  "board": "         ",
  "current_turn": "X",
  "result": "in_progress",
  "player_x": "alice",
  "player_o": "bob"
}
```

#### Client → Server messages

**Make a move**
```json
{ "action": "make_move", "position": 4 }
```
Positions are 0–8, mapped as:
```
0 | 1 | 2
---------
3 | 4 | 5
---------
6 | 7 | 8
```

**Request rematch**
```json
{ "action": "rematch" }
```

#### Server → Client messages

**Game state update** (broadcast to both players after every move or rematch)
```json
{
  "type": "game_state",
  "board": "X O X    ",
  "current_turn": "O",
  "result": "in_progress",
  "last_move": { "position": 2, "symbol": "X" },
  "winning_line": null
}
```

`result` values: `"in_progress"` · `"X"` · `"O"` · `"draw"`  
`winning_line` example: `[0, 4, 8]` (diagonal win)

**Error**
```json
{ "type": "error", "message": "It is not your turn." }
```

---

## Android

### Android Tech Stack

| Library | Version | Purpose |
|---|---|---|
| Kotlin | 2.0.21 | Language |
| Jetpack Compose BOM | 2024.09.03 | Declarative UI |
| Material3 | — | Design system |
| Hilt | 2.52 | Dependency injection |
| Retrofit | 2.11.0 | REST API client |
| OkHttp | 4.12.0 | HTTP + WebSocket client |
| Moshi | 1.15.1 | JSON serialization |
| Navigation Compose | 2.8.2 | Screen navigation |
| DataStore Preferences | 1.1.1 | JWT token persistence |
| Kotlin Coroutines | 1.9.0 | Async / reactive |

### Android Prerequisites

- Android Studio Hedgehog or newer
- JDK 17 (set in Android Studio: **File → Settings → Build Tools → Gradle → Gradle JDK**)
- Android device or emulator running API 26+

### Android Configuration

Open `android/app/src/main/java/com/tictactoe/app/data/remote/NetworkConfig.kt`:

```kotlin
val baseUrl: String = "http://10.0.2.2:8000/"   // emulator → localhost
val wsBaseUrl: String = "ws://10.0.2.2:8000/"
```

| Target | Address |
|---|---|
| Android Emulator | `10.0.2.2` (maps to host machine localhost) |
| Physical device (same Wi-Fi) | Your machine's local IP, e.g. `192.168.1.100` |

### Building & Running

1. Open the `android/` folder in Android Studio
2. Wait for Gradle sync to complete
3. Select a device or emulator
4. Click **Run ▶**

Or build from the command line:

```bash
cd android
./gradlew assembleDebug
```

The APK will be at `android/app/build/outputs/apk/debug/app-debug.apk`.

### App Structure

#### Screens

| Screen | Description |
|---|---|
| **Login** | Sign in with username and password |
| **Register** | Create a new account |
| **Game Lobby** | Browse, filter, and join rooms; create a new room |
| **Create Room** | Name and create a new game room |
| **Game** | Live 3×3 board with real-time WebSocket updates |
| **History** | View past match results |

#### Navigation flow

```
Login ──► Register
  │
  ▼
Game Lobby ──► Create Room ──► Game
     │                          │
     └──► Join Room ────────────┘
     │
     └──► History
```

#### Key design decisions

- **MVVM** — each screen has a dedicated ViewModel; UI state is exposed as `StateFlow`
- **Hilt** — all dependencies injected; ViewModels scoped to nav back stack entries
- **JWT auth** — tokens stored in DataStore, attached to every REST request via `AuthInterceptor`, and passed as a query param for WebSocket connections
- **One-shot navigation guard** — `navigated` flag in state prevents `LaunchedEffect` from re-triggering navigation on recomposition

---

## Game Rules

- The board is a 3×3 grid with positions numbered 0–8
- Player X always moves first
- Players alternate turns
- A player wins by placing three of their symbols in a row, column, or diagonal
- If all 9 cells are filled with no winner, the game is a draw
- After a game ends, either player can request a rematch — sides swap (X becomes O and vice versa)
