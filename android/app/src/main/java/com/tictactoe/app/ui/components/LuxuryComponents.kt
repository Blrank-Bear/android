package com.tictactoe.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.tictactoe.app.ui.theme.*

// ── Glass card surface ────────────────────────────────────────────────────────
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(GlassWhite)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(listOf(GlassBorder, Color.Transparent)),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp),
        content = content
    )
}

// ── Gold gradient button ──────────────────────────────────────────────────────
@Composable
fun GoldButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val gradient = Brush.horizontalGradient(
        listOf(GoldDark, Gold, GoldLight, Gold, GoldDark)
    )
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier.height(54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Navy,
            disabledContainerColor = NavyElevated,
            disabledContentColor = IvoryMuted
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (enabled && !isLoading)
                        Modifier.background(gradient, RoundedCornerShape(14.dp))
                    else
                        Modifier.background(NavyElevated, RoundedCornerShape(14.dp))
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.5.dp,
                    color = Gold
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (enabled) Navy else IvoryMuted
                )
            }
        }
    }
}

// ── Luxury text field ─────────────────────────────────────────────────────────
@Composable
fun LuxuryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Gold,
            unfocusedBorderColor = GlassBorder,
            focusedLabelColor = Gold,
            unfocusedLabelColor = IvoryMuted,
            cursorColor = Gold,
            focusedTextColor = Ivory,
            unfocusedTextColor = Ivory,
            focusedContainerColor = GlassWhite,
            unfocusedContainerColor = GlassWhite
        )
    )
}

// ── Section divider with label ────────────────────────────────────────────────
@Composable
fun GoldDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(Color.Transparent, Gold.copy(alpha = 0.5f), Color.Transparent)
                )
            )
    )
}

// ── Error banner ──────────────────────────────────────────────────────────────
@Composable
fun ErrorBanner(message: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "⚠",
            color = RoseRed,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = message,
            color = RoseRed,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
