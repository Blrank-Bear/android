-if class com.tictactoe.app.data.model.WsGameStateMessage
-keepnames class com.tictactoe.app.data.model.WsGameStateMessage
-if class com.tictactoe.app.data.model.WsGameStateMessage
-keep class com.tictactoe.app.data.model.WsGameStateMessageJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
