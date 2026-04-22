package com.tictactoe.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tictactoe.app.ui.components.*
import com.tictactoe.app.ui.theme.*

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Navy, NavyCard, NavySurface)
                )
            )
    ) {
        // Decorative gold orb top-right
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = 120.dp, y = (-80).dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(
                        listOf(Gold.copy(alpha = 0.12f), Gold.copy(alpha = 0f))
                    ),
                    RoundedCornerShape(50)
                )
        )
        // Decorative orb bottom-left
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-80).dp, y = 80.dp)
                .align(Alignment.BottomStart)
                .background(
                    Brush.radialGradient(
                        listOf(SapphireBlue.copy(alpha = 0.10f), SapphireBlue.copy(alpha = 0f))
                    ),
                    RoundedCornerShape(50)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo / Title
            Text(
                text = "✕ ○",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Gold,
                letterSpacing = 8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "TIC TAC TOE",
                style = MaterialTheme.typography.headlineLarge,
                color = Ivory,
                letterSpacing = 6.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "The Classic Game, Reimagined",
                style = MaterialTheme.typography.bodyMedium,
                color = IvoryMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Ivory
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sign in to continue your journey",
                    style = MaterialTheme.typography.bodySmall,
                    color = IvoryMuted
                )

                Spacer(modifier = Modifier.height(24.dp))

                LuxuryTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Username",
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = IvoryMuted)
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                LuxuryTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = IvoryMuted)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                uiState.error?.let { ErrorBanner(it) }

                Spacer(modifier = Modifier.height(20.dp))

                GoldButton(
                    text = "SIGN IN",
                    onClick = { viewModel.login(username, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = username.isNotBlank() && password.isNotBlank(),
                    isLoading = uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register link
            TextButton(onClick = onNavigateToRegister) {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = IvoryMuted)) { append("New here? ") }
                        withStyle(SpanStyle(color = Gold, fontWeight = FontWeight.SemiBold)) {
                            append("Create an account")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
