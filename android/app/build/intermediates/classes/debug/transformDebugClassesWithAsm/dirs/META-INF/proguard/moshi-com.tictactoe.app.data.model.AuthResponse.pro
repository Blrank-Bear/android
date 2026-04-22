-if class com.tictactoe.app.data.model.AuthResponse
-keepnames class com.tictactoe.app.data.model.AuthResponse
-if class com.tictactoe.app.data.model.AuthResponse
-keep class com.tictactoe.app.data.model.AuthResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
