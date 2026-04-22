-if class com.tictactoe.app.data.model.WsMakeMoveMessage
-keepnames class com.tictactoe.app.data.model.WsMakeMoveMessage
-if class com.tictactoe.app.data.model.WsMakeMoveMessage
-keep class com.tictactoe.app.data.model.WsMakeMoveMessageJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.tictactoe.app.data.model.WsMakeMoveMessage
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.tictactoe.app.data.model.WsMakeMoveMessage
-keepclassmembers class com.tictactoe.app.data.model.WsMakeMoveMessage {
    public synthetic <init>(java.lang.String,int,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
