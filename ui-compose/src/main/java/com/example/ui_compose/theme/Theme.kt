package com.yourname.github.ui.compose.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryPurple = Color(0xFF6200EE)

@Composable
fun GitHubTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        background = Color.White,
        primary = PrimaryPurple,
        onPrimary = Color.White
    )
    MaterialTheme(colorScheme = colorScheme, content = content)
}