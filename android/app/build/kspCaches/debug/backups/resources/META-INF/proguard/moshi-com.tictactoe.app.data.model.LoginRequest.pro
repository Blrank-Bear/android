-if class com.tictactoe.app.data.model.LoginRequest
-keepnames class com.tictactoe.app.data.model.LoginRequest
-if class com.tictactoe.app.data.model.LoginRequest
-keep class com.tictactoe.app.data.model.LoginRequestJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
