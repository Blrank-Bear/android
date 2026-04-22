package com.tictactoe.app.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tictactoe.app.data.local.TokenDataStore
import com.tictactoe.app.data.remote.WebSocketManager
import com.tictactoe.app.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameUiState(
    val isLoading: Boolean = true,
    // 9-cell board, each cell is " ", "X", or "O"
    val board: List<String> = List(9) { " " },
    val currentTurn: String = "X",
    // "in_progress" | "X" | "O" | "draw"
    val result: String = "in_progress",
    val playerX: String = "",
    val playerO: String = "",
    val mySymbol: String = "",
    val myUsername: String = "",
    val lastMovePosition: Int? = null,
    // Positions [a, b, c] of the winning line, null if no winner yet
    val winningLine: List<Int>? = null,
    val connectionState: String = "Connecting...",
    val error: String? = null
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val webSocketManager: WebSocketManager,
    private val gameRepository: GameRepository,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var roomId: Int = -1

    init {
        observeWebSocket()
        observeConnectionState()
    }

    fun initGame(roomId: Int) {
        this.roomId = roomId
        viewModelScope.launch {
            val username = tokenDataStore.username.firstOrNull() ?: ""
            _uiState.value = _uiState.value.copy(myUsername = username)
            webSocketManager.connect(roomId)
        }
    }

    private fun observeWebSocket() {
        viewModelScope.launch {
            webSocketManager.messages.collect { msg ->
                when (msg.type) {
                    "game_state" -> {
                        val board = msg.board?.map { it.toString() } ?: List(9) { " " }
                        val myUsername = _uiState.value.myUsername
                        val playerX = msg.playerX ?: _uiState.value.playerX
                        val playerO = msg.playerO ?: _uiState.value.playerO
                        val mySymbol = when (myUsername) {
                            playerX -> "X"
                            playerO -> "O"
                            else    -> _uiState.value.mySymbol
                        }
                        _uiState.value = _uiState.value.copy(
                            isLoading        = false,
                            board            = board,
                            currentTurn      = msg.currentTurn ?: _uiState.value.currentTurn,
                            result           = msg.result ?: _uiState.value.result,
                            playerX          = playerX,
                            playerO          = playerO,
                            mySymbol         = mySymbol,
                            lastMovePosition = msg.lastMove?.position,
                            winningLine      = msg.winningLine,
                            error            = null
                        )
                    }
                    "error" -> {
                        _uiState.value = _uiState.value.copy(error = msg.message)
                    }
                }
            }
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            webSocketManager.connectionState.collect { state ->
                val stateStr = when (state) {
                    is WebSocketManager.ConnectionState.Connecting   -> "Connecting..."
                    is WebSocketManager.ConnectionState.Connected    -> "Connected"
                    is WebSocketManager.ConnectionState.Disconnected -> "Disconnected"
                    is WebSocketManager.ConnectionState.Error        -> "Error: ${state.message}"
                }
                _uiState.value = _uiState.value.copy(connectionState = stateStr)
            }
        }
    }

    fun makeMove(position: Int) {
        val state = _uiState.value
        if (state.result != "in_progress") return
        if (state.currentTurn != state.mySymbol) return
        if (state.board[position] != " ") return
        webSocketManager.sendMove(position)
    }

    fun requestRematch() {
        webSocketManager.sendRematch()
        // Optimistically show loading while waiting for server response
        _uiState.value = _uiState.value.copy(isLoading = true)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }
}
