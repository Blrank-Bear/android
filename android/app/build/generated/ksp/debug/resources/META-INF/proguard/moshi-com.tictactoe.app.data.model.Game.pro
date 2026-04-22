-if class com.tictactoe.app.data.model.Game
-keepnames class com.tictactoe.app.data.model.Game
-if class com.tictactoe.app.data.model.Game
-keep class com.tictactoe.app.data.model.GameJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
