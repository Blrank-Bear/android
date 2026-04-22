package com.tictactoe.app.data.remote

import com.tictactoe.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth
    @POST("api/auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/token/refresh/")
    suspend fun refreshToken(@Body request: TokenRefreshRequest): Response<TokenRefreshResponse>

    @GET("api/auth/profile/")
    suspend fun getProfile(): Response<User>

    // Rooms
    @GET("api/rooms/")
    suspend fun getRooms(@Query("status") status: String? = null): Response<List<Room>>

    @POST("api/rooms/")
    suspend fun createRoom(@Body request: CreateRoomRequest): Response<Room>

    @GET("api/rooms/{id}/")
    suspend fun getRoom(@Path("id") id: Int): Response<Room>

    @POST("api/rooms/{id}/join/")
    suspend fun joinRoom(@Path("id") id: Int): Response<JoinRoomResponse>

    // Games
    @GET("api/games/history/")
    suspend fun getGameHistory(): Response<List<GameHistory>>

    @GET("api/games/{id}/")
    suspend fun getGame(@Path("id") id: Int): Response<Game>
}
