-if class com.tictactoe.app.data.model.Move
-keepnames class com.tictactoe.app.data.model.Move
-if class com.tictactoe.app.data.model.Move
-keep class com.tictactoe.app.data.model.MoveJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
