package com.tictactoe.app.data.remote

import com.squareup.moshi.Moshi
import com.tictactoe.app.data.local.TokenDataStore
import com.tictactoe.app.data.model.WsGameStateMessage
import com.tictactoe.app.data.model.WsMakeMoveMessage
import com.tictactoe.app.data.model.WsRematchMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val moshi: Moshi,
    private val tokenDataStore: TokenDataStore,
    private val networkConfig: NetworkConfig
) {
    private var webSocket: WebSocket? = null

    private val _messages = MutableSharedFlow<WsGameStateMessage>(extraBufferCapacity = 64)
    val messages: SharedFlow<WsGameStateMessage> = _messages.asSharedFlow()

    private val _connectionState = MutableSharedFlow<ConnectionState>(
        replay = 1,
        extraBufferCapacity = 8
    )
    val connectionState: SharedFlow<ConnectionState> = _connectionState.asSharedFlow()

    fun connect(roomId: Int) {
        disconnect()
        val token = runBlocking { tokenDataStore.accessToken.firstOrNull() }
        val url = "${networkConfig.wsBaseUrl}ws/game/$roomId/?token=$token"

        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _connectionState.tryEmit(ConnectionState.Connected)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val adapter = moshi.adapter(WsGameStateMessage::class.java)
                try {
                    val msg = adapter.fromJson(text)
                    if (msg != null) _messages.tryEmit(msg)
                } catch (e: Exception) {
                    // Ignore malformed messages
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _connectionState.tryEmit(ConnectionState.Error(t.message ?: "Connection failed"))
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _connectionState.tryEmit(ConnectionState.Disconnected)
            }
        })
        _connectionState.tryEmit(ConnectionState.Connecting)
    }

    fun sendMove(position: Int) {
        val adapter = moshi.adapter(WsMakeMoveMessage::class.java)
        val json = adapter.toJson(WsMakeMoveMessage(position = position))
        webSocket?.send(json)
    }

    fun sendRematch() {
        val adapter = moshi.adapter(WsRematchMessage::class.java)
        val json = adapter.toJson(WsRematchMessage())
        webSocket?.send(json)
    }

    fun disconnect() {
        webSocket?.close(1000, "Client disconnected")
        webSocket = null
        _connectionState.tryEmit(ConnectionState.Disconnected)
    }

    sealed class ConnectionState {
        object Connecting : ConnectionState()
        object Connected : ConnectionState()
        object Disconnected : ConnectionState()
        data class Error(val message: String) : ConnectionState()
    }
}
