package com.tictactoe.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Game(
    val id: Int,
    val room: Room,
    @Json(name = "player_x") val playerX: User,
    @Json(name = "player_o") val playerO: User,
    @Json(name = "board_state") val boardState: String,
    @Json(name = "current_turn") val currentTurn: String,
    val result: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "finished_at") val finishedAt: String?,
    val moves: List<Move>
)

@JsonClass(generateAdapter = true)
data class Move(
    val id: Int,
    val player: User,
    val position: Int,
    val symbol: String,
    val timestamp: String
)

@JsonClass(generateAdapter = true)
data class GameHistory(
    val id: Int,
    @Json(name = "room_name") val roomName: String,
    @Json(name = "player_x") val playerX: User,
    @Json(name = "player_o") val playerO: User,
    val result: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "finished_at") val finishedAt: String?
)

// WebSocket message models
@JsonClass(generateAdapter = true)
data class WsMakeMoveMessage(
    val action: String = "make_move",
    val position: Int
)

@JsonClass(generateAdapter = true)
data class WsRematchMessage(
    val action: String = "rematch"
)

@JsonClass(generateAdapter = true)
data class WsGameStateMessage(
    val type: String,
    val board: String?,
    @Json(name = "current_turn") val currentTurn: String?,
    val result: String?,
    @Json(name = "player_x") val playerX: String?,
    @Json(name = "player_o") val playerO: String?,
    @Json(name = "last_move") val lastMove: WsLastMove?,
    @Json(name = "winning_line") val winningLine: List<Int>?,
    val message: String?
)

@JsonClass(generateAdapter = true)
data class WsLastMove(
    val position: Int,
    val symbol: String
)
