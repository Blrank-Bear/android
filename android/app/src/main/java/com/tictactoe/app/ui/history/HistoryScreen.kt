package com.tictactoe.app.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tictactoe.app.data.model.GameHistory
import com.tictactoe.app.ui.rooms.GoldDivider
import com.tictactoe.app.ui.theme.*

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, NavyCard)))
    ) {
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
                        "BATTLE HISTORY",
                        style = MaterialTheme.typography.labelLarge,
                        color = Gold,
                        letterSpacing = 3.sp
                    )
                    Text(
                        "Your past matches",
                        style = MaterialTheme.typography.bodySmall,
                        color = IvoryMuted
                    )
                }
            }

            GoldDivider()

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Gold, strokeWidth = 3.dp)
                    }
                }
                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚠", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(uiState.error!!, color = RoseRed,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.loadHistory() },
                                colors = ButtonDefaults.buttonColors(containerColor = Gold),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Retry", color = Navy, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                uiState.games.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📜", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No battles yet", style = MaterialTheme.typography.headlineSmall, color = Ivory)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Play your first game to see history here",
                                style = MaterialTheme.typography.bodyMedium,
                                color = IvoryMuted, textAlign = TextAlign.Center)
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.games) { game ->
                            HistoryCard(game = game)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(game: GameHistory) {
    val (resultEmoji, resultLabel, resultColor) = when (game.result) {
        "X"           -> Triple("✕", "X Wins", RoseRed)
        "O"           -> Triple("○", "O Wins", SapphireBlue)
        "draw"        -> Triple("🤝", "Draw", Gold)
        "in_progress" -> Triple("⏳", "In Progress", IvoryMuted)
        else          -> Triple("?", game.result, IvoryMuted)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NavyCard)
            .border(
                1.dp,
                Brush.linearGradient(listOf(GlassBorder, Color.Transparent)),
                RoundedCornerShape(20.dp)
            )
    ) {
        // Top accent line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, resultColor.copy(alpha = 0.7f), Color.Transparent)
                    )
                )
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = game.roomName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Ivory,
                    modifier = Modifier.weight(1f)
                )
                // Result badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(resultColor.copy(alpha = 0.12f))
                        .border(1.dp, resultColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(resultEmoji, fontSize = 12.sp)
                    Text(
                        resultLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = resultColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Players
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlayerHistoryTag(symbol = "✕", name = game.playerX.username, color = RoseRed,
                    modifier = Modifier.weight(1f))
                Text("vs", color = IvoryMuted, style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically))
                PlayerHistoryTag(symbol = "○", name = game.playerO.username, color = SapphireBlue,
                    modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dates
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "📅 ${formatDate(game.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = IvoryMuted
                )
                game.finishedAt?.let {
                    Text(
                        text = "🏁 ${formatDate(it)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = IvoryMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerHistoryTag(symbol: String, name: String, color: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(symbol, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(name, style = MaterialTheme.typography.bodySmall, color = Ivory, maxLines = 1)
    }
}

private fun formatDate(isoDate: String): String {
    return try {
        val parts = isoDate.split("T")
        if (parts.size >= 2) "${parts[0]} ${parts[1].take(5)}" else isoDate.take(16)
    } catch (e: Exception) { isoDate }
}
