-if class com.tictactoe.app.data.model.GameHistory
-keepnames class com.tictactoe.app.data.model.GameHistory
-if class com.tictactoe.app.data.model.GameHistory
-keep class com.tictactoe.app.data.model.GameHistoryJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
