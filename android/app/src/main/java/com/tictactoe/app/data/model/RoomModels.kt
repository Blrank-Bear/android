package com.tictactoe.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Room(
    val id: Int,
    val name: String,
    val host: User,
    val guest: User?,
    val status: String,
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class CreateRoomRequest(
    val name: String
)

@JsonClass(generateAdapter = true)
data class JoinRoomResponse(
    val room: Room,
    @Json(name = "game_id") val gameId: Int
)
