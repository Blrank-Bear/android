package com.tictactoe.app.ui.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tictactoe.app.data.model.Room
import com.tictactoe.app.data.repository.RoomRepository
import com.tictactoe.app.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomListUiState(
    val isLoading: Boolean = false,
    val rooms: List<Room> = emptyList(),
    val error: String? = null,
    val statusFilter: String? = "waiting"
)

data class CreateRoomUiState(
    val isLoading: Boolean = false,
    val createdRoomId: Int? = null,
    val error: String? = null,
    val navigated: Boolean = false
)

data class JoinRoomUiState(
    val isLoading: Boolean = false,
    val joinedGameId: Int? = null,
    val joinedRoomId: Int? = null,
    val error: String? = null,
    val navigated: Boolean = false
)

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _listState = MutableStateFlow(RoomListUiState())
    val listState: StateFlow<RoomListUiState> = _listState.asStateFlow()

    private val _createState = MutableStateFlow(CreateRoomUiState())
    val createState: StateFlow<CreateRoomUiState> = _createState.asStateFlow()

    private val _joinState = MutableStateFlow(JoinRoomUiState())
    val joinState: StateFlow<JoinRoomUiState> = _joinState.asStateFlow()

    init {
        loadRooms()
    }

    fun loadRooms(status: String? = "waiting") {
        viewModelScope.launch {
            _listState.value = _listState.value.copy(
                isLoading = true,
                error = null,
                statusFilter = status
            )
            when (val result = roomRepository.getRooms(status)) {
                is Result.Success -> _listState.value = _listState.value.copy(
                    isLoading = false,
                    rooms = result.data
                )
                is Result.Error -> _listState.value = _listState.value.copy(
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }

    fun createRoom(name: String) {
        if (name.isBlank()) {
            _createState.value = _createState.value.copy(error = "Room name cannot be empty")
            return
        }
        viewModelScope.launch {
            _createState.value = _createState.value.copy(
                isLoading = true,
                error = null,
                navigated = false
            )
            when (val result = roomRepository.createRoom(name)) {
                is Result.Success -> _createState.value = _createState.value.copy(
                    isLoading = false,
                    createdRoomId = result.data.id,
                    navigated = false
                )
                is Result.Error -> _createState.value = _createState.value.copy(
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }

    fun joinRoom(roomId: Int) {
        viewModelScope.launch {
            _joinState.value = _joinState.value.copy(
                isLoading = true,
                error = null,
                navigated = false
            )
            when (val result = roomRepository.joinRoom(roomId)) {
                is Result.Success -> _joinState.value = _joinState.value.copy(
                    isLoading = false,
                    joinedGameId = result.data.gameId,
                    joinedRoomId = result.data.room.id,
                    navigated = false
                )
                is Result.Error -> _joinState.value = _joinState.value.copy(
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }

    fun markCreateNavigated() {
        _createState.value = _createState.value.copy(
            navigated = true,
            createdRoomId = null
        )
    }

    fun markJoinNavigated() {
        _joinState.value = _joinState.value.copy(
            navigated = true,
            joinedRoomId = null,
            joinedGameId = null
        )
    }

    fun resetCreateState() {
        _createState.value = CreateRoomUiState()
    }

    fun resetJoinState() {
        _joinState.value = JoinRoomUiState()
    }

    fun clearListError() {
        _listState.value = _listState.value.copy(error = null)
    }
}
