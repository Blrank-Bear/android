package com.tictactoe.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tictactoe.app.ui.components.*
import com.tictactoe.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) onRegisterSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, NavyCard, NavySurface)))
    ) {
        // Decorative orb
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = 100.dp, y = (-60).dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(listOf(Gold.copy(alpha = 0.10f), Gold.copy(alpha = 0f))),
                    RoundedCornerShape(50)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Gold
                    )
                }
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.titleLarge,
                    color = Ivory
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Join the Arena",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Ivory,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Create your account and start playing",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IvoryMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                GlassCard(modifier = Modifier.fillMaxWidth()) {
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
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = IvoryMuted)
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
                    Spacer(modifier = Modifier.height(14.dp))

                    LuxuryTextField(
                        value = passwordConfirm,
                        onValueChange = { passwordConfirm = it },
                        label = "Confirm Password",
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
                        text = "CREATE ACCOUNT",
                        onClick = {
                            viewModel.register(username, email, password, passwordConfirm)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = username.isNotBlank() && email.isNotBlank() && password.isNotBlank(),
                        isLoading = uiState.isLoading
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = IvoryMuted)) { append("Already have an account? ") }
                            withStyle(SpanStyle(color = Gold, fontWeight = FontWeight.SemiBold)) {
                                append("Sign in")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
