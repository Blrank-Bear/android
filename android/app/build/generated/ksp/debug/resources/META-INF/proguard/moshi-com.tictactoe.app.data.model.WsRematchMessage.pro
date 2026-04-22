-if class com.tictactoe.app.data.model.WsRematchMessage
-keepnames class com.tictactoe.app.data.model.WsRematchMessage
-if class com.tictactoe.app.data.model.WsRematchMessage
-keep class com.tictactoe.app.data.model.WsRematchMessageJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.tictactoe.app.data.model.WsRematchMessage
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.tictactoe.app.data.model.WsRematchMessage
-keepclassmembers class com.tictactoe.app.data.model.WsRematchMessage {
    public synthetic <init>(java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
