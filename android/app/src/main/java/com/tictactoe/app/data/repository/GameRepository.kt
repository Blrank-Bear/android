package com.tictactoe.app.data.repository

import com.tictactoe.app.data.model.Game
import com.tictactoe.app.data.model.GameHistory
import com.tictactoe.app.data.remote.ApiService
import com.tictactoe.app.util.Result
import com.tictactoe.app.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getGame(id: Int): Result<Game> =
        safeApiCall { apiService.getGame(id) }

    suspend fun getGameHistory(): Result<List<GameHistory>> =
        safeApiCall { apiService.getGameHistory() }
}
