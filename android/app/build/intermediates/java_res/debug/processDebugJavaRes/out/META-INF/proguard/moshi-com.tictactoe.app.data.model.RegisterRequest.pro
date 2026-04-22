-if class com.tictactoe.app.data.model.RegisterRequest
-keepnames class com.tictactoe.app.data.model.RegisterRequest
-if class com.tictactoe.app.data.model.RegisterRequest
-keep class com.tictactoe.app.data.model.RegisterRequestJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
