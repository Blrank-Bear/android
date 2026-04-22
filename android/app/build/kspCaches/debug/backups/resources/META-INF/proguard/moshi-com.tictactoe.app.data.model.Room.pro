-if class com.tictactoe.app.data.model.Room
-keepnames class com.tictactoe.app.data.model.Room
-if class com.tictactoe.app.data.model.Room
-keep class com.tictactoe.app.data.model.RoomJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
