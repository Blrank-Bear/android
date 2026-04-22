package com.tictactoe.app.ui.rooms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
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
import com.tictactoe.app.data.model.Room
import com.tictactoe.app.ui.components.ErrorBanner
import com.tictactoe.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(
    viewModel: RoomViewModel = hiltViewModel(),
    onNavigateToCreateRoom: () -> Unit,
    onNavigateToGame: (Int) -> Unit,
    onNavigateToHistory: () -> Unit,
    onLogout: () -> Unit
) {
    val listState by viewModel.listState.collectAsState()
    val joinState by viewModel.joinState.collectAsState()

    LaunchedEffect(joinState.joinedRoomId, joinState.navigated) {
        val roomId = joinState.joinedRoomId ?: return@LaunchedEffect
        if (joinState.navigated) return@LaunchedEffect
        viewModel.markJoinNavigated()
        onNavigateToGame(roomId)
    }

    var showLogoutDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("waiting") }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = NavyCard,
            titleContentColor = Ivory,
            textContentColor = IvoryMuted,
            title = { Text("Sign Out") },
            text = { Text("Are you sure you want to sign out?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("Sign Out", color = RoseRed, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = IvoryMuted)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, NavyCard)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "GAME LOBBY",
                        style = MaterialTheme.typography.labelLarge,
                        color = Gold,
                        letterSpacing = 3.sp
                    )
                    Text(
                        text = "Choose your battle",
                        style = MaterialTheme.typography.bodySmall,
                        color = IvoryMuted
                    )
                }
                IconButton(onClick = { viewModel.loadRooms(selectedFilter) }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = IvoryMuted)
                }
                IconButton(onClick = onNavigateToHistory) {
                    Icon(Icons.Default.List, contentDescription = "History", tint = IvoryMuted)
                }
                TextButton(onClick = { showLogoutDialog = true }) {
                    Text("Sign Out", color = RoseRed, style = MaterialTheme.typography.labelLarge)
                }
            }

            GoldDivider()

            // Filter chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf(
                    "waiting" to "Waiting",
                    "playing" to "Live",
                    "finished" to "Finished",
                    null to "All"
                )
                items(filters) { (filter, label) ->
                    val isSelected = selectedFilter == (filter ?: "")
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected)
                                    Brush.horizontalGradient(listOf(GoldDark, Gold))
                                else
                                    Brush.horizontalGradient(listOf(GlassWhite, GlassWhite))
                            )
                            .border(
                                1.dp,
                                if (isSelected) Color.Transparent else GlassBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                selectedFilter = filter ?: ""
                                viewModel.loadRooms(filter)
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) Navy else IvoryMuted
                        )
                    }
                }
            }

            // Errors
            listState.error?.let {
                ErrorBanner(it, Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }
            joinState.error?.let {
                ErrorBanner(it, Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Content
            if (listState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Gold, strokeWidth = 3.dp)
                }
            } else if (listState.rooms.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎮", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No rooms available",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Ivory
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Be the first to create one!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = IvoryMuted,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        TextButton(onClick = onNavigateToCreateRoom) {
                            Text("+ Create Room", color = Gold, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(listState.rooms) { room ->
                        LuxuryRoomCard(
                            room = room,
                            isJoining = joinState.isLoading,
                            onJoin = { viewModel.joinRoom(room.id) },
                            onSpectate = { onNavigateToGame(room.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }

        // FAB
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.linearGradient(listOf(GoldDark, Gold)))
                    .clickable(onClick = onNavigateToCreateRoom),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Create Room",
                    tint = Navy,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun GoldDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(Color.Transparent, Gold.copy(alpha = 0.4f), Color.Transparent)
                )
            )
    )
}

@Composable
fun LuxuryRoomCard(
    room: Room,
    isJoining: Boolean,
    onJoin: () -> Unit,
    onSpectate: () -> Unit
) {
    val statusColor = when (room.status) {
        "waiting"  -> Gold
        "playing"  -> EmeraldGreen
        "finished" -> IvoryMuted
        else       -> IvoryMuted
    }
    val statusLabel = when (room.status) {
        "waiting"  -> "● Waiting"
        "playing"  -> "● Live"
        "finished" -> "✓ Finished"
        else       -> room.status
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NavyCard)
            .border(
                1.dp,
                Brush.linearGradient(listOf(GlassBorder, Color.Transparent, GlassBorder)),
                RoundedCornerShape(20.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .background(
                    Brush.verticalGradient(
                        listOf(statusColor.copy(alpha = 0.8f), statusColor.copy(alpha = 0.2f))
                    ),
                    RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                )
        )

        Column(
            modifier = Modifier.padding(start = 20.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Ivory,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = statusLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                PlayerTag(symbol = "✕", name = room.host.username, color = RoseRed)
                Text(
                    "vs",
                    color = IvoryMuted,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                PlayerTag(
                    symbol = "○",
                    name = room.guest?.username ?: "Open",
                    color = if (room.guest != null) SapphireBlue else IvoryMuted
                )
            }

            if (room.status == "waiting" || room.status == "playing") {
                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (room.status == "waiting") {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.horizontalGradient(listOf(GoldDark, Gold)))
                                .clickable(enabled = !isJoining, onClick = onJoin),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isJoining) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = Navy
                                )
                            } else {
                                Text(
                                    "JOIN GAME",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Navy
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GlassWhite)
                                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                                .clickable(onClick = onSpectate),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "WATCH LIVE",
                                style = MaterialTheme.typography.labelLarge,
                                color = EmeraldGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerTag(symbol: String, name: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(symbol, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(name, style = MaterialTheme.typography.bodySmall, color = IvoryMuted)
    }
}
