-if class com.tictactoe.app.data.model.User
-keepnames class com.tictactoe.app.data.model.User
-if class com.tictactoe.app.data.model.User
-keep class com.tictactoe.app.data.model.UserJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
