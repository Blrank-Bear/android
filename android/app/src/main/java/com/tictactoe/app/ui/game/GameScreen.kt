package com.tictactoe.app.ui.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tictactoe.app.ui.components.ErrorBanner
import com.tictactoe.app.ui.rooms.GoldDivider
import com.tictactoe.app.ui.theme.*

@Composable
fun GameScreen(
    roomId: Int,
    viewModel: GameViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(roomId) { viewModel.initGame(roomId) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, NavyCard, NavySurface)))
    ) {
        // Decorative background orbs
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-60).dp, y = (-40).dp)
                .background(
                    Brush.radialGradient(listOf(RoseRed.copy(alpha = 0.06f), Color.Transparent)),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 40.dp)
                .background(
                    Brush.radialGradient(listOf(SapphireBlue.copy(alpha = 0.06f), Color.Transparent)),
                    CircleShape
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top bar ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Gold)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "ROOM #$roomId",
                        style = MaterialTheme.typography.labelLarge,
                        color = Gold,
                        letterSpacing = 2.sp
                    )
                }
                // Live connection indicator
                val dotColor = when {
                    uiState.connectionState == "Connected"          -> EmeraldGreen
                    uiState.connectionState.startsWith("Error")     -> RoseRed
                    else                                            -> Gold
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Box(modifier = Modifier.size(8.dp).background(dotColor, CircleShape))
                    Text(uiState.connectionState, style = MaterialTheme.typography.labelSmall, color = dotColor)
                }
            }

            GoldDivider()

            // ── Content ──────────────────────────────────────────────────────
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Gold, strokeWidth = 3.dp)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            "Waiting for opponent…",
                            color = IvoryMuted,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Player cards
                    LuxuryPlayerRow(
                        playerX    = uiState.playerX,
                        playerO    = uiState.playerO,
                        currentTurn = uiState.currentTurn,
                        mySymbol   = uiState.mySymbol,
                        result     = uiState.result
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Status banner
                    LuxuryStatusBanner(
                        result      = uiState.result,
                        currentTurn = uiState.currentTurn,
                        mySymbol    = uiState.mySymbol,
                        playerX     = uiState.playerX,
                        playerO     = uiState.playerO
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // 3×3 Board
                    LuxuryBoard(
                        board            = uiState.board,
                        lastMovePosition = uiState.lastMovePosition,
                        winningLine      = uiState.winningLine,
                        isMyTurn         = uiState.currentTurn == uiState.mySymbol
                                           && uiState.result == "in_progress",
                        onCellClick      = { viewModel.makeMove(it) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    uiState.error?.let { ErrorBanner(it) }

                    // Post-game buttons
                    if (uiState.result != "in_progress") {
                        Spacer(modifier = Modifier.height(24.dp))

                        // Rematch button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brush.horizontalGradient(listOf(GoldDark, Gold)))
                                .clickable { viewModel.requestRematch() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "⚔  REMATCH",
                                style = MaterialTheme.typography.labelLarge,
                                color = Navy
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Back to lobby
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(GlassWhite)
                                .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                                .clickable(onClick = onBack),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "BACK TO LOBBY",
                                style = MaterialTheme.typography.labelLarge,
                                color = IvoryMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

// ── Player row ────────────────────────────────────────────────────────────────
@Composable
fun LuxuryPlayerRow(
    playerX: String,
    playerO: String,
    currentTurn: String,
    mySymbol: String,
    result: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LuxuryPlayerCard(
            symbol      = "✕",
            username    = playerX,
            isActive    = currentTurn == "X" && result == "in_progress",
            isWinner    = result == "X",
            isMe        = mySymbol == "X",
            accentColor = RoseRed,
            modifier    = Modifier.weight(1f)
        )

        Text(
            "VS",
            style = MaterialTheme.typography.labelLarge,
            color = Gold,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        LuxuryPlayerCard(
            symbol      = "○",
            username    = playerO,
            isActive    = currentTurn == "O" && result == "in_progress",
            isWinner    = result == "O",
            isMe        = mySymbol == "O",
            accentColor = SapphireBlue,
            modifier    = Modifier.weight(1f)
        )
    }
}

@Composable
fun LuxuryPlayerCard(
    symbol: String,
    username: String,
    isActive: Boolean,
    isWinner: Boolean,
    isMe: Boolean,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val borderBrush = when {
        isWinner -> Brush.linearGradient(listOf(Gold, GoldLight))
        isActive -> Brush.linearGradient(listOf(accentColor, accentColor.copy(alpha = 0.3f)))
        else     -> Brush.linearGradient(listOf(GlassBorder, GlassBorder))
    }
    val bgColor by animateColorAsState(
        targetValue = when {
            isWinner -> Gold.copy(alpha = 0.10f)
            isActive -> accentColor.copy(alpha = 0.12f)
            else     -> GlassWhite
        },
        animationSpec = tween(400),
        label = "playerBg"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderBrush, RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isWinner) "🏆" else symbol,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = if (isWinner) Gold else accentColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = username.ifBlank { "Waiting…" },
            style = MaterialTheme.typography.bodySmall,
            color = if (isActive || isWinner) Ivory else IvoryMuted,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        if (isMe) {
            Spacer(modifier = Modifier.height(2.dp))
            Text("YOU", style = MaterialTheme.typography.labelSmall, color = Gold, letterSpacing = 1.sp)
        }
        if (isActive) {
            Spacer(modifier = Modifier.height(6.dp))
            TurnIndicatorDots(color = accentColor)
        }
    }
}

@Composable
private fun TurnIndicatorDots(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "dotAnim"
    )
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(3) { i ->
            val scale = if ((offset * 3).toInt() == i) 1.4f else 0.8f
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .scale(scale)
                    .background(color, CircleShape)
            )
        }
    }
}

// ── Status banner ─────────────────────────────────────────────────────────────
@Composable
fun LuxuryStatusBanner(
    result: String,
    currentTurn: String,
    mySymbol: String,
    playerX: String,
    playerO: String
) {
    val (emoji, text, color) = when (result) {
        "in_progress" -> {
            if (currentTurn == mySymbol)
                Triple("⚡", "Your Turn", Gold)
            else {
                val name = if (currentTurn == "X") playerX else playerO
                Triple("⏳", "$name's Turn", IvoryMuted)
            }
        }
        "X"    -> if (mySymbol == "X") Triple("🏆", "You Win!", Gold)
                  else Triple("💀", "$playerX Wins", IvoryMuted)
        "O"    -> if (mySymbol == "O") Triple("🏆", "You Win!", Gold)
                  else Triple("💀", "$playerO Wins", IvoryMuted)
        "draw" -> Triple("🤝", "It's a Draw!", SapphireBlue)
        else   -> Triple("", result, IvoryMuted)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(GlassWhite)
            .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(emoji, fontSize = 22.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── 3×3 Board ─────────────────────────────────────────────────────────────────
@Composable
fun LuxuryBoard(
    board: List<String>,
    lastMovePosition: Int?,
    winningLine: List<Int>?,
    isMyTurn: Boolean,
    onCellClick: (Int) -> Unit
) {
    val boardSize = 320.dp
    val gap = 8.dp
    val cellSize: Dp = (boardSize - gap * 2) / 3

    Box(
        modifier = Modifier
            .size(boardSize)
            .clip(RoundedCornerShape(24.dp))
            .background(NavyElevated)
            .border(
                1.dp,
                Brush.linearGradient(listOf(GlassBorder, Color.Transparent, GlassBorder)),
                RoundedCornerShape(24.dp)
            )
            .padding(gap)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(gap)) {
            for (row in 0..2) {
                Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                    for (col in 0..2) {
                        val position = row * 3 + col
                        LuxuryCell(
                            value       = board[position],
                            isLastMove  = position == lastMovePosition,
                            isWinning   = winningLine?.contains(position) == true,
                            isClickable = board[position] == " " && isMyTurn,
                            size        = cellSize,
                            onClick     = { onCellClick(position) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LuxuryCell(
    value: String,
    isLastMove: Boolean,
    isWinning: Boolean,
    isClickable: Boolean,
    size: Dp,
    onClick: () -> Unit
) {
    // Winning cells pulse with a gold glow
    val infiniteTransition = rememberInfiniteTransition(label = "winPulse")
    val winAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f, targetValue = 0.55f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "winAlpha"
    )

    val bgColor by animateColorAsState(
        targetValue = when {
            isWinning              -> Gold.copy(alpha = winAlpha)
            isLastMove && value == "X" -> RoseRed.copy(alpha = 0.15f)
            isLastMove && value == "O" -> SapphireBlue.copy(alpha = 0.15f)
            else                   -> NavyCard
        },
        animationSpec = tween(300),
        label = "cellBg"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isWinning  -> Gold.copy(alpha = 0.9f)
            isLastMove -> Gold.copy(alpha = 0.5f)
            isClickable -> GlassBorder
            else        -> GlassWhite
        },
        animationSpec = tween(300),
        label = "cellBorder"
    )

    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(enabled = isClickable, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when (value) {
            "X" -> Text(
                text = "✕",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = if (isWinning) Gold else RoseRed
            )
            "O" -> Text(
                text = "○",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = if (isWinning) Gold else SapphireBlue
            )
            else -> {
                // Show a subtle dot hint on empty clickable cells
                if (isClickable) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(GlassBorder, CircleShape)
                    )
                }
            }
        }
    }
}
