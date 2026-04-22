package com.tictactoe.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    @Json(name = "password_confirm") val passwordConfirm: String
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val username: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class TokenRefreshRequest(
    val refresh: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val user: User,
    val access: String,
    val refresh: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val access: String,
    val refresh: String
)

@JsonClass(generateAdapter = true)
data class TokenRefreshResponse(
    val access: String
)

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val username: String,
    val email: String,
    @Json(name = "created_at") val createdAt: String
)
