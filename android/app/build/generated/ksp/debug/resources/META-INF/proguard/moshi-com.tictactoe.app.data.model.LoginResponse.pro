-if class com.tictactoe.app.data.model.LoginResponse
-keepnames class com.tictactoe.app.data.model.LoginResponse
-if class com.tictactoe.app.data.model.LoginResponse
-keep class com.tictactoe.app.data.model.LoginResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
