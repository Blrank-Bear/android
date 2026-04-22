package com.tictactoe.app.ui.rooms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tictactoe.app.ui.components.*
import com.tictactoe.app.ui.theme.*

@Composable
fun CreateRoomScreen(
    viewModel: RoomViewModel = hiltViewModel(),
    onRoomCreated: (Int) -> Unit,
    onBack: () -> Unit
) {
    val createState by viewModel.createState.collectAsState()
    var roomName by remember { mutableStateOf("") }

    LaunchedEffect(createState.createdRoomId, createState.navigated) {
        val roomId = createState.createdRoomId ?: return@LaunchedEffect
        if (createState.navigated) return@LaunchedEffect
        viewModel.markCreateNavigated()
        onRoomCreated(roomId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, NavyCard, NavySurface)))
    ) {
        // Decorative orb
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-80).dp, y = 200.dp)
                .align(Alignment.CenterStart)
                .background(
                    Brush.radialGradient(listOf(Gold.copy(alpha = 0.07f), Gold.copy(alpha = 0f))),
                    RoundedCornerShape(50)
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
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
                Text("New Room", style = MaterialTheme.typography.titleLarge, color = Ivory)
            }

            GoldDivider()

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Brush.radialGradient(listOf(GoldDark.copy(alpha = 0.3f), Gold.copy(alpha = 0.05f))),
                            RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Create a Room",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Ivory,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Name your arena and wait for a challenger",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IvoryMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(36.dp))

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    LuxuryTextField(
                        value = roomName,
                        onValueChange = { roomName = it },
                        label = "Room Name",
                        leadingIcon = {
                            Text("⚔", fontSize = 18.sp, modifier = Modifier.padding(start = 4.dp))
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    createState.error?.let { ErrorBanner(it) }

                    Spacer(modifier = Modifier.height(20.dp))

                    GoldButton(
                        text = "CREATE & WAIT",
                        onClick = { viewModel.createRoom(roomName) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = roomName.isNotBlank(),
                        isLoading = createState.isLoading
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1.5f))
        }
    }
}
