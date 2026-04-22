package com.tictactoe.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Always use the luxury dark scheme — no dynamic color, no light mode
private val LuxuryDarkColorScheme = darkColorScheme(
    primary              = Gold,
    onPrimary            = Navy,
    primaryContainer     = GoldDark,
    onPrimaryContainer   = GoldLight,

    secondary            = SapphireBlue,
    onSecondary          = Ivory,
    secondaryContainer   = NavyElevated,
    onSecondaryContainer = IvoryMuted,

    tertiary             = EmeraldGreen,
    onTertiary           = Navy,

    background           = Navy,
    onBackground         = Ivory,

    surface              = NavyCard,
    onSurface            = Ivory,
    surfaceVariant       = NavySurface,
    onSurfaceVariant     = IvoryMuted,

    outline              = GlassBorder,
    outlineVariant       = GlassWhite,

    error                = RoseRed,
    onError              = Ivory,
    errorContainer       = Color(0xFF3D1A1E),
    onErrorContainer     = RoseRed,

    inverseSurface       = Ivory,
    inverseOnSurface     = Navy,
    inversePrimary       = GoldDark,

    scrim                = Overlay
)

@Composable
fun TicTacToeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LuxuryDarkColorScheme,
        typography  = Typography,
        shapes      = Shapes,
        content     = content
    )
}
