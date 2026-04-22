package com.tictactoe.app.data.repository

import com.tictactoe.app.data.local.TokenDataStore
import com.tictactoe.app.data.model.LoginRequest
import com.tictactoe.app.data.model.RegisterRequest
import com.tictactoe.app.data.model.User
import com.tictactoe.app.data.remote.ApiService
import com.tictactoe.app.util.Result
import com.tictactoe.app.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenDataStore: TokenDataStore
) {
    val accessToken: Flow<String?> = tokenDataStore.accessToken
    val username: Flow<String?> = tokenDataStore.username

    suspend fun register(
        username: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): Result<User> {
        val result = safeApiCall {
            apiService.register(RegisterRequest(username, email, password, passwordConfirm))
        }
        if (result is Result.Success) {
            tokenDataStore.saveTokens(result.data.access, result.data.refresh)
            tokenDataStore.saveUserInfo(result.data.user.id, result.data.user.username)
            return Result.Success(result.data.user)
        }
        return Result.Error((result as Result.Error).message)
    }

    suspend fun login(username: String, password: String): Result<Unit> {
        val result = safeApiCall { apiService.login(LoginRequest(username, password)) }
        if (result is Result.Success) {
            tokenDataStore.saveTokens(result.data.access, result.data.refresh)
            // Fetch profile to get user info
            val profileResult = safeApiCall { apiService.getProfile() }
            if (profileResult is Result.Success) {
                tokenDataStore.saveUserInfo(profileResult.data.id, profileResult.data.username)
            }
            return Result.Success(Unit)
        }
        return Result.Error((result as Result.Error).message)
    }

    suspend fun getProfile(): Result<User> = safeApiCall { apiService.getProfile() }

    suspend fun logout() {
        tokenDataStore.clearAll()
    }

    suspend fun isLoggedIn(): Boolean {
        return !tokenDataStore.accessToken.firstOrNull().isNullOrBlank()
    }
}
