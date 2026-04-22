package com.tictactoe.app.data.repository

import com.tictactoe.app.data.model.CreateRoomRequest
import com.tictactoe.app.data.model.JoinRoomResponse
import com.tictactoe.app.data.model.Room
import com.tictactoe.app.data.remote.ApiService
import com.tictactoe.app.util.Result
import com.tictactoe.app.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getRooms(status: String? = null): Result<List<Room>> =
        safeApiCall { apiService.getRooms(status) }

    suspend fun createRoom(name: String): Result<Room> =
        safeApiCall { apiService.createRoom(CreateRoomRequest(name)) }

    suspend fun getRoom(id: Int): Result<Room> =
        safeApiCall { apiService.getRoom(id) }

    suspend fun joinRoom(id: Int): Result<JoinRoomResponse> =
        safeApiCall { apiService.joinRoom(id) }
}
